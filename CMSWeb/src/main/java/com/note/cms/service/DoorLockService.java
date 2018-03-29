package com.note.cms.service;

import com.note.cms.data.model.TbDoor;
import com.note.cms.data.model.TbDoorLock;
import com.note.cms.data.model.TbUser;
import com.note.cms.data.vo.TbDoorLockVo;
import com.note.common.utils.HException;

import java.util.List;

/**
 * 
 * @author door 相关的service
 *
 */
public interface DoorLockService extends BaseService{

    public List<TbDoorLockVo> getListPage(TbDoorLock param, int pageNum, int count, String order, String orderRule);
    public void delete(Integer id);
    public TbDoorLock addOrUpdate(TbDoorLock user) throws HException;
    public int getCount(TbDoorLock param);
    public TbDoorLock getByDoorId(Integer id,boolean dangerFlag);//获取某个门的门禁
    public boolean openDoor(Integer id);
}
