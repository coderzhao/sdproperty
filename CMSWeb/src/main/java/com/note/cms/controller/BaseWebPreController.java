package com.note.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.note.cms.data.model.TbInitSession;
import com.note.cms.data.model.TbUser;
import com.note.cms.data.vo.OutputUserVo;
import com.note.common.utils.*;
import com.note.cms.data.ErrorCode;
import com.note.cms.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @des: controller 基类
 * @author Administrator
 *
 */


@Controller
public class BaseWebPreController {
	private final static String md5key = ".note.0160830";
	//private final static String TokenName = "X-CSRFToken";
	private final static String TokenName = "csrftoken";

	private static final Logger logger = LoggerFactory.getLogger(BaseWebPreController.class);
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

	public void setmSessionId(HttpServletRequest request, String mSessionId) {
		this.mSessionId = mSessionId;
		//request.getSession().getAttribute(mSessionId);

	}

	public TbInitSession getTbInitSession(HttpServletRequest request){

		String id = getSessionID(request);
		if(HTextUtils.isEmpty(id)) {
			return null;
		}

		return mUserService.getSessionBySessionID(id);
	}

	public TbUser getTbUser(HttpServletRequest request) {
		try {
//			if (mTbUser != null) {
//				return mTbUser;
//			}
			OutputUserVo vo = (OutputUserVo)request.getSession().getAttribute("OutputUserVo");

			TbUser user = mUserService.getUserById(vo.getId());

			return user;
		} catch (Exception e) {
			return null;
		}
	}

	public OutputUserVo getOutputUserVo(HttpServletRequest request) {
		try {
			OutputUserVo vo = (OutputUserVo)(request.getSession().getAttribute("OutputUserVo"));
			return vo;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 检查是否是跨站攻击,No -安全
	 */

	private boolean checkIsXCSS(HttpServletRequest request){
		try {
		String org = (String) request.getSession().getAttribute(TokenName);
		String token = request.getHeader(TokenName);
			Cookie[] cokei =  request.getCookies();
		if (org.equals(token)) {
			return false;
		} else {
			return true;
		}
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return true;
	}

	/**
	 * @param request
	 * @param needSessionID 设备访问， 登录接口不需要session，其他如果没有，则返回错误
	 * @throws Exception
	 */
	public int requestProcess(HttpServletRequest request, HttpSession session, boolean needSessionID) throws Exception {
		
		requestJson= "";
		responseJson= "";
		errorMessageList.clear();
		
		requestJson = this.readJson(request);
		//logger.info("requestJson--->>"+requestJson);
		mSessionId = request.getHeader("sessionID");
		logger.info(request.getRequestURL() + ":sessionID:" + mSessionId);

//		if(needSessionID && checkIsXCSS(request)){
//			errorMessageList.add("非法请求，跨站攻击!");
//			return ErrorCode.EC_INVALID_PARAM;
//		}

		//判定Json是否为空
		if(requestJson != null && requestJson.length() == 0){
			if(errorMessageList.size()==0)
				errorMessageList.add("非法参数!");
			logger.info("requestJson is null");
			return ErrorCode.EC_INVALID_PARAM;
		}else{
			JSONObject j = JSONObject.parseObject(requestJson);
			if(j != null) {
				if(HTextUtils.isEmpty(mSessionId)){
					mSessionId = j.getString("sessionID");
				}
				String sign = j.getString("sign");
				requestJson = j.getString("reqData");

				String localSign = Md5.md5(requestJson + md5key);
//				if(!localSign.equals(sign)){
//					//sign不对
//					errorMessageList.add("非法请求");
//					return ErrorCode.EC_INVALID_REQUEST;
//				}

			}
		}
		
		//处理session
		if(needSessionID){
			//不是配置登陆， 检查sessionID
			if(mSessionId==null || mSessionId.length()==0){
				if(errorMessageList.size()==0)
					errorMessageList.add("无效的SessionID，请重试!");
				 logger.info("sessionID is null");
				 return ErrorCode.EC_INVALID_SESSIONID;
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
		return ErrorCode.EC_OK;
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
    		if(result.equals("")){
    			result=request.getParameter("json");
    		}
    		logger.info("request body: " + request.getRequestURL() + "====" + result);
			//logger.info(LMException.PREFIXINFOMESSAGE + "Request Body = " + json);
			return result;
		} catch (IOException e) {
			throw new HException(e.getMessage());
		}
    }
    
//    private void setResponse(HttpServletResponse response, Object outObj){
//        response.setContentType("text/html;charset=utf-8");
//        response.setHeader("sessionID", "sessionID1");
//        try {
//        	JsonGenerator jsonGenerator = null;
//        	ObjectMapper objectMapper = null;
//            logger.info("jsonGenerator");
//            objectMapper = new ObjectMapper();
//            jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
//
//            jsonGenerator.writeObject(outObj);
//
//            logger.info("ObjectMapper");
//            objectMapper.writeValue(response.getWriter(), outObj);
//            response.flushBuffer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    
    /**
     * 将错误码和返回的内容封装进response中并返回
     * @param response
     * @param
     */
    public void respond(HttpServletResponse response, HttpSession session, int errCode, String errMessage, Map<String, Object> dataMap) {
 		OutputStream os = null;
 		try {
 			os = response.getOutputStream();
 			response.setHeader("Cache-Control", "no-cache");
 			response.setContentType("application/json");
 			response.setCharacterEncoding("utf8");
			session.setAttribute(TokenName, TokenName);

			Map<String, Object> model = new HashMap<String, Object>();
			if(dataMap == null) {
				dataMap = new HashMap<String, Object>();
			}
			model.put("data", dataMap);
			model.put("errorCode", errCode);
			if(HTextUtils.isEmpty(errMessage)){
				errMessage = " ";
			}
			model.put("errorMessage", errMessage);
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			responseJson = gson.toJson(model);

 			logger.info("responseJson:  " + responseJson);
			byte[] data = responseJson.getBytes("utf8");
 			response.setContentLength(data.length);

 			os.write(data);
 		} catch (IOException e) {
 			e.printStackTrace();
 		} finally {
 			IOUtils.closeQuietly(os);
 		}
 	}
 	
     public void respondException(HttpServletResponse response, HttpSession session, int errCode, String errMessage) {
    	logger.info("respondException:" + errMessage);
      	errorMessageList.add(errMessage);
      	this.respond(response,session, errCode, errMessage, null);
      }
    
	/**
	 * 将notification统一包装起来, 然后输出
	 * @param dataMap 协议的消息体部分， 对应 responseData，不能为null
	 */
	public void outputRespBody(HttpServletResponse response, HttpSession session, Map<String, Object> dataMap){

//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("data", dataMap);
//
//		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//		responseJson = gson.toJson(model);
		
		this.respond(response, session, ErrorCode.EC_OK, null, dataMap);
	}

}
