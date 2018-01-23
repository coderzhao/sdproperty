package com.note.cms.controller;

import com.google.common.base.Strings;
import com.note.cms.data.ErrorCode;
import com.note.cms.data.vo.GuestClickInOutVo;
import com.note.cms.service.GuestService;
import com.note.cms.service.IpcService;
import com.quadrant.CameraServerBroadcaster;
import com.quadrant.ws.VideoViewerManager;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/quadrant")
public class QuadrantController extends BaseWebPreController {
	private static final Logger logger = LoggerFactory.getLogger(QuadrantController.class);
	@Autowired
	private GuestService mGuestService;
	
	@Autowired
	private CameraServerBroadcaster cameraServerBroadcaster;
	
	@Autowired
    com.quadrant.FDCameraDataHandler FDCameraDataHandler;
	
	@Autowired
	private IpcService ipcService;
	
	@RequestMapping(value="/report.do",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryListRecord(Integer guestRoleId , Integer ipcId, String date , HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		try {
//			TbUser user = getLoginTbUser(request);
			logger.info("report params ,ipcId={}",ipcId);
			if(guestRoleId == null) guestRoleId = 1;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(Strings.isNullOrEmpty(date)) date =  toDay();
			Timestamp startTime = new Timestamp(sdf.parse(date +" 00:00:00").getTime());
			Timestamp endTime = new Timestamp(sdf.parse(date +" 23:59:59").getTime());
			List<GuestClickInOutVo> listData = null;
			if(ipcId == null)
				listData = mGuestService.getGuestReport(guestRoleId, startTime, endTime);
			else
				listData = mGuestService.getGuestReportByIpc(ipcId, guestRoleId, startTime, endTime);
			
			System.out.println(listData);
			Map<String, Object> responseDataMap = new HashMap<String, Object>(16);
			responseDataMap.put("total", listData.size());
			responseDataMap.put("rows", listData);
			return responseDataMap;

		}catch(Exception e){
			logger.error(e.getMessage(), e);
			super.respondException(response, session, ErrorCode.EC_ERROR, e.getMessage());
			return null;
		}

	}
	
	private String toDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	
	@RequestMapping(value="/broadcast.do",method=RequestMethod.GET)
	@ResponseBody
	public void notifyCamera( HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		cameraServerBroadcaster.broadcast();
		logger.info("Broadcast camera on {}:{}" , cameraServerBroadcaster.getAddress(),cameraServerBroadcaster.getPort());
	}
	
	@RequestMapping(value="/getOnlineCamera.do",method=RequestMethod.GET)
	@ResponseBody
	public Set<String> getOnlineCamera( HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return FDCameraDataHandler.getOnlineCameras();
	}
	
	
	@RequestMapping(value="/stopViewing.do",method=RequestMethod.GET)
	@ResponseBody
	public void stopViewing(String mac , HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		boolean result =  VideoViewerManager.getInstance().stopViewingByMac(mac);
		logger.info("Find orphan viewer of {} to stop viewing, result=",mac,result);
	}
	
}
