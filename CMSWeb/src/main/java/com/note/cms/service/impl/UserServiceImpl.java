package com.note.cms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.note.cms.dao.TbDoorMapper;
import com.note.cms.dao.TbInitSessionMapper;
import com.note.cms.dao.TbUserMapper;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.data.vo.OutputUserVo;
import com.note.cms.data.vo.TbIpcVo;
import com.note.cms.service.IpcService;
import com.note.cms.tools.CacheService;
import com.note.common.Plugin.Page;
import com.note.common.utils.HException;
import com.note.common.utils.HTextUtils;
import com.note.common.utils.Md5;
import com.note.cms.data.model.*;
import com.note.cms.service.UserService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("UserService")
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private TbUserMapper mTbUserMapper;
    @Autowired
    private TbInitSessionMapper mInitSessionMapper;
    @Autowired
    private TbDoorMapper mTbDoorMapper;
    @Autowired
    private IpcService mIpcService;
//

    public TbUser dologin(String username, String password) {
        try {
            TbUserExample example = new TbUserExample();
            TbUserExample.Criteria c = example.createCriteria();
            c.andMobileEqualTo(username);
            List<TbUser> listUser = mTbUserMapper.selectByExample(example);
            if (listUser != null && listUser.size() > 0) {
                TbUser item = listUser.get(0);
                if (password.equalsIgnoreCase(password)) {
                    logger.info("user:" + item.getMobile() + " login success");
                    return item;
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {

        }
        logger.info("user:" + username + " login fail");
        return null;
    }


    @Override
    public TbUser getUserByName(String name) throws HException {
        //mCacheService.flushDB();
        try {
            TbUserExample ue = new TbUserExample();
            ue.createCriteria().andNameEqualTo(name);
            List<TbUser> listUser =  mTbUserMapper.selectByExample(ue);
            if (listUser != null && listUser.size() > 0) {
                TbUser ret = listUser.get(0);
                return ret;
            } else {
                logger.info("has no user:" + name);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HException("没有当前名称用户");
        }
    }

    private TbUser getUserByMobile(String mobile) throws HException {
        try {
            TbUserExample ue = new TbUserExample();
            TbUserExample.Criteria c = ue.createCriteria();
            c.andMobileEqualTo(mobile);
            List<TbUser> listUser = mTbUserMapper.selectByExample(ue);
            if (listUser != null && listUser.size() > 0) {
                TbUser ret = listUser.get(0);
                return ret;
            } else {
                logger.info("has no user:" + mobile);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HException("没有当前手机号的用户");
        }
    }

    @Override
    public TbUser getUserById(Integer id) throws HException {
        return mTbUserMapper.selectByPrimaryKey(id);
    }

    //取当前用户所属代理商及其下属代理商的所有用户
    public List<OutputUserVo> getListPage(TbUser self, TbUser param, int pageNum, int count, String order, String orderRule) {
        TbUserExample example = new TbUserExample();
        if (param != null) {
            TbUserExample.Criteria c = example.createCriteria();

            if (param.getMobile() != null && param.getMobile().length() > 0) {
                c.andMobileLike("%" + param.getMobile() + "%");
            }

            if (param.getName() != null && param.getName().length() > 0) {
                c.andNameLike("%" + param.getName() + "%");
            }
        }
        if (order == null || orderRule == null)
            example.setOrderByClause("id desc");
        else
            example.setOrderByClause(order + " " + orderRule);
        example.setPage(new Page((pageNum - 1) * count, count));

        List<OutputUserVo> listVo = new ArrayList<OutputUserVo>();
        List<TbUser> listData = mTbUserMapper.selectByExample(example);
        if (listData != null && listData.size() > 0) {
            for (TbUser user : listData) {
                OutputUserVo vo = new OutputUserVo(user);
                if (user.getDoorId() != null) {
                    TbDoor door = mTbDoorMapper.selectByPrimaryKey(user.getDoorId());
                    if (null != door) {
                        vo.setDoorName(door.getName());
                    }
                }
                listVo.add(vo);
            }
        }

        return listVo;
    }

    public void delete(List<Integer> usersId) {
        for (int id : usersId) {
            mTbUserMapper.deleteByPrimaryKey(id);
        }
    }

    public TbUser addOrUpdate(TbUser user) {
        user.setCreateTime(new Date());
        if (user.getId() == null) {
            if (HTextUtils.isEmpty(user.getPassword())) {
                //如果创建用户没输入密码， 则默认六个8
                user.setPassword(Md5.getMD5Str("888888"));
            }
            mTbUserMapper.insert(user);
        } else {
            TbUser oldUser = mTbUserMapper.selectByPrimaryKey(user.getId());
            try {
                TbUser sameUsername = getUserByName(user.getMobile());
                if (sameUsername != null) {
                    throw new Exception("用户名: " + user.getName() + " 已存在");
                }
            } catch (Exception e) {

            }

            //如果用户没输入密码， 则用老密码
            if (HTextUtils.isEmpty(user.getPassword())) {
                user.setPassword(oldUser.getPassword());
            }
            mTbUserMapper.updateByPrimaryKey(user);
        }
        return user;
    }

    public int getCount(TbUser param) {
        TbUserExample example = new TbUserExample();
        if (param != null) {
            TbUserExample.Criteria c = example.createCriteria();
            if (param.getName() != null && param.getName().length() > 0) {
                c.andNameLike("%" + param.getName() + "%");
            }
            if (param.getMobile() != null && param.getMobile().length() > 0) {
                c.andMobileLike("%" + param.getMobile() + "%");
            }
        }
        return (int) (mTbUserMapper.countByExample(example));
    }

    //
//	//app登录
    public OutputUserVo doLoginApp(String mobile, String password) throws HException {

        OutputUserVo output = null;
        TbUser user = getUserByMobile(mobile);
        if (user != null && user.getPassword().equalsIgnoreCase(password)) {
            output = new OutputUserVo(user);

            List<TbIpcVo> listIpcVo = mIpcService.getListPage(null, null, 1, 100, null, null);
            output.setListIpcVo(listIpcVo);
            return output;
        } else {
            return null;
        }
    }


    //通过sessionID获取TbInitSession对象
    public TbInitSession getSessionBySessionID(String sessionID) {
        logger.info("getSessionBySessionID:" + sessionID);


        TbInitSessionExample example = new TbInitSessionExample();
        example.createCriteria().andSessionEqualTo(sessionID);
        List<TbInitSession> listSession = mInitSessionMapper.selectByExample(example);
        if (listSession.size() <= 0)
            return null;
        TbInitSession tbInitSession = listSession.get(0);
        return tbInitSession;
    }

    /**
     * 1. 手机已关联到userid，则判断userId与当前userId是否一致，如果一致，则更新时间，否则取消老的关联，更新新的关联
     * 2. 手机未关联到账号，则创建新关联关系
     * @param data
     * @return
     */
//	public int bindDeviceId(TbPush data){
//		data.setLastTime(new Date());
//
//		TbPushExample example = new TbPushExample();
//		TbPushExample.Criteria c = example.createCriteria();
//		//c.andUserIdEqualTo(data.getUserId());
//		c.andGetuiIdEqualTo(data.getGetuiId());
//		List<TbPush> listPush = mTbPushMapper.selectByExample(example);
//		if(listPush == null || listPush.size() <= 0){
//			//该设备之前没注册
//			data.setCreateTime(new Date());
//			data.setAvaible(true);
//			mTbPushMapper.insert(data);
//		}else{
//			//设备已注册
//			TbPush old = listPush.get(0);
//			if(old.getUserId() == data.getUserId()){
//				//如果该手机之前关联账号和本次关联相同
//				old.setAvaible(true);
//				old.setLastTime(new Date());
//				mTbPushMapper.updateByPrimaryKey(old);
//			}else{
//				//如果手机关联账号和本次关联不一致，则删除老的关联关系
//				mTbPushMapper.deleteByPrimaryKey(old.getId());
//
//				data.setCreateTime(new Date());
//				data.setAvaible(true);
//				mTbPushMapper.insert(data);
//			}
//		}
//		return KConst.RESULT_OK;
//	}
//
//	//获取当前所有代理商关联的用户
//	public List<TbUser> getListUserByAgentId(int agentId){
//		TbUserExample example = new TbUserExample();
//		TbUserExample.Criteria c = example.createCriteria();
//		c.andAgentIdEqualTo(agentId);
//
//		List<TbUser> listData = mTbUserMapper.selectByExample(example);
//		if(listData == null){
//			listData = new ArrayList<TbUser>();
//		}
//
//		return listData;
//	}
}
