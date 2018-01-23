package com.note.cms.service;

import com.note.cms.data.model.TbDoor;
import com.note.cms.data.model.TbIpc;
import com.note.cms.data.model.TbUser;
import com.note.common.utils.HException;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author door 相关的service
 *
 */
public interface SDKService extends BaseService{
    public Map<String, Object> addFace(String image) throws Exception;
    /**
     *  增加一个摄像头的流程
     *  1. 在sdk中获取摄像头列表， 然后删除掉所有摄像头url跟要增加的摄像头重复的
     *  2. 增加一个新的摄像头到sdk中， 并返回一个cameraid
     *  3. 将该cameraid更新到tb_ipc表中
     * @param input
     * @return
     */
    public String addCamera(TbIpc input);//增加一个摄像头， 返回cameraId
    public boolean deleteCamera(TbIpc input);//删除摄像头
    public boolean updateSet(String keyname, String value);

    //闪开门
    public void flashOpenDoor(String ip, Integer port, Integer line, Integer time);
}
