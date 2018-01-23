package com.note.cms.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.note.cms.NConst;
import com.note.cms.data.model.TbSnapshot;
import com.note.cms.data.model.TbSys;
import com.note.cms.data.vo.InputSnapshotFaceVo;
import com.note.cms.data.vo.InputSnapshotVo;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.data.vo.OutputUserVo;
import com.note.cms.service.SettingService;
import com.note.cms.service.SnapshotService;
import com.note.cms.service.UserService;
import com.note.common.utils.HException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * @author xuxinjian
 * @des: api for app
 * 2017
 */
@Controller
@RequestMapping("/api")
	
public class AppApiController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(AppApiController.class);
	@Autowired
	private UserService mUserService;

	@Autowired
	private SnapshotService mSnapshotService;
	@Autowired
	private SettingService mSettingService;



	/*
	 * 手机端登录
	 */
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public void login(String mobile, String password, HttpServletRequest request,	HttpServletResponse response){
		try{
			//获取请求消息
			logger.info("loginConfiguration begin");
//			this.requestProcess(request, false);
			Map<String, Object> responseDataMap = new HashMap<String, Object>();
			
//			if(errorMessageList.size()==0)
			{
//				JSONObject j = JSONObject.parseObject(requestJson);

				OutputUserVo output = mUserService.doLoginApp(mobile, password);
				if(output != null){
					responseDataMap.put("data", output);
					super.outputRespBody(request, response, responseDataMap, null);
				}else{
					super.respondException(response, responseJson, "用户名或者密码不正确");
				}
			}
//			else{
//				//参数错误
//				super.respondException(response, responseJson, "参数错误");
//			}

			logger.info("loginConfiguration end");

			
		}catch (HException e) {
			e.printStackTrace();
			super.respondException(response, responseJson, e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			super.respondException(response, responseJson);
		}
	}

//	{
//		"results":{
//		"camera_id":"3d9c794e-cf9b-4e7f-9b6e-16c7d580250b",
//				"snapshot_photo":"/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg",
//				"snapshot_timestamp":"2017-07-13T06:41:09.213516",
//				"[455, 119, 551, 215]":[
//		{
//			"face":{
//			"x2":199,
//					"normalized":"http://127.0.0.1:3333/uploads//20170713/14999280429919252.jpeg",
//					"y1":-16,
//					"friend":false,
//					"timestamp":"2017-07-13T06:40:42.985000",
//					"thumbnail":"http://127.0.0.1:3333/uploads//20170713/1499928042989949.jpeg",
//					"age":20.982995986938477,
//					"galleries":[],
//			"meta":"",
//					"photo_hash":"3cbfcf2fafa58d043355476e0e3c77a4",
//					"y2":183,
//					"x1":0,
//					"person_id":20,
//					"id":3839815790104905,
//					"photo":"http://127.0.0.1:3333/uploads//20170713/14999280429854276.jpeg",
//					"gender":"male",
//					"emotions":[]
//		},
//			"confidence":0.831
//		}
//]
//	}
//	}

	/*
 	*抓拍
 	*/
	@RequestMapping(value = "sendSnapshot.do", method = RequestMethod.POST)
	@ResponseBody
	public void sendSnapshot(HttpServletRequest request,	HttpServletResponse response){
		try{
			//获取请求消息
			logger.info("sendSnapshot begin");
			this.requestProcess(request, false);
			Map<String, Object> responseDataMap = new HashMap<String, Object>();

			InputSnapshotVo input = new InputSnapshotVo();
			if(errorMessageList.size()==0){
				JSONObject j = JSONObject.parseObject(requestJson);
				JSONObject resultsObj = j.getJSONObject("results");
				String camera_id = resultsObj.getString("camera_id");
				String snapshot_photo = resultsObj.getString("snapshot_photo");
				String snapshot_timestamp = resultsObj.getString("snapshot_timestamp");
				InputSnapshotFaceVo faceVo = null;

				for (Map.Entry<String, Object> entry : resultsObj.entrySet()) {
					String strKey = entry.getKey();
					String head = strKey.substring(0, 1);
					if(head.equalsIgnoreCase("[")){
						JSONArray facesObj = resultsObj.getJSONArray(strKey);
						if(facesObj.size() > 0) {
							JSONObject faceObj = facesObj.getJSONObject(0);
							JSONObject ff = faceObj.getJSONObject("face");
							float confidence = faceObj.getFloat("confidence");
							faceVo = JSONObject.parseObject(ff.toString(), InputSnapshotFaceVo.class);
							faceVo.setConfidence(confidence);
						}
						break;
					}
//					System.out.println(entry.getKey() + ":" + entry.getValue());
				}

				input.setCamera_id(camera_id);
				input.setSnap_timestamp(snapshot_timestamp);
				input.setSnapshot_photo(snapshot_photo);
				input.setFace(faceVo);
				if(input != null) {
					Map<String, Object> lock = mSnapshotService.add(input);
					responseDataMap.put("lock", lock);
					responseDataMap.put("resultCode", 0);
				}else{
					responseDataMap.put("resultCode", 1);
				}

				super.outputRespBody(request, response, responseDataMap, null);
			}else{
				//参数错误
				super.respondException(response, responseJson, "参数错误");
				logger.info("sendSnapshot error");
			}

			logger.info("sendSnapshot end");


		}catch (Exception e) {
			logger.error("sendSnapshot crash:" + e.getMessage());
			Map<String, Object> responseDataMap = new HashMap<String, Object>();
			responseDataMap.put("resultCode", 1);
			responseDataMap.put("message", "sendSnapshot crash:" + e.getMessage());
			super.outputRespBody(request, response, responseDataMap, null);
//			super.respondException(response, responseJson, e.getMessage());
		}
	}

	/*
 	*获取最新的抓拍
 	* userid: 用户的id
 	* lastsnapid: 用户最新的snapid，如果为0，则自动返回最新的5条
 	* forward: 1 -> 获取更新的， 0->获取更旧的
 	* count: 数量
 	*/
	@RequestMapping(value = "getLastSnapshot.do", method = RequestMethod.POST)
	@ResponseBody
	public void getLastSnapshot(Integer ipcid, Integer userid, Integer lastsnapid, int forward, int count, HttpServletRequest request,	HttpServletResponse response){
		try{
			//获取请求消息
			logger.info("getLastSnapshot begin");
			Map<String, Object> responseDataMap = new HashMap<String, Object>();

			if(errorMessageList.size()==0){
				List<OutputSnapshotVo> output = mSnapshotService.getLastSnapshot(ipcid, lastsnapid, forward, count);
				responseDataMap.put("data", output);
				responseDataMap.put("resultCode", 0);

				super.outputRespBody(request,response, responseDataMap, null);
			}else{
				//参数错误
				super.respondException(response, responseJson, "参数错误");
				logger.info("getLastSnapshot error");
			}

		}catch (Exception e) {
			logger.error(e.getMessage());
			super.respondException(response, responseJson, e.getMessage());
		}
	}



	/*
 	获取系统设置参数
 	*/
	@RequestMapping(value = "getSetting.do", method = RequestMethod.POST)
	@ResponseBody
	public void getSetting(HttpServletRequest request,	HttpServletResponse response){
		try{
			//获取请求消息
			logger.info("getSetting begin");
			Map<String, Object> responseDataMap = new HashMap<String, Object>();

			List<TbSys> output = mSettingService.getListPage(1, 1000, null, null);
			responseDataMap.put("data", output);
			responseDataMap.put("resultCode", 0);

			super.outputRespBody(request,response, responseDataMap, null);

			logger.info("getSetting end");


		}catch (Exception e) {
			logger.error(e.getMessage());
			super.respondException(response, responseJson, e.getMessage());
		}
	}


}
