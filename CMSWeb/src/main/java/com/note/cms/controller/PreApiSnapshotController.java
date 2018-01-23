package com.note.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.note.cms.NConst;
import com.note.cms.dao.TbGuestMapper;
import com.note.cms.data.ErrorCode;
import com.note.cms.data.model.TbGuest;
import com.note.cms.data.model.TbUser;
import com.note.cms.data.vo.OutputSnapshotFaceVo;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.data.vo.searchparam.ParamSnapshot;
import com.note.cms.service.GuestService;
import com.note.cms.service.SnapshotService;
import com.note.common.model.DataGridModel;
import org.apache.maven.artifact.repository.metadata.Snapshot;
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
 * @des: 快照列表管理，
 */
@Controller
@RequestMapping("/PreApiSnapshot")

public class PreApiSnapshotController extends BaseWebPreController{
	private static final Logger logger = LoggerFactory.getLogger(PreApiSnapshotController.class);

	@Autowired
	private SnapshotService mSnapshotService;
	@Autowired
	private GuestService mGuestService;
	@Autowired
	private TbGuestMapper mTbGuestMapper;

	/***************************************************************************************
	 * 分页查询所有记录
	 * @return
	 * @throws Exception
	 ***************************************************************************************/
	@RequestMapping(value="/queryList.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList(DataGridModel dgm, ParamSnapshot param, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		//todo 分页查询所有的日志记录
		try {
			logger.info("queryList begin");

			TbUser self = getTbUser(request);

			int pageNum = dgm.getPage();
			int count = dgm.getRows();
//			String order = dgm.getSort();
//			String rule = dgm.getOrder();
			String order = "id";
			String rule = "desc";

			logger.info("pageNum:" + pageNum + "count:" + count + "order:" + order + "rule:" + rule);

			List<OutputSnapshotVo> listData;
			listData = mSnapshotService.getListPage(param, pageNum, count, order, rule);
			if (listData == null)
				listData = new ArrayList<OutputSnapshotVo>();
			Map<String, Object> responseDataMap = new HashMap<String, Object>();
			responseDataMap.put("total", mSnapshotService.getCount(param));
			responseDataMap.put("rows", listData);
//			super.outputRespBody(response, session, responseDataMap);
			return responseDataMap;
		}catch(Exception e){
			logger.info(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			return null;
		}

	}

	/***************************************************************************************
	 * 分页查询某个访客的所有访问记录
	 * @return
	 * @throws Exception
	 ***************************************************************************************/
	@RequestMapping(value="/queryListByGuest.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryListByGuest(DataGridModel dgm, ParamSnapshot param, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		//todo 分页查询所有的日志记录
		try {

			TbUser self = getTbUser(request);

			int pageNum = dgm.getPage();
			int count = dgm.getRows();
			String order = "id";
			String rule = "desc";

			logger.info("pageNum:" + pageNum + "count:" + count + "order:" + order + "rule:" + rule);

			List<OutputSnapshotVo> listData;
			listData = mSnapshotService.getListPage(param, pageNum, count, order, rule);
			if (listData == null)
				listData = new ArrayList<OutputSnapshotVo>();
			Map<String, Object> responseDataMap = new HashMap<String, Object>();
			responseDataMap.put("total", mSnapshotService.getCount(param));
			responseDataMap.put("rows", listData);

			TbGuest tbGuest = mGuestService.getByCode(param.getGuestCode());
			tbGuest.setCount(mSnapshotService.getCount(param));
			int result =mTbGuestMapper.updateByPrimaryKeySelective(tbGuest);

//			super.outputRespBody(response, session, responseDataMap);
			return responseDataMap;
		}catch(Exception e){
			logger.info(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			return null;
		}

	}

	@RequestMapping(value = "getLatest.do", method = RequestMethod.GET)
	@ResponseBody
	public List<OutputSnapshotVo> getLatest(Integer ipcid, Integer lastsnapid, HttpServletRequest request,	HttpServletResponse response){
		try{
			//获取请求消息
			logger.info("getLatest ipcid=" + ipcid + "---snapid=" + lastsnapid);
			Map<String, Object> responseDataMap = new HashMap<String, Object>();

			List<OutputSnapshotVo> output = mSnapshotService.getLastSnapshot(ipcid, lastsnapid, NConst.FORWARD_NEW, 5);
			logger.info("getLatest:" + output.toString());
			return output;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}



}
