package com.note.cms.service.impl;

import com.note.cms.dao.TbSysMapper;
import com.note.cms.data.model.*;
import com.note.cms.service.SDKService;
import com.note.cms.service.SettingService;
import com.note.common.Plugin.Page;
import com.note.common.utils.HException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


@Service("SettingService")
public class SettingServiceImpl implements SettingService {
	private static final Logger logger = LoggerFactory.getLogger(SettingServiceImpl.class);
	@Autowired
	private TbSysMapper mTbSysMapper;
	@Autowired
	private SDKService mSDKService;

	@Override
	public TbSys getById(int id) {
		return mTbSysMapper.selectByPrimaryKey(id);
	}

	@Override
	public TbSys getByKeyName(String name){
		TbSysExample example = new TbSysExample();
		TbSysExample.Criteria c = example.createCriteria();
		c.andKeynameEqualTo(name);
		List<TbSys> list = mTbSysMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public TbSys addOrUpdate(TbSys input) throws HException {
		if(input.getId() == null){
			mTbSysMapper.insert(input);
		}else{
//			TbSys old = mTbSysMapper.selectByPrimaryKey(input.getId());
			mTbSysMapper.updateByPrimaryKey(input);
		}
		//此处需要调用sdk的接口， 传递参数过去
		mSDKService.updateSet(input.getKeyname(), input.getValue());

		return input;
	}

	//取当前用户所属代理商及其下属代理商的所有用户
	public List<TbSys> getListPage(int pageNum, int count, String order, String orderRule){
		TbSysExample example = new TbSysExample();
		TbSysExample.Criteria c = example.createCriteria();
		if(order == null || orderRule == null)
			example.setOrderByClause("id desc");
		else
			example.setOrderByClause(order + " " + orderRule);
		example.setPage(new Page((pageNum-1) *count, count));

		List<TbSys> listData = mTbSysMapper.selectByExample(example);
		return listData;
	}

	public int getCount(){
		TbSysExample example = new TbSysExample();
		return (int)(mTbSysMapper.countByExample(example));
	}

	/**
	 * 获取采集服务程序存放图片路径对应的 根域名
	 * @return
	 */
	@Override
	public String getSdkImageUrlRoot(){
		TbSys sys  = getByKeyName(SettingService.KEY_STORAGE_URL_ROOT);
		return sys.getValue();
	}

	@Override
	public String getPhotoByImageFile(String imageFile){
		try {
			String url = getSdkImageUrlRoot();
			TbSys sys = getByKeyName(SettingService.KEY_STORAGE_PATH);
			String filePath = sys.getValue();

			int index = imageFile.indexOf(filePath);
			java.lang.String subStr = imageFile.substring(index + filePath.length());
			java.lang.String strPhoto = url + "/" + subStr;
			return strPhoto;
		}catch (Exception e){
			logger.error(e.getLocalizedMessage());
			return "http://localhost/";
		}
	}

	@Override
	public Boolean checkConfidenceEnough(float confidence){
		try {
			TbSys sys = getByKeyName(SettingService.KEY_CONFIDENCE);
			String value = sys.getValue();
			float sysconfidence = Float.parseFloat(value);
			confidence = confidence * 100;
			if(confidence > sysconfidence){
				return TRUE;
			}else{
				return FALSE;
			}
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		return TRUE;
	}
}
