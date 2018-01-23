package com.note.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.note.cms.data.ErrorCode;
import com.note.cms.data.model.TbDoor;
import com.note.cms.data.model.TbDoorLock;
import com.note.cms.data.model.TbUser;
import com.note.cms.data.vo.TbDoorLockVo;
import com.note.cms.service.DoorLockService;
import com.note.common.model.DataGridModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author xuxinjian
 * @des: 管理后台用， 用来管理日志记录
 */
@Controller
@RequestMapping("/PreApiDoorLock")

public class PreApiDoorLockController extends BaseWebPreController{
	private static final Logger logger = LoggerFactory.getLogger(PreApiDoorLockController.class);

	@Autowired
	private DoorLockService mDoorLockService;

	/***************************************************************************************
	 * 分页查询所有记录
	 * @return
	 * @throws Exception
	 ***************************************************************************************/
	@RequestMapping(value="/queryList.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList(DataGridModel dgm, TbDoorLock param, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		//todo 分页查询所有的日志记录
		try {
			TbUser self = getTbUser(request);

			JSONObject j = JSONObject.parseObject(requestJson);

			int pageNum = dgm.getPage();
			int count = dgm.getRows();
			if(count <= 0){
				count = 200;
			}
			String order = dgm.getSort();
			String rule = dgm.getOrder();

			logger.info("pageNum:" + pageNum + "count:" + count + "order:" + order + "rule:" + rule);

			List<TbDoorLockVo> listData;
			listData = mDoorLockService.getListPage(param, pageNum, count, order, rule);
			Map<String, Object> responseDataMap = new HashMap<String, Object>();
			responseDataMap.put("total", mDoorLockService.getCount(param));
			responseDataMap.put("rows", listData);

//			super.outputRespBody(response, session, responseDataMap);
			return responseDataMap;
		}catch(Exception e){
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			return null;
		}
	}

	/*
     * 删除用户
     */
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@ResponseBody
	public void delete(Integer id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		try {
			mDoorLockService.delete(id);
			super.outputRespBody(response, session, null);

		}catch (Exception e){
			e.printStackTrace();
			logger.info(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
		}

		return ;//重定向
	}

	@RequestMapping(value="/addOrUpdate.do",method=RequestMethod.POST)
	@ResponseBody
	public void addOrUpdate(TbDoorLock input, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{

		Map<String, String> map = new HashMap<String, String>();
		input.setCreateTime(new Date());
		try {
			mDoorLockService.addOrUpdate(input);
			super.outputRespBody(response, session, null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			throw e;
		}
		return;
	}

	@RequestMapping(value="/openDoor.do",method=RequestMethod.POST)
	@ResponseBody
	public void openDoor(Integer doorId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{

		Map<String, String> map = new HashMap<String, String>();
		try {
			mDoorLockService.openDoor(doorId);
			super.outputRespBody(response, session, null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			throw e;
		}
		return;
	}

}
