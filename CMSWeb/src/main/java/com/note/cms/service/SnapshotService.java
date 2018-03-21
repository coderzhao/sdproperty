package com.note.cms.service;


import com.note.cms.data.model.TbSnapshot;
import com.note.cms.data.vo.InputSnapshotVo;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.data.vo.searchparam.ParamSnapshot;
import com.note.common.utils.HException;
import com.quadrant.dao.entity.Snapshot;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author door 相关的service
 *
 */
public interface SnapshotService extends BaseService{

    public TbSnapshot getById(Integer id) throws HException;

    public List<OutputSnapshotVo> getListPage(ParamSnapshot param, int pageNum, int count, String order, String orderRule);
    public void delete(Integer id);
    public Map<String, Object> add(InputSnapshotVo data) throws Exception;
    public int getCount(ParamSnapshot param);

    public void updateSnapshot(Integer snapshotId, String guest_code);

    public List<OutputSnapshotVo> getLastSnapshot(Integer ipcid, Integer lastsnapid, Integer forward, Integer count);

    public void addToSnapshot(InputSnapshotVo snapshot);

    public void openDoorCheck(int doorId, String person_id,float confidence);
}
