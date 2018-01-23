package com.note.cms.service;


import com.note.cms.data.model.TbGuest;
import com.note.cms.data.model.TbUser;
import com.note.cms.data.vo.GuestClickInOutVo;
import com.note.cms.data.vo.TbGuestModifyRecordVo;
import com.note.cms.data.vo.TbGuestVo;
import com.note.common.utils.HException;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author door 相关的service
 *
 */
public interface GuestService extends BaseService{

    public TbGuest getByName(String name) throws HException;//根据名称返回对象
    public TbGuest getByCode(String code);
    public TbGuest getById(Integer id) throws HException;
    public TbGuest getByCardNo(String cardNo);
    public List<TbGuestModifyRecordVo> getListPageRecord(TbGuest param, int pageNum, int count, String order, String orderRule);
    public long getCountRecord(TbGuest param);

    public List<TbGuestVo> getListPage(TbGuest param, int pageNum, int count, String order, String orderRule);
    public void delete(TbUser user, Integer id);
    public TbGuest addOrUpdate(TbUser user, TbGuest data) throws HException;
    public String addImages(TbUser user, TbGuest data,List<String> imagePathList,String webroot) throws HException;
    public int getCount(TbGuest param);
    public List<GuestClickInOutVo> getGuestReport(Integer guestRoleId , Timestamp startTime, Timestamp endTime);
    public List<GuestClickInOutVo> getGuestReportByIpc(Integer ipcId,Integer guestRoleId , Timestamp startTime, Timestamp endTime);


}
