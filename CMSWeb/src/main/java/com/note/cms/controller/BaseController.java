package com.note.cms.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.note.common.utils.HTextUtils;
import com.note.cms.service.UserService;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.note.common.utils.DateUtils;
import com.note.common.utils.HException;

/**
 * @des: controller 基类
 * @author Administrator
 *
 */


@Controller
public class BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	/**请求json*/
	public String requestJson ;
	/**手机sessionId*/
//	public HSession mSession ;
	/**返回json*/
	public String responseJson = "";
	/**返回错误消息*/
	
	public List<String> errorMessageList = new ArrayList<String>();
	private String mSessionId;
//
	@Autowired
	private UserService mUserService;
//
	
	/**
	 * @ｄｅｓ：　获取ｓｅｓｓｉｏｎＩＤ
	 * @author xuxinjian
	 * @param request
	 * @return
	 */
	public String getSessionID(HttpServletRequest request){
		if(HTextUtils.isEmpty(mSessionId)){
			mSessionId = request.getHeader("sessionID");
		}
		return mSessionId;
	}


	/**
	 *
	 * @param request
	 * @param needSessionID 设备访问， 登录接口不需要session，其他如果没有，则返回错误
	 * @throws Exception
	 */
	public void requestProcess (HttpServletRequest request, boolean needSessionID) throws Exception {
		
		requestJson= "";
		String sessionId= "";
		responseJson= "";
		errorMessageList.clear();
		
		requestJson = this.readJson(request);
		logger.info("requestJson--->>"+requestJson);
		sessionId = request.getHeader("sessionID");
		logger.info(request.getRequestURL() + ":sessionID:" + sessionId);
		
		//判定Json是否为空
		if(HTextUtils.isEmpty(requestJson)){
			if(errorMessageList.size()==0)
				errorMessageList.add("操作失败，请重试!");
			logger.info("requestJson is null");
		}
		
		//处理session
		if(needSessionID){
			//不是配置登陆， 检查sessionID
			if(sessionId==null || sessionId.length()==0){
				if(errorMessageList.size()==0)
					errorMessageList.add("无效的SessionID，请重试!");
				 logger.info("sessionID is null");
			}else{
//				//检查sessionID的合法性
//				mSession = mHSessionDao.getHSessionBySessionID(sessionId);
//				if(mSession == null){
//					if(errorMessageList.size()==0){
//						errorMessageList.add("无效的SessionID，请重试!");
//						logger.info("sessionID is invaild");
//					}
//				}
				
			}
		}else{
			//配置登陆， 不检查sessionID
		}
	}
	
	  /**
     * 以UTF-8编码读取网络请求的内容
     * @param request
     * @return
     * @throws IOException
     */
    public String readJson(HttpServletRequest request) throws Exception {
    	try {
    		BufferedReader reader = request.getReader();
    		String line = null;
    		StringBuffer jsonIn = new StringBuffer();
    		while((line=reader.readLine()) != null) {
    		jsonIn.append(line);
    		}
    		String result=jsonIn.toString();
    		if(result.equals(""))
    		{
    		result=request.getParameter("key");
    		}
    		logger.info("request body: "+ request.getRequestURL() + "====" + result);
			//logger.info(LMException.PREFIXINFOMESSAGE + "Request Body = " + json);
			return result;
		} catch (IOException e) {
			throw new HException(e.getMessage());
		}
    }
    
    protected void setResponse(HttpServletResponse response, Object outObj){
        response.setContentType("text/html;charset=utf-8");  
        response.setHeader("sessionID", "sessionID1");
        try {
        	JsonGenerator jsonGenerator = null;
        	ObjectMapper objectMapper = null;
            logger.info("jsonGenerator");
            objectMapper = new ObjectMapper();
            jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
            
            jsonGenerator.writeObject(outObj);    
            
            logger.info("ObjectMapper");
            objectMapper.writeValue(response.getWriter(), outObj);
            response.flushBuffer();  
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }
    
    /**
     * 将错误码和返回的内容封装进response中并返回
     * @param response
     * @param responseStr
     */
    public void respond(HttpServletResponse response,String responseStr) {
 		OutputStream os = null;
 		try {
 			os = response.getOutputStream();
 			response.setHeader("Cache-Control", "no-cache");
 			response.setContentType("application/json");
 			response.setCharacterEncoding("utf8");
 			String errorMsg = "";
 			for (String errorMessage : errorMessageList) {
 				errorMsg += errorMessage;
 			}
 			if(errorMsg==null || errorMsg.length()==0){
 				response.setHeader("errorCode", "0"); 
 				response.setHeader("errorMessage", ""); 
 			}else{
 				String retMessage = Base64.encodeBase64String(errorMsg.getBytes("utf8"));
 				response.setHeader("errorCode", "-1"); 
 				response.setHeader("errorMessage", retMessage); 
 				logger.info("----------------error-start---------------");
 				logger.info("requestJson-->>"+requestJson);
 				logger.info("errorMsg-->>"+errorMsg);
 				logger.info(DateUtils.convertDateToStringForChina(new Date()));
 				logger.info("----------------error-end---------------");
 				//responseStr = "";
 			}
 			//responseStr = responseStr.replaceAll("\\\\", "");
 			logger.info("responseStr:  " + responseStr);
			byte[] data = responseStr.getBytes("utf8");
 			response.setContentLength(data.length);

 			os.write(data);
 		} catch (IOException e) {
 			e.printStackTrace();
 		} finally {
 			IOUtils.closeQuietly(os);
 		}
 	}
 	
     public void respondException(HttpServletResponse response,String responseStr) {
    	 logger.info("respondException:" + responseStr);
     	errorMessageList.add("操作失败或网络异常");
     	this.respond(response, responseStr);
     }
     
     public void respondException(HttpServletResponse response,String responseStr, String errMessage) {
    	logger.info("respondException:" + errMessage);
      	errorMessageList.add(errMessage);
      	this.respond(response, responseStr);
      }
    
	/**
	 * 将notification统一包装起来, 然后输出
	 * @param responseDataMap 协议的消息体部分， 对应 responseData，不能为null
	 * @param notificationMap 协议的消息部分， 对应 notification， 可以为null
	 */
	public void outputRespBody(HttpServletRequest request, HttpServletResponse response, Map<String, Object> responseDataMap, Map<String, Object> notificationMap){
//		if(notificationMap == null){
//			notificationMap = new HashMap<String, Object>();
//			notificationMap.put("notifCode", "");
//			notificationMap.put("notifInfo", "");
//		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("responseData", responseDataMap);
		//model.put("notification", notificationMap);

		long startTime = (Long)request.getAttribute("startTime");
		long endTime = System.currentTimeMillis();
		long executeTime = endTime - startTime;
		model.put("cost", executeTime + "ms");

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		responseJson = gson.toJson(model);
		
		this.respond(response, responseJson);
	}

	public void outputWebRespBody(HttpServletResponse response, Map<String, Object> responseDataMap){

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		responseJson = gson.toJson(responseDataMap);

		this.respond(response, responseJson);
	}

	/**
	 * 返回给设备请求过来的方法
	 * @param response
	 * @param responseStr
	 */
	public void respondDeviceSimpleProtocol(HttpServletResponse response,String responseStr) {
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("application/json");
			response.setCharacterEncoding("utf8");

			logger.info("responseStr:  " + responseStr);
			byte[] data = responseStr.getBytes("utf8");
			response.setContentLength(data.length);

			os.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
		
}
