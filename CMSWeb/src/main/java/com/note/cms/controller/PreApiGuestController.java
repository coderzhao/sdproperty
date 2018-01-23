package com.note.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.note.cms.data.ErrorCode;
import com.note.cms.data.model.TbGuest;
import com.note.cms.data.model.TbUser;
import com.note.cms.data.vo.TbGuestModifyRecordVo;
import com.note.cms.data.vo.TbGuestVo;
import com.note.cms.service.GuestService;
import com.note.cms.service.SnapshotService;
import com.note.common.model.DataGridModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xuxinjian
 * @des: 访客列表管理，
 */
@Controller
@RequestMapping("/PreApiGuest")

public class PreApiGuestController extends BaseWebPreController{
	private static final Logger logger = LoggerFactory.getLogger(PreApiGuestController.class);

	@Autowired
	private GuestService mGuestService;
	@Autowired
	private SnapshotService mSnapShotService;

	/***************************************************************************************
	 * 分页查询所有记录
	 * @return
	 * @throws Exception
	 ***************************************************************************************/
	@RequestMapping(value="/queryList.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList(DataGridModel dgm, TbGuest param, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		//todo 分页查询所有的日志记录
		try {
			TbUser self = getTbUser(request);

			JSONObject j = JSONObject.parseObject(requestJson);

			int pageNum = dgm.getPage();
			int count = dgm.getRows();
			String order = dgm.getSort();
			String rule = dgm.getOrder();

			logger.info("pageNum:" + pageNum + "count:" + count + "order:" + order + "rule:" + rule);

			List<TbGuestVo> listData;
			listData = mGuestService.getListPage(param, pageNum, count, order, rule);
			if (listData == null) {
				listData = new ArrayList<TbGuestVo>();
			}
			Map<String, Object> responseDataMap = new HashMap<String, Object>(16);
			responseDataMap.put("total", mGuestService.getCount(param));
			responseDataMap.put("rows", listData);

			return responseDataMap;
		}catch(Exception e){
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			return null;
		}

	}

	@RequestMapping(value="/queryListRecord.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryListRecord(DataGridModel dgm, TbGuest param, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		//todo 分页查询所有的日志记录
		try {
			TbUser self = getTbUser(request);

			JSONObject j = JSONObject.parseObject(requestJson);

			int pageNum = dgm.getPage();
			int count = dgm.getRows();
			String order = dgm.getSort();
			String rule = dgm.getOrder();

			logger.info("pageNum:" + pageNum + "count:" + count + "order:" + order + "rule:" + rule);

			List<TbGuestModifyRecordVo> listData;
			listData = mGuestService.getListPageRecord(param, pageNum, count, order, rule);
			if (listData == null) {
				listData = new ArrayList<TbGuestModifyRecordVo>();
			}
			Map<String, Object> responseDataMap = new HashMap<String, Object>(16);
			responseDataMap.put("total", mGuestService.getCountRecord(param));
			responseDataMap.put("rows", listData);

			return responseDataMap;
		}catch(Exception e){
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			return null;
		}

	}


	/*
     * 删除访客
     */
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@ResponseBody
	public void delete(Integer id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		try {
			TbUser user = getTbUser(request);
			mGuestService.delete(user, id);
			super.outputRespBody(response, session, null);

		}catch (Exception e){
			e.printStackTrace();
			logger.info(e.getMessage());
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
		}

		return ;//重定向
	}

	/**
	 *
	 * @param snapshotId 如果是从快照列表添加的访客身份， 需要讲快照id传递过来
	 * @param input
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addOrUpdate.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addOrUpdate(Integer snapshotId, TbGuest input, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
		logger.info("snapshotid:" + snapshotId + "|input:" + input.toString());

		Map<String, Object> map = new HashMap<String, Object>(16);
		input.setCreateTime(new Date());
		try {
			TbUser user = getTbUser(request);
			TbGuest guest = mGuestService.addOrUpdate(user, input);
			if(null != guest){
				// 成功
				if(snapshotId != null){
					//从快照列表添加的访客，需要更新快照 id对应的数据
					mSnapShotService.updateSnapshot(snapshotId, guest.getCode());
				}

				map.put("result", 1);
			}else{
				map.put("result", 0);
				map.put("mes", "增加或修改访客信息失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			map.put("result", 0);
			map.put("mes", "增加或修改访客信息失败");
		}
		return map;
	}

	@RequestMapping(value="/addImages.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addImages(Integer snapshotId, TbGuest input,
										   @RequestParam(value = "uploadImageList", required = true) List<String> imagePathList,
										   @RequestParam(value = "webroot", required = true) String webroot,
										 HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
		logger.info("snapshotid:" + snapshotId + "|input:" + input.toString());

		Map<String, Object> map = new HashMap<String, Object>(16);
		input.setCreateTime(new Date());
		try {
			TbUser user = getTbUser(request);
			if(imagePathList.size()>0){
				String result = mGuestService.addImages(user, input,imagePathList,webroot);
				if(result.equals("Success")){
					// 成功
					map.put("result", 1);
				}
			}else{
				map.put("result", 0);
				map.put("mes", "批量增加访客失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			map.put("result", 0);
			map.put("mes", "批量增加访客失败");
		}
		return map;
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request,ServletRequestDataBinder binder) throws Exception {
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
	      binder.registerCustomEditor(Date.class, dateEditor);
//	      super.initBinder(request, binder);
	}

}
