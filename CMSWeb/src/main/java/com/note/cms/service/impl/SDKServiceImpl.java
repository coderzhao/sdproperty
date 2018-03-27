package com.note.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.note.cms.common.Constant;
import com.note.cms.data.model.KConst;
import com.note.cms.data.model.TbIpc;
import com.note.cms.data.model.TbSys;
import com.note.cms.data.sdk.MSdkCamera;
import com.note.cms.service.SDKService;
import com.note.cms.service.SettingService;
import com.note.cms.tools.ZtHttpUtil;
import com.note.cms.tools.ZtHttpUtilResult;
import com.note.common.utils.HTextUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxinjian on 17/7/21.
 */
@Service("SDKService")
public class SDKServiceImpl implements SDKService{
    private static final Logger logger = LoggerFactory.getLogger(SDKServiceImpl.class);
    @Autowired
    private SettingService mSettingService;

    /*
    添加人脸特征（face）
    Uri：/v0/face POST
    */
    public Map<String, Object> addFace(String image) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            logger.info("addFace:" + image);
            String urlRoot = getSDKRootUrl();
            if (HTextUtils.isEmpty(urlRoot)) {
                return null;
            }
//            String url = urlRoot + "/v0/face";
            String url = "http://"+Constant.SDK_IP + ":8000/v0/face";

            JSONObject obj = new JSONObject();
            obj.put("photo", image);

            HttpResponse response = Request.Post(url)
                    .connectTimeout(10000)
                    .socketTimeout(30000)
                    .addHeader("Authorization", "Token " + Constant.TOKEN)//Token 4BBj-6pjv
                    .body(MultipartEntityBuilder
                            .create()
//                            .addTextBody("mf_selector", "all")
                            .addTextBody("photo", image)
                            .build())
                    .execute().returnResponse();
            String result= EntityUtils.toString(response.getEntity());
            logger.info(result);
//            ZtHttpUtilResult result = ZtHttpUtil.sendHttpRequest(url, obj.toJSONString(), HttpMethod.POST);
            if (result != null) {
//                logger.info(result.content);
                if (response.getStatusLine().getStatusCode() == 200) {
                    JSONObject outObj = JSON.parseObject(result);
                    JSONArray array = outObj.getJSONArray("results");
                    JSONObject jsonObj = array.getJSONObject(0);
                    String image1 = jsonObj.getString("normalized");
                    String code = jsonObj.getString("person_id");
                    map.put("image", image1);
                    map.put("code", code);
                    logger.info("image=" + image + "  code=" + code);
                }
            }
//            if (result != null) {
//                logger.info(result.content);
//                if (result.code == KConst.RESULT_OK) {
//                    JSONObject outObj = JSON.parseObject(result.content);
//                    JSONArray array = outObj.getJSONArray("results");
//                    JSONObject jsonObj = array.getJSONObject(0);
//                    String image1 = jsonObj.getString("normalized");
//                    String code = jsonObj.getString("person_id");
//                    map.put("image", image1);
//                    map.put("code", code);
//                    logger.info("image=" + image + "  code=" + code);
//                }
//            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return map;
    }

    @Override
    public String addCamera(TbIpc input) {
        try {
            String urlRoot = getSDKRootUrl();
            if (HTextUtils.isEmpty(urlRoot)) {
                return null;
            }

            //删除掉所有已存在的地址的，还要删除掉已存在的cameraid的
            deleteByAddress(input.getAddress());
            if(!HTextUtils.isEmpty(input.getCameraId())){
                deleteByCameraId(input.getCameraId());
            }

            String url = urlRoot + "/v0/camera";

            JSONObject obj = new JSONObject();
            obj.put("meta", input.getName());
            obj.put("url", input.getAddress());
            obj.put("detector", "detect1");
            ZtHttpUtilResult result = ZtHttpUtil.sendHttpRequest(url, obj.toJSONString(), HttpMethod.POST);
            if (result != null) {
                if (result.code == KConst.RESULT_OK) {
                    JSONObject outObj = JSON.parseObject(result.content);
                    String camera_id = outObj.getString("id");
                    return camera_id;
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteCamera(TbIpc input) {
        if(HTextUtils.isEmpty(input.getCameraId())) {
            deleteByAddress(input.getAddress());
        }else{
            deleteByCameraId(input.getCameraId());
        }
        return true;
    }

    private String getSDKRootUrl(){
        TbSys item = mSettingService.getByKeyName(SettingService.KEY_SDK_API_URL_ROOT);
        if (item == null) {
            return null;
        }
        String urlRoot = item.getValue();
        return urlRoot;
    }

    /**
     * 获取sdk里所有指定地址的摄像头的列表，如果为null，则返回所有的摄像头列表
     * @param address
     * @return
     */
    private List<MSdkCamera> getListCamera(String address){
        try {
            String urlRoot = getSDKRootUrl();
            if (HTextUtils.isEmpty(urlRoot)) {
                return null;
            }
            String url = urlRoot + "/v0/camera";

            ZtHttpUtilResult result = ZtHttpUtil.sendHttpRequest(url, "", HttpMethod.GET);
            if (result != null) {
                if (result.code == KConst.RESULT_OK) {
                    List<MSdkCamera> listData = JSON.parseArray(result.content, MSdkCamera.class);
                    if(HTextUtils.isEmpty(address)) {
                        return listData;
                    }else{
                        if(listData != null && listData.size() > 0){
                            //过滤掉非address
                            for(int i = listData.size() -1; i >= 0; i--){
                                MSdkCamera item = listData.get(i);
                                if(!item.getUrl().equalsIgnoreCase(address)){
                                    listData.remove(i);
                                }
                            }
                            return listData;
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 删除指定地址的所有摄像头
     * @param address
     */
    private void deleteByAddress(String address){
        if(HTextUtils.isEmpty(address)){
            return ;
        }
        List<MSdkCamera> listData = getListCamera(address);
        if(listData != null && listData.size() > 0){
            for(MSdkCamera item : listData) {
                deleteByCameraId(item.getId());
            }
        }
    }

    /**
     * 删除掉sdk中指定cameraid的摄像头
     * @param cameraId
     */
    private void deleteByCameraId(String cameraId){
        try {
            String urlRoot = getSDKRootUrl();
            if (HTextUtils.isEmpty(urlRoot)) {
                return;
            }
            String url = urlRoot + "/v0/camera/" + cameraId;

            ZtHttpUtilResult result = ZtHttpUtil.sendHttpRequest(url, "", HttpMethod.DELETE);
            logger.info("result:" + result.code + "|content=" + result.content);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return;
    }

    /**
     * 更新采集服务程序的设置参数
     * @param keyname
     * @param value
     * @return
     */
    @Override
    public boolean updateSet(String keyname, String value){
        try {
            String urlRoot = getSDKRootUrl();
            if (HTextUtils.isEmpty(urlRoot)) {
                return false;
            }
            String url = urlRoot + "/sysconfig";
            JSONObject obj = new JSONObject();
            obj.put(keyname, value);

            ZtHttpUtilResult result = ZtHttpUtil.sendHttpRequest(url, obj.toJSONString(), HttpMethod.PUT);
            logger.info("result:" + result.code + "|content=" + result.content);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     *
     * @param ip
     * @param port
     * @param line 第几录
     * @param time 持续几秒
     */
    public void flashOpenDoor(String ip, Integer port, Integer line, Integer time){
        logger.info("flashOpenDoor1：");
        Runnable r = new OpenDoorThread(ip, port, line, time);
        Thread t = new Thread(r);
        t.start();
        logger.info("flashOpenDoor2：" );
    }

    public  synchronized  void flashOpenDoorThreadFun(String ip, Integer port, Integer line, Integer time){
        try {
            logger.info("flashOpenDoorThreadFun 2：" );
            String urlRoot = getSDKRootUrl();
            if (HTextUtils.isEmpty(urlRoot)) {
                logger.warn("flashOpenDoorThreadFun 33：" );
                return ;
            }
//            String url = urlRoot + "/switch/on_off_ex";
//            String url =  Constant.SWITCH_IP_PORT;
//            JSONObject obj = new JSONObject();
//            obj.put("ip", ip);
//            obj.put("port", port);
//            obj.put("line", line);
//            obj.put("on_off", 1);
//            obj.put("time", time);

//					String url = "http://192.168.10.208:8888/";

            logger.info("flashOpenDoorThreadFun: " + ip + " port:" + port + " line:" + line);
            HttpResponse response = Request.Post(Constant.SWITCH_IP_PORT)
                    .connectTimeout(10000)
                    .socketTimeout(30000)
//							.addHeader("Authorization", "Token " + ntechToken)
                    .body(MultipartEntityBuilder
                            .create()
                            .addTextBody("ip", ip)
                            .addTextBody("port", String.valueOf(port))
                            .addTextBody("line", String.valueOf(line))
                            .addTextBody("on_off", "1")
                            .addTextBody("time", String.valueOf(time))
                            .build())
                    .execute().returnResponse();


//            ZtHttpUtilResult result = ZtHttpUtil.sendHttpRequest(url, obj.toJSONString(), HttpMethod.POST);
            logger.info("flashOpenDoorThreadFun result:" + response.getStatusLine().getStatusCode()+ "|content=" + response.getEntity().toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ;
        }
    }

    private class OpenDoorThread implements Runnable {
        public String ip;
        public Integer port;
        public Integer line;
        public Integer time;

        public OpenDoorThread(String ip, Integer port, Integer line, Integer time){
            this.ip = ip;
            this.port = port;
            this.line = line;
            this.time = time;
        }

        @Override
        public void run() {
            logger.info("OpenDoorThread 11：" + Thread.currentThread().getName());
            flashOpenDoorThreadFun(ip, port, line, time);
            logger.info("OpenDoorThread 22：" + Thread.currentThread().getName());
        }
    }
}
