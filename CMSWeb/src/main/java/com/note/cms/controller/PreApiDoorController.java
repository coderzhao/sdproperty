package com.note.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.note.cms.data.ErrorCode;
import com.note.cms.data.model.TbDoor;
import com.note.cms.data.model.TbUser;
import com.note.cms.service.DoorService;
import com.note.cms.service.UserService;
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
@RequestMapping("/PreApiDoor")

public class PreApiDoorController extends BaseWebPreController{
	private static final Logger logger = LoggerFactory.getLogger(PreApiDoorController.class);

	@Autowired
	private DoorService mDoorService;

	/***************************************************************************************
	 * 分页查询所有记录
	 * @return
	 * @throws Exception
	 ***************************************************************************************/
	@RequestMapping(value="/queryDoorList.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryDoorList(DataGridModel dgm, TbDoor param, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		//todo 分页查询所有的日志记录
		try {
//			int ret = this.requestProcess(request, session, false);
//			if(ret != ErrorCode.EC_OK){
//				//请求出错
//				this.respondException(response, session, ret, errorMessageList.get(0));
//				return;
//			}

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

			List<TbDoor> listData;
			listData = mDoorService.getListPage(self, param, pageNum, count, order, rule);
			if (listData == null)
				listData = new ArrayList<TbDoor>();
			Map<String, Object> responseDataMap = new HashMap<String, Object>();
			responseDataMap.put("total", mDoorService.getCount(param));
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
	@RequestMapping(value = "/deleteDoor.do", method = RequestMethod.POST)
	@ResponseBody
	public void deleteDoor(Integer id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		try {
			mDoorService.delete(id);
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
	public void addOrUpdate(TbDoor input, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{

		Map<String, String> map = new HashMap<String, String>();
		input.setCreateTime(new Date());
		try {
			mDoorService.addOrUpdate(input);
			super.outputRespBody(response, session, null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			throw e;
		}
		return;
	}

	@RequestMapping(value="/getAllDoor.do",method=RequestMethod.GET)
	@ResponseBody
	public List<TbDoor> getAllDoor(HttpServletRequest request, HttpSession session) throws Exception{
		//spring会利用jackson自动将返回值封装成JSON对象，比struts2方便了很多
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("getAllDoor:11" );
		try {
			//mAgentService.addOrUpdate(item);
			List<TbDoor> listVO;
			TbUser self = getTbUser(request);
			listVO = mDoorService.getListPage(self, null, 1, 200, null, null);
			return listVO;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("mes", "操作失败");
			throw e;
		}
	}

}
