package com.note.cms.service;


import com.note.cms.data.model.TbSnapshot;
import com.note.cms.data.model.TbSnapshotFace;
import com.note.cms.data.vo.InputSnapshotFaceVo;
import com.note.cms.data.vo.InputSnapshotVo;
import com.note.cms.data.vo.OutputSnapshotFaceVo;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.data.vo.searchparam.ParamSnapshot;
import com.note.common.utils.HException;

import java.util.List;

/**
 * 
 * @author SnapshotFace 相关的service
 *
 */
public interface SnapshotFaceService extends BaseService{

    public List<OutputSnapshotFaceVo> queryListBySnapshotId(int snapshotId);
    public OutputSnapshotFaceVo queryOutputSnapshotFaceVoBySnapshotId(int snapshotId);

}
