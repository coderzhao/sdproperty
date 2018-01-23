package com.quadrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 
 * @author bean
 *@deprecated
 */
public class CameraClientThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(CameraClientThread.class);
	private final int FD_HEADER_LENGTH = 176;
	private Socket mSocket;
	private FDCameraListener mListener = null;
	private String clientIP = null;
	
	public CameraClientThread(Socket s, FDCameraListener listener) {
		mSocket = s;
		mListener = listener;
		this.clientIP = ((InetSocketAddress)s.getRemoteSocketAddress()).getAddress().getHostAddress();
	}

	/**
	 * If clientIP is not in table TbIpc , we may need to close socket to avoid attack
	 */
	public void closeSocket() {
		if(mSocket != null) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int byte2int(byte[] b) {
		int[] aTemplate = new int[4];
		aTemplate[0] = 0x000000FF;
		aTemplate[1] = 0x0000FF00;
		aTemplate[2] = 0x00FF0000;
		aTemplate[3] = 0xFF000000;

		int a = 0;
		if (b != null && b.length <= 4) {
			for (int i = 0; i < b.length; i++) {
				a = a + ((b[i] << i * 8) & aTemplate[i]);
			}
		}

		return a;
	}

	private void isSocketAlive(OutputStream out) throws IOException {
		byte[] d = {0,1};
		try {
			out.write(d);
		}catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	public String byte2hex(byte bytex)
    {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[2];
        int v;
        
        v = bytex & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
            
        return new String(hexChars);
    }
	 public String bytes2String(byte[] bytes)
	    {
	        String s = new String();
	        boolean showingHex = false;
	        for ( int j = 0; j < bytes.length; j++ )
	        {
	            if(bytes[j]>126 || bytes[j]<32 || (bytes[j]==0x77 && showingHex) || (j-1>0 && bytes[j-1]==0x77 && showingHex))
	            {
	                if(!showingHex)
	                {
	                    s+="[";
	                    showingHex = true;
	                }
	                s += byte2hex(bytes[j]);
	            }
	            else
	            {
	                if(showingHex)
	                {
	                    s+="]";
	                    showingHex = false;
	                }
	                s += (char)bytes[j];
	            }
	        }
	        return s;
	    }
	
	public void run() {
		
		try {
			InputStream is = mSocket.getInputStream();
			OutputStream out = mSocket.getOutputStream();
			FDCameraData fdData = new FDCameraData();

			byte[] magicStr = new byte[4];
			byte[] totalLen = new byte[4];
			byte[] mac = new byte[18];
			byte[] reserved = new byte[6];
			byte[] faceNum = new byte[4];
			byte[] timeStamp = new byte[8];
			byte[] newFdFlag = new byte[4];
			byte[] dummy = new byte[4];
			byte[] data = new byte[2];

			while (true) {
				
				if (is.available() < FD_HEADER_LENGTH) {
					try {
						isSocketAlive(out);
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				if (is.read(magicStr) != magicStr.length) {
					break;
				}

				if (is.read(totalLen) != totalLen.length) {
					break;
				}
				fdData.mJpgSize = byte2int(totalLen) - (FD_HEADER_LENGTH - 4);

				if (is.read(mac) != mac.length) {
					break;
				}
				
				fdData.mStrMac = bytes2String(mac).substring(0, 17).toUpperCase();
				
//				for (int i = 0; i < mac.length -1; i++) {
//					char hex = (char) mac[i];
//					System.out.println("mac[i]=" + hex);
//					fdData.mStrMac += hex;
//				}
				

				if (is.read(reserved) != reserved.length) {
					break;
				}

				if (is.read(faceNum) != faceNum.length) {
					break;
				}
				fdData.mFaceNum = byte2int(faceNum);

				if (is.read(dummy) != dummy.length) {
					break;
				}

				if (is.read(timeStamp) != timeStamp.length) {
					break;
				}

				if (is.read(newFdFlag) != newFdFlag.length) {
					break;
				}
				if (fdData.mFaceNum <= 0)
					fdData.mHasNewFd = false;
				else
					fdData.mHasNewFd = true;

				boolean bFlag = true;
				for (int i = 0; i < FDCameraData.MAX_FACE_ITEM_PER_FRAME; i++) {
					if (is.read(data) != data.length) {
						bFlag = false;
						break;
					}
					fdData.mFaceItem[i].confidence = byte2int(data);

					if (is.read(data) != data.length) {
						bFlag = false;
						break;
					}
					fdData.mFaceItem[i].ID = byte2int(data);

					if (is.read(data) != data.length) {
						bFlag = false;
						break;
					}
					fdData.mFaceItem[i].left = byte2int(data) / 640.0;

					if (is.read(data) != data.length) {
						bFlag = false;
						break;
					}
					fdData.mFaceItem[i].right = byte2int(data) / 640.0;

					if (is.read(data) != data.length) {
						bFlag = false;
						break;
					}
					fdData.mFaceItem[i].top = byte2int(data) / 360.0;

					if (is.read(data) != data.length) {
						bFlag = false;
						break;
					}
					fdData.mFaceItem[i].bottom = byte2int(data) / 360.0;
				}

				if (bFlag == false)
					break;

				if (is.read(dummy) != dummy.length) {
					break;
				}

				while (is.available() < fdData.mJpgSize) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					continue;
				}

				fdData.mJpgData = new byte[fdData.mJpgSize];
				if (is.read(fdData.mJpgData) != fdData.mJpgSize) {
					break;
				}

				if (mListener != null) {
					mListener.OnCameraData(clientIP , fdData);
				}
				logger.info(fdData.toString());
				// BufferedOutputStream fw = new BufferedOutputStream(
				// new FileOutputStream("D:/fd.jpg"));
				// fw.write(jpgData);
				// fw.flush();
				// fw.close();

//				for (int j = 0; j < fdData.mFaceNum; j++) {
//					System.out.println(j + ": left:" + fdData.mFaceItem[j].left
//							* 1280 + ", right:" + fdData.mFaceItem[j].right
//							* 1280 + ", top:" + fdData.mFaceItem[j].top * 720
//							+ ", bottom:" + fdData.mFaceItem[j].bottom * 720);
//				}
			}

			
		} catch (IOException e) {
			logger.info("Camera {} socket exception", clientIP);
			logger.error(e.getMessage());
		} finally {
			logger.info("Camera {} socket closed", clientIP);
			try {
				mSocket.close();
			} catch (IOException e) {
			}finally {
				mListener.onSocketSessionClose(clientIP);
			}
		}
		logger.info("{} CameraClientThread stop",clientIP);
	}
}
