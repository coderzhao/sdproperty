package com.note.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.note.cms.data.ErrorCode;
import com.note.cms.data.model.TbIpc;
import com.note.cms.data.model.TbUser;
import com.note.cms.data.vo.OutputUserVo;
import com.note.cms.data.vo.TbIpcVo;
import com.note.cms.service.IpcService;
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
 * @des: 管理后台用，
 */
@Controller
@RequestMapping("/PreApiIpc")

public class PreApiIpcController extends BaseWebPreController{
	private static final Logger logger = LoggerFactory.getLogger(PreApiIpcController.class);

	@Autowired
	private IpcService mIpcService;

	/***************************************************************************************
	 * 分页查询所有记录
	 * @return
	 * @throws Exception
	 ***************************************************************************************/
	@RequestMapping(value="/queryList.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList(DataGridModel dgm, TbIpc param, HttpServletRequest request, HttpServletResponse response, HttpSession session)
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
			String order = dgm.getSort();
			String rule = dgm.getOrder();

			logger.info("pageNum:" + pageNum + "count:" + count + "order:" + order + "rule:" + rule);

			List<TbIpcVo> listData;
			listData = mIpcService.getListPage(self, param, pageNum, count, order, rule);
			Map<String, Object> responseDataMap = new HashMap<String, Object>();
			responseDataMap.put("total", mIpcService.getCount(param));
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
			mIpcService.delete(id);
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
	public void addOrUpdate(TbIpc input, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{

		Map<String, String> map = new HashMap<String, String>();
		input.setCreateTime(new Date());
		try {
			mIpcService.addOrUpdate(input);
			super.outputRespBody(response, session, null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			throw e;
		}
		return;
	}

	@RequestMapping(value="/getAllIpcByUser.do",method=RequestMethod.GET)
	@ResponseBody
	public List<TbIpcVo> getAllIpcByUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		//todo 分页查询所有的日志记录
		try {
			OutputUserVo self = getOutputUserVo(request);
			List<TbIpcVo> listData = self.getListIpcVo();
			return listData;
		}catch(Exception e){
			logger.error(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			return null;
		}

	}

	//获取所有ipc列表， 给用户筛选用，显示在combox中
	@RequestMapping(value="/queryAllIpc.do",method=RequestMethod.POST)
	@ResponseBody
	public List<TbIpcVo> queryAllIpc(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		//todo 分页查询所有的日志记录
		try {
			TbUser self = getTbUser(request);
			if(null==self){
				List<TbIpc> tbIpcs=mIpcService.getAllIpc();
				if(null!=tbIpcs&&tbIpcs.size()>0) {
					List<TbIpcVo> tbIpcVos = new ArrayList<>();
					for (TbIpc tbIpc : tbIpcs) {
						TbIpcVo tbIpcVo= new TbIpcVo(tbIpc);
						tbIpcVos.add(tbIpcVo);
					}
					return tbIpcVos;
				}
			}
			List<TbIpcVo> listData;
			listData = mIpcService.getListPage(self, null, 1, 1000, null, null);

			return listData;
		}catch(Exception e){
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			return null;
		}

	}

}
