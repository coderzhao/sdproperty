package com.note.cms.service;


import com.note.cms.data.model.TbGuestRole;
import com.note.cms.data.model.TbIpc;
import com.note.cms.data.model.TbUser;
import com.note.common.utils.HException;

import java.util.List;

/**
 * 
 * @author door 相关的service
 *
 */
public interface GuestRoleService extends BaseService{

    public TbGuestRole getByName(String name) throws HException;//根据名称返回对象
    public TbGuestRole getById(Integer id);

    public List<TbGuestRole> getAllGuestRole();
    public List<TbGuestRole> getListPage(TbUser self, TbGuestRole param, int pageNum, int count, String order, String orderRule);
    public void delete(Integer id);
    public TbGuestRole addOrUpdate(TbGuestRole data) throws HException;
    public int getCount(TbGuestRole param);
}
