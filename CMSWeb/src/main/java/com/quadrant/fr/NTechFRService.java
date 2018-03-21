package com.quadrant.fr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.note.cms.common.Constant;
import com.quadrant.FDCameraData;
import com.quadrant.FaceDefine;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
//import com.quadrant.face.DetectedFace;


public class NTechFRService {
	private static final Logger logger = LoggerFactory.getLogger(NTechFRService.class);
	private String host = null;//192.168.0.8
	private String gallery = null;//quadrant
	protected Gson gson = null;
	private String ntechToken = Constant.TOKEN;
	private String findFaceHost;
	private String findFacePort;
	private String findFaceVersion;
    private static JSONParser jsonParser = new JSONParser();
	
	
	public NTechFRService (){
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");//
		gson = builder.create();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getGallery() {
		return gallery;
	}

	public void setGallery(String gallery) {
		this.gallery = gallery;
	}

	
	protected String getBoundingBox(FDCameraData fd) {
		String result = null;
		if(fd.mHasNewFd) {
			StringBuilder sb = new StringBuilder("[");
			for(FaceDefine f : fd.mFaceItem) {
				if( (f.bottom - f.top) >0  && f.right - f.left >0) {
					sb.append("[").append((int)f.top).append(",").append((int)f.left).append(",").append((int)f.bottom).append(",").append((int)f.right).append("],");
				}
			}
			sb.setLength(sb.length() -1);
			sb.append("]");
			result = sb.toString();
		}
		return result;
	}
	
	
	public NTechIdentifyResponse analyze(FDCameraData fd) {

		NTechIdentifyResponse result = null;
		try {
			String rsp = null;
//			if(fd.mFaceNum == 1) {
//				//single face
//				rsp = callNTech(fd.mJpgData , (int)fd.mFaceItem[0].left ,(int)fd.mFaceItem[0].top , (int)fd.mFaceItem[0].right , (int)fd.mFaceItem[0].bottom);
//			}else if(fd.mFaceNum >= 2) {
//				rsp = callNTech(fd.mJpgData , getBoundingBox(fd));
//			}else {
//				//multiple faces, need to test performance compare FD+FR with multi-FR 
//				rsp = callNTech(fd.mJpgData);
//			}
            FaceDefine faceDefine = fd.mFaceItem[0];
            if (faceDefine == null) {
                rsp = callNTech(fd.mJpgData);
            } else {
                rsp = callNTech(fd.mJpgData, faceDefine.left, faceDefine.top, faceDefine.right, faceDefine.bottom);
            }
            if (rsp == null || rsp.indexOf("NO_FACES") != -1) {
                return null;
            }
//			if(rsp.indexOf("face")==-1&&rsp.indexOf("confidence")==-1){
//				return new NTechIdentifyResponse();
//			}
            result = gson.fromJson(rsp, NTechIdentifyResponse.class);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public FaceDefine[] imageDetect(FDCameraData fdCameraData) {
        logger.debug("===========detect============");
        byte[] image = fdCameraData.mJpgData;
        HttpResponse response = null;
        int i = 0;
        FaceDefine[] faceDefines = null;
        String url = "http://" + getHost() + ":8000/v0/detect/";
        try {
            response = Request.Post(url)
                    .connectTimeout(10000)
                    .socketTimeout(30000)
                    .addHeader("Authorization", "Token " + ntechToken)
                    .body(MultipartEntityBuilder
                            .create()
                            //.addTextBody("mf_selector", "all")
                            .addBinaryBody("photo", image, ContentType.create("image/jpeg"), "photo.jpg")
                            .build())
                    .execute().returnResponse();
            String reply = EntityUtils.toString(response.getEntity());
            logger.debug(reply);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200) {
                logger.info("请求未正确响应：" + responseCode);
                logger.info(reply);
                return null;
            }
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reply);
            if (jsonObject.containsKey("faces")) {
                JSONArray faceArray = (JSONArray) jsonObject.get("faces");
                int faceNum = faceArray.size();
                if (faceArray.size() == 0) {
//                    logger.error("图片中没有人脸！");
                    return null;
                }
                faceDefines = new FaceDefine[faceNum];
                Iterator iterator1 = faceArray.iterator();
                while (iterator1.hasNext()) {
                    JSONObject face = (JSONObject) (iterator1.next());
                    FaceDefine faceDefine = new FaceDefine();

                    faceDefine.left = (double) (long) face.get("x1");
                    faceDefine.right = (double) (long) face.get("x2");
                    faceDefine.top = (double) (long) face.get("y1");
                    faceDefine.bottom = (double) (long) face.get("y2");
                    faceDefines[i] = faceDefine;
                    i++;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        return faceDefines;
    }

    protected String callNTech(byte[] imageBytes, String bbox) throws ClientProtocolException, IOException {
        String url = "http://" + getHost() + ":8000/v1/faces/gallery/" + getGallery() + "/identify/";
        HttpResponse response = Request.Post(url)
                .connectTimeout(10000)
                .socketTimeout(30000)
                .addHeader("Authorization", "Token " + ntechToken)//Token 4BBj-6pjv
                .body(MultipartEntityBuilder
                        .create()
                        .addTextBody("mf_selector", "all")
                        .addTextBody("n", "1")
                        .addTextBody("bbox", bbox)
//                        .addTextBody("throshold", Constant.IDENTIFY_THRESHOLD)
                        .addBinaryBody("photo", imageBytes, ContentType.create("image/jpeg"), "photo.jpg")
                        .build())
                .execute().returnResponse();
        return EntityUtils.toString(response.getEntity());
    }


    protected String callNTech(byte[] imageBytes, double x1, double y1, double x2, double y2) throws ClientProtocolException, IOException {
        String url = "http://" + getHost() + ":8000/v0/faces/gallery/" + getGallery() + "/identify/";
        HttpResponse response = Request.Post(url)
                .connectTimeout(10000)
                .socketTimeout(30000)
                .addHeader("Authorization", "Token " + ntechToken)//Token 4BBj-6pjv
                .body(MultipartEntityBuilder
                        .create()
                        //.addTextBody("mf_selector", "all")
                        .addTextBody("n", "1")
                        .addTextBody("bbox", String.format("[[%s,%s,%s,%s]]", x1, y1, x2, y2))
                        .addBinaryBody("photo", imageBytes, ContentType.create("image/jpeg"), "photo.jpg")
                        .build())
                .execute().returnResponse();
        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity());
        }
        return null;
    }

    protected String callNTech(byte[] imageBytes) throws ClientProtocolException, IOException {
        String url = "http://" + getHost() + ":8000/v1/faces/gallery/" + getGallery() + "/identify/";
        HttpResponse response = Request.Post(url)
                .connectTimeout(10000)
                .socketTimeout(30000)
                .addHeader("Authorization", "Token " + ntechToken)
                .body(MultipartEntityBuilder
                        .create()
                        .addTextBody("mf_selector", "all")
                        .addTextBody("n", "1")
                        .addBinaryBody("photo", imageBytes, ContentType.create("image/jpeg"), "photo.jpg")
                        .build())
                .execute().returnResponse();
        if(response.getStatusLine().getStatusCode()==200){
//        	DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(new File("/home/n-tech-admin/pictures"+System.currentTimeMillis())));
//        	dataOutputStream.write(imageBytes);
//        	dataOutputStream.close();
        	return EntityUtils.toString(response.getEntity());
        }
        return null;

    }

    protected void drawBoxesOnImage(int x, int y, int w, int h, String name, BufferedImage bgImage)
            throws IOException {
        Graphics graph = bgImage.getGraphics();
        graph.setColor(Color.RED);

        graph.drawRect(x, y, w, h);

        graph.drawString(name, x, y - 10);

        graph.dispose();
    }

    protected BufferedImage loadImage(byte[] jpg) {
        BufferedImage jpgImage = null;
        try {
            jpgImage = ImageIO.read(new ByteArrayInputStream(jpg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jpgImage;
    }

    protected byte[] convertBufferedImage2Byte(BufferedImage jpgImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(jpgImage, "jpeg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    protected byte[] loadImageFile(String file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(file));
            BufferedImage jpgImage = ImageIO.read(fileInputStream);
            ImageIO.write(jpgImage, "jpeg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fileInputStream!=null){
                    fileInputStream.close();
                }
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }


    //	public static void main(String[] args) {
//		FaceDefine f1 = new FaceDefine();
//		f1.left =1;
//		f1.top = 1;
//		f1.bottom = 1;
//		f1.right = 1;
//		
//		FaceDefine f2 = new FaceDefine();
//		f2.left =2;
//		f2.top = 2;
//		f2.bottom = 2;
//		f2.right = 2;
//
//		FaceDefine[] fds = {f1,f2};
//		StringBuilder sb = new StringBuilder("[");
//		for(FaceDefine f : fds) {
//			if(f.top >0 && f.left > 0 && f.bottom > 0 && f.right >0) {
//				sb.append("[").append((int)f.top).append(",").append((int)f.left).append(",").append((int)f.bottom).append(",").append((int)f.right).append("],");
//			}
//		}
//		sb.setLength(sb.length() -1);
//		sb.append("]");
//		System.out.println(sb);
//	}
//	
	public void detect(byte[] image) {
//		POST /v1/detect/
		try {
			String url = "http://"+getHost()+":8000/v1/detect/";
	        HttpResponse response = Request.Post(url)
	                .connectTimeout(10000)
	                .socketTimeout(30000)
	                .addHeader("Authorization", "Token " + ntechToken)//Token 4BBj-6pjv
	                .body(MultipartEntityBuilder
	                        .create()
	                        .addTextBody("emotions", "true")
	                        .addTextBody("gender", "true")
	                        .addTextBody("age", "true")
	                        .addBinaryBody("photo", image, ContentType.create("image/jpeg"), "photo.jpg")
	                        .build())
	                .execute().returnResponse();
	        String text = EntityUtils.toString(response.getEntity());
	        DetectedFace df = gson.fromJson(text, DetectedFace.class);
	        for(NTechFace f : df.getResults()) {
	        	System.out.println(f.getAge());
	        }
		}catch(ClientProtocolException e) {
			logger.error(e.getMessage() , e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage() , e);
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println(String.format("[%s,%s,%s,%s]", 437, 227, 510, 299));
		String prjFolder = System.getProperty("user.dir");
//		List<FaceBox> fbs = new ArrayList<>();
		NTechFRService service = new NTechFRService();
		service.setHost("192.168.0.8");
		service.setGallery("com/quadrant");
		byte[] img = service.loadImageFile(prjFolder +"/src/main/java/com/quadrant/fr/f5-1.jpg");
//		byte[] b64 = Base64.encodeBase64(img);
//		try {
//			System.out.println(new String(b64 , "utf-8"));
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		if(true) return;
//		
		
//		try {
			long start = System.currentTimeMillis();//437, 227, 510, 299
			String bbox = "[[192, 189, 273, 270],[296, 231, 362, 296]]";
			service.detect(img);
//			String resp = service.callNTech(img);
//			
//			System.out.println("FR cost = " + (System.currentTimeMillis() - start));
//			System.out.println(resp);
//			return;
//			NTechIdentifyResponse res = service.gson.fromJson(resp, NTechIdentifyResponse.class);
//			Map<String,List<NTechFaceMatch>> results = res.getResults();
//			for (String key : results.keySet()) {
//				String faceBox = key.substring(1, key.length() - 1);
//				String[] faceBoxSplit = faceBox.split(",");
//				int x1 = Integer.parseInt(faceBoxSplit[0].trim());
//				int y1 = Integer.parseInt(faceBoxSplit[1].trim());
//				int x2 = Integer.parseInt(faceBoxSplit[2].trim());
//				int y2 = Integer.parseInt(faceBoxSplit[3].trim());
//				int w = x2 - x1;
//				int h = y2 - y1;
//
//				if (w < 0)
//					throw new RuntimeException("NTech returned face with w<0; x1,y1,x2,y2 = " + faceBox);
//				if (h < 0)
//					throw new RuntimeException("NTech returned face with h<0; x1,y1,x2,y2 = " + faceBox);
//				List<NTechFaceMatch> list = results.get(key);
//
//				NTechFaceMatch bestMatch = null;
//
//				if (list.size() > 0) {
//					for (NTechFaceMatch m : list) {
//						if (bestMatch == null || m.getConfidence() > bestMatch.getConfidence()) {
//							bestMatch = m;
//						}
//					}
//				}
//				System.out.println(faceBox + "--" +bestMatch.getConfidence());
//				if(bestMatch!=null && bestMatch.getConfidence()>0.7 && !Strings.isNullOrEmpty(bestMatch.getFace().getMeta()))//found 
//	            {
//					FaceBox fb = new FaceBox(x1 , y1,w, h, bestMatch.getFace().getMeta());
////					FaceBox fb = new FaceBox(bestMatch.getFace().getLeft() , bestMatch.getFace().getTop(),bestMatch.getFace().getWidth(), bestMatch.getFace().getHeight(), bestMatch.getFace().getMeta());
//					fbs.add(fb);
//	            }
//				else if(bestMatch != null){//low confidence
//					FaceBox fb = new FaceBox(x1 , y1,w, h, bestMatch.getFace().getMeta());
//					fbs.add(fb);
//					
//	            }
//	            else {//unknown
//	            		FaceBox fb = new FaceBox(x1, y1, w, h, "Unknown");
//					fbs.add(fb);
//	            }
//				
//				
//				
//			}
//			System.out.println(fbs.size());
//			BufferedImage bi = service.loadImage(img);
//			for(FaceBox fb : fbs) {
//				service.drawBoxesOnImage(fb.getX(), fb.getY(),fb.getW(), fb.getH(),  fb.getMeta(), bi);
//			}
//			
//			ImageIO.write(bi, "jpg", new File(prjFolder +"/src/main/java/com/quadrant/fr/f5-1-x.jpg"));
			
			
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
}
