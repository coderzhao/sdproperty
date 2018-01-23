package com.note.cms.service.impl;

import com.note.cms.dao.TbGuestMapper;
import com.note.cms.dao.TbGuestModifyRecordMapper;
import com.note.cms.dao.TbGuestRoleMapper;
import com.note.cms.dao.TbUserMapper;
import com.note.cms.data.model.*;
import com.note.cms.data.vo.GuestClickInOutVo;
import com.note.cms.data.vo.GuestReportParams;
import com.note.cms.data.vo.TbGuestModifyRecordVo;
import com.note.cms.data.vo.TbGuestVo;
import com.note.cms.service.GuestRoleService;
import com.note.cms.service.GuestService;
import com.note.cms.service.SDKService;
import com.note.cms.service.SettingService;
import com.note.common.Plugin.Page;
import com.note.common.utils.HException;
import com.note.common.utils.HTextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("GuestService")
public class GuestServiceImpl implements GuestService {
    private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);
    @Autowired
    private TbGuestMapper mTbGuestMapper;
    @Autowired
    private GuestRoleService mGuestRoleService;
    @Autowired
    private SettingService mSettingService;
    @Autowired
    private TbGuestRoleMapper mTbGuestRoleMapper;
    @Autowired
    private SDKService mSDKService;
    @Autowired
    private TbGuestModifyRecordMapper mTbGuestModifyRecordMapper;
    @Autowired
    private TbUserMapper mTbUserMapper;

    @Override
    public TbGuest getByName(String name) throws HException {
        try {
            TbGuestExample ue = new TbGuestExample();
            ue.createCriteria().andNameEqualTo(name);
            List<TbGuest> listGuest = (List<TbGuest>) mTbGuestMapper.selectByExample(ue);
            if (listGuest != null && listGuest.size() > 0) {
                TbGuest ret = listGuest.get(0);
                return ret;
            } else {
                logger.info("has no Guest:" + name);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HException("没有当前名称的访客角色");
        }
    }

    @Override
    public TbGuest getByCode(String code) {
        try {
            TbGuestExample ue = new TbGuestExample();
            ue.createCriteria().andCodeEqualTo(code);
            List<TbGuest> listGuest = (List<TbGuest>) mTbGuestMapper.selectByExample(ue);
            if (listGuest != null && listGuest.size() > 0) {
                TbGuest ret = listGuest.get(0);
                return ret;
            } else {
                logger.info("has no Guest code:" + code);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public TbGuest getById(Integer id) throws HException {
        return mTbGuestMapper.selectByPrimaryKey(id);
    }

    public List<TbGuestModifyRecordVo> getListPageRecord(TbGuest param, int pageNum, int count, String order, String orderRule){
        TbGuestModifyRecordExample example = new TbGuestModifyRecordExample();
        example.setOrderByClause("id desc");
        example.setPage(new Page((pageNum - 1) * count, count));
        List<TbGuestModifyRecord> listData = mTbGuestModifyRecordMapper.selectByExample(example);
        List<TbGuestModifyRecordVo> listVo = new ArrayList<TbGuestModifyRecordVo>();
        if(listData != null && listData.size() > 0){
            for(TbGuestModifyRecord record : listData){
                TbGuestModifyRecordVo vo = new TbGuestModifyRecordVo();
                vo.setId(record.getId());
                vo.setUserId(record.getUserId());
                vo.setGuestName(record.getGuestName());
                vo.setGuestId(record.getGuestId());
                vo.setCreateTime(record.getCreateTime());
                vo.setOperation(record.getOperation());
                TbUser user = mTbUserMapper.selectByPrimaryKey(record.getUserId());
                if(user != null) {
                    vo.setUserName(user.getName());
                }else{
                }
                listVo.add(vo);
            }
        }
        return listVo;
    }

    public long getCountRecord(TbGuest param){
        TbGuestModifyRecordExample example = new TbGuestModifyRecordExample();
        long count = mTbGuestModifyRecordMapper.countByExample(example);
        return count;
    }

    public List<TbGuestVo> getListPage(TbGuest param, int pageNum, int count, String order, String orderRule) {
        TbGuestExample example = createExample(param);

        logger.info("order:" + order + "  rule:" + orderRule);
//        example.setOrderByClause("last_time desc");
        if(order == null || orderRule == null)
            example.setOrderByClause("last_time desc");
        else
            example.setOrderByClause(order + " " + orderRule);

        example.setPage(new Page((pageNum - 1) * count, count));

        List<TbGuestVo> listVo = new ArrayList<TbGuestVo>();
        List<TbGuestRole> listRole = mGuestRoleService.getAllGuestRole();

        List<TbGuest> listData = mTbGuestMapper.selectByExample(example);
        if (listData != null && listData.size() > 0) {
            for (TbGuest item : listData) {
                TbGuestVo vo = new TbGuestVo(item);
                vo.setPhoto(item.getUploadImage());
//                vo.setPhoto(mSettingService.getPhotoByImageFile(item.getImage()));
                //获取访客角色名称
                if (item.getGuestRoleId() != null) {
                    //存在角色id的， 去匹配角色，否则直接设置为未知
                    for (TbGuestRole role : listRole) {

                        if (role.getId().equals(item.getGuestRoleId())) {
                            vo.setGuestRoleName(role.getName());
                            break;
                        }
                    }
                }
                if (HTextUtils.isEmpty(vo.getGuestRoleName())) {
                    vo.setGuestRoleName("未知");
                }
                listVo.add(vo);
            }
        }

        return listVo;
    }

    public void delete(TbUser user, Integer id) {
        //删除访客调用删除接口
        //to do
        TbGuest guest = mTbGuestMapper.selectByPrimaryKey(id);
        mTbGuestMapper.deleteByPrimaryKey(id);

        //新增访客修改记录
        TbGuestModifyRecord record = new TbGuestModifyRecord();
        record.setCreateTime(new Date());
        record.setGuestId(guest.getId());
        record.setGuestName(guest.getName());
        record.setUserId(user.getId());
        record.setOperation("delete");
        mTbGuestModifyRecordMapper.insert(record);
    }

    public TbGuest addOrUpdate(TbUser user, TbGuest input) {
        try {
            if (input.getId() == null && (!HTextUtils.isEmpty(input.getCode()) ||(!HTextUtils.isEmpty(input.getUploadImage())))) {
                //新增
                input.setUserIdCreate(user.getId());
                input.setLastModifyUserId(user.getId());
                input.setCreateTime(new Date());
                input.setLastModifyTime(new Date());
                //todo
                //注册headImage中的图片
                Map<String, Object> result = mSDKService.addFace(input.getUploadImage());
                input.setImage((String) result.get("image"));
                input.setCode((String) result.get("code"));
                mTbGuestMapper.insert(input);

                //新增访客修改记录
                TbGuestModifyRecord record = new TbGuestModifyRecord();
                record.setCreateTime(new Date());
                record.setGuestId(input.getId());
                record.setGuestName(input.getName());
                record.setUserId(user.getId());
                record.setOperation("add");
                mTbGuestModifyRecordMapper.insert(record);

                return input;
            } else if (input.getId() != null) {
                //更新,如果不更新头像， 则头像字段传null
                TbGuest old = mTbGuestMapper.selectByPrimaryKey(input.getId());
                if (old == null) {
                    return null;
                }
                old.setLastModifyUserId(user.getId());
                old.setLastModifyTime(new Date());
                //todo
                //注册headImage中的图片,如果uploadImage不为空，则注册到图库中， 并更新用户信息
                if(!HTextUtils.isEmpty(input.getUploadImage())) {
                    //图片发生了变化
                    if(!old.getImage().equalsIgnoreCase(input.getUploadImage())) {
                        mSDKService.addFace(input.getUploadImage());
                        Map<String, Object> result = mSDKService.addFace(input.getUploadImage());
                        old.setImage((String) result.get("image"));
                        old.setCode((String) result.get("code"));
                    }
                }

                if (old.getCreateTime() != null) {
                    old.setCreateTime(new Date());
                }
                old.setAge(input.getAge());
                if (input.getCardNo() != null) {
                    old.setCardNo(input.getCardNo());
                }
                old.setGender(input.getGender());
                old.setName(input.getName());
                old.setRoom(input.getRoom());
                old.setGuestRoleId(input.getGuestRoleId());
                old.setLockEndTime(input.getLockEndTime());
                old.setLockStartTime(input.getLockStartTime());

                mTbGuestMapper.updateByPrimaryKey(old);

                //更新访客修改记录
                TbGuestModifyRecord record = new TbGuestModifyRecord();
                record.setCreateTime(new Date());
                record.setGuestId(old.getId());
                record.setGuestName(old.getName());
                record.setUserId(user.getId());
                record.setOperation("update");
                mTbGuestModifyRecordMapper.insert(record);
                return old;

            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String addImages(TbUser user, TbGuest input,List<String> imagePathList,String webroot) {
        try {
            for(String imagePath :imagePathList){
                input.setUserIdCreate(user.getId());
                input.setLastModifyUserId(user.getId());
                input.setCreateTime(new Date());
                input.setLastModifyTime(new Date());
                //todo
                //注册headImage中的图片
                String imageRealPath =imagePath.split("#.pn#")[0];
                String personName = imagePath.split("#.pn#")[1];
                input.setUploadImage(webroot+imageRealPath);
                Map<String, Object> result = mSDKService.addFace(webroot+imageRealPath);
                input.setImage((String) result.get("image"));
                input.setCode((String) result.get("code"));
                input.setName(personName);
                mTbGuestMapper.insert(input);

                //新增访客修改记录
                TbGuestModifyRecord record = new TbGuestModifyRecord();
                record.setCreateTime(new Date());
                record.setGuestId(input.getId());
                record.setGuestName(input.getName());
                record.setUserId(user.getId());
                record.setOperation("add");
                mTbGuestModifyRecordMapper.insert(record);
            }
            return "Success";
        } catch (Exception e) {
            return e.toString();
        }
    }

    public int getCount(TbGuest param) {
        TbGuestExample example = createExample(param);
        return (int) (mTbGuestMapper.countByExample(example));
    }

    private TbGuestExample createExample(TbGuest param) {
        TbGuestExample example = new TbGuestExample();
        if (param != null) {
            TbGuestExample.Criteria c = example.createCriteria();

            if (param.getName() != null && param.getName().length() > 0) {
                c.andNameLike("%" + param.getName() + "%");
            }

            if (param.getCode() != null && param.getCode().length() > 0) {
                c.andCodeLike("%" + param.getCode() + "%");
            }

            if (param.getRoom() != null && param.getRoom().length() > 0) {
                c.andRoomLike("%" + param.getRoom() + "%");
            }

            if (param.getGuestRoleId() != null) {
                if (param.getGuestRoleId() == 0) {
                    //0代表全部
                } else if (param.getGuestRoleId() == -1) {
                    c.andGuestRoleIdIsNull();
                } else {
                    c.andGuestRoleIdEqualTo(param.getGuestRoleId());
                }

            }
        }
        return example;
    }

    @Override
    public List<GuestClickInOutVo> getGuestReport(Integer guestRoleId, Timestamp startTime, Timestamp endTime) {
        GuestReportParams params = new GuestReportParams();
        params.setGuestRoleId(guestRoleId);
        params.setStartTime(startTime);
        params.setEndTime(endTime);
        return mTbGuestMapper.findClickInOutRecord(params);

    }

    @Override
    public TbGuest getByCardNo(String cardNo) {
        try {
            TbGuestExample ue = new TbGuestExample();
            ue.createCriteria().andCardNoEqualTo(cardNo);
            List<TbGuest> listGuest = (List<TbGuest>) mTbGuestMapper.selectByExample(ue);
            if (listGuest != null && listGuest.size() > 0) {
                TbGuest ret = listGuest.get(0);
                return ret;
            } else {
                logger.info("has no Guest cardNo:" + cardNo);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<GuestClickInOutVo> getGuestReportByIpc(Integer ipcId, Integer guestRoleId, Timestamp startTime,
                                                       Timestamp endTime) {
        GuestReportParams params = new GuestReportParams();
        params.setGuestRoleId(guestRoleId);
        params.setStartTime(startTime);
        params.setEndTime(endTime);
        params.setIpcId(ipcId);
        return mTbGuestMapper.findClickInOutRecordByIpc(params);
    }
}
