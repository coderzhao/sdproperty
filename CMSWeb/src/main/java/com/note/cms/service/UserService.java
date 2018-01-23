package com.note.cms.service;


import com.note.cms.data.model.TbInitSession;
import com.note.cms.data.model.TbUser;
import com.note.cms.data.vo.OutputUserVo;
import com.note.common.utils.HException;

import java.util.List;

/**
 * 
 * @author user相关的service
 *
 */
public interface UserService extends BaseService{
    public TbUser dologin(String username, String password);

    public TbUser getUserByName(String username) throws HException;//用户登陆,返回给客户端的json对象对应的java对象
    public TbUser getUserById(Integer id) throws HException;

    /**
     *
     * @param self 当前登陆用户
     * @param param 前端页面搜索传递来的参数对象
     * @param pageNum
     * @param count
     * @param order
     * @param orderRule
     * @return
     */
    public List<OutputUserVo> getListPage(TbUser self, TbUser param, int pageNum, int count, String order, String orderRule);
    public void delete(List<Integer> usersId);
    public TbUser addOrUpdate(TbUser user) throws HException;
    public int getCount(TbUser param);

    //登录相关
    public OutputUserVo doLoginApp(String mobile, String password) throws HException;

    //session相关
    public TbInitSession getSessionBySessionID(String sessionID);

//    public int bindDeviceId(TbPush data);

//    public List<TbUser> getListUserByAgentId(int agentId);

}
