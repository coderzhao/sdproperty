package com.note.cms.service.impl;

import com.note.cms.common.Constant;
import com.note.cms.dao.TbDoorLockMapper;
import com.note.cms.dao.TbDoorMapper;
import com.note.cms.data.model.*;
import com.note.cms.data.vo.TbDoorLockVo;
import com.note.cms.service.DoorLockService;
import com.note.cms.service.SDKService;
import com.note.common.Plugin.Page;
import com.note.common.utils.HException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("DoorLockService")
public class DoorLockServiceImpl implements DoorLockService {
	private static final Logger logger = LoggerFactory.getLogger(DoorLockServiceImpl.class);
	@Autowired
	private TbDoorLockMapper mTbDoorLockMapper;
	@Autowired
	private TbDoorMapper mTbDoorMapper;
	@Autowired
	private SDKService mSDKService;

	//取当前用户所属代理商及其下属代理商的所有用户
	public List<TbDoorLockVo> getListPage(TbDoorLock param, int pageNum, int count, String order, String orderRule){

		TbDoorLockExample example = new TbDoorLockExample();
		if(param != null){
			TbDoorLockExample.Criteria c = example.createCriteria();

			if(param.getIp() != null && param.getIp().length() > 0){
				c.andIpLike("%" + param.getIp() + "%");
			}

			if(param.getName() != null && param.getName().length() > 0){
				c.andNameLike("%" + param.getName() + "%");
			}
		}
		if(order == null || orderRule == null)
			example.setOrderByClause("id desc");
		else
			example.setOrderByClause(order + " " + orderRule);
		if(pageNum == 0){
			pageNum = 1;
		}
		example.setPage(new Page((pageNum-1) *count, count));

		List<TbDoorLockVo> listVo = new ArrayList<TbDoorLockVo>();
		List<TbDoorLock> listData = mTbDoorLockMapper.selectByExample(example);
		if(listData != null && listData.size() > 0){
			for(TbDoorLock item : listData){
				TbDoorLockVo vo = new TbDoorLockVo(item);
				TbDoor door = mTbDoorMapper.selectByPrimaryKey(item.getDoorId());
				vo.setDoorName(door.getName());
				listVo.add(vo);
			}
		}

		return listVo;
	}

	public void delete(Integer id){
			mTbDoorLockMapper.deleteByPrimaryKey(id);
	}

	public TbDoorLock addOrUpdate(TbDoorLock input){
		input.setCreateTime(new Date());
		if(input.getId() == null){
			mTbDoorLockMapper.insert(input);
		}else{
			mTbDoorLockMapper.updateByPrimaryKey(input);
		}
		return input;
	}

	public int getCount(TbDoorLock param){
		TbDoorLockExample example = new TbDoorLockExample();
		if(param != null){
			TbDoorLockExample.Criteria c = example.createCriteria();
			if(param.getName() != null && param.getName().length() > 0){
				c.andNameLike("%" + param.getName() + "%");
			}
			if(param.getIp() != null && param.getIp().length() > 0){
				c.andIpLike("%" + param.getIp() + "%");
			}
		}
		return (int)(mTbDoorLockMapper.countByExample(example));
	}

	public TbDoorLock getByDoorId(Integer id,boolean dangerFlag){
		TbDoorLockExample example = new TbDoorLockExample();
		TbDoorLockExample.Criteria c = example.createCriteria();
		c.andDoorIdEqualTo(id);
		if(dangerFlag){
			c.andSecurityLevelEqualTo(Constant.dangerLevel);
		}else {
			c.andSecurityLevelNotEqualTo(Constant.dangerLevel);
		}
		List<TbDoorLock> listDoorLock = mTbDoorLockMapper.selectByExample(example);
		if(listDoorLock != null && listDoorLock.size() > 0){
			return listDoorLock.get(0);
		}else{
			return null;
		}
	}

	public boolean openDoor(Integer id){
		TbDoorLock lock = mTbDoorLockMapper.selectByPrimaryKey(id);
		mSDKService.flashOpenDoor(lock.getIp(), lock.getPort(), lock.getLine(), lock.getTime());
		return true;
	}
}
