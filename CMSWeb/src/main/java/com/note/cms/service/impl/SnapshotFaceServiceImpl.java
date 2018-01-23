package com.note.cms.service.impl;

import com.note.cms.dao.*;
import com.note.cms.data.model.*;
import com.note.cms.data.vo.InputSnapshotFaceVo;
import com.note.cms.data.vo.InputSnapshotVo;
import com.note.cms.data.vo.OutputSnapshotFaceVo;
import com.note.cms.data.vo.OutputSnapshotVo;
import com.note.cms.data.vo.searchparam.ParamSnapshot;
import com.note.cms.service.GuestService;
import com.note.cms.service.SnapshotFaceService;
import com.note.cms.service.SnapshotService;
import com.note.common.Plugin.Page;
import com.note.common.utils.HException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("SnapshotFaceService")
public class SnapshotFaceServiceImpl implements SnapshotFaceService {
	private static final Logger logger = LoggerFactory.getLogger(SnapshotFaceServiceImpl.class);
	@Autowired
	private TbSnapshotFaceMapper mTbSnapshotMapperFaceMapper;

	@Autowired
	private GuestService mGuestService;


	@Override
	public List<OutputSnapshotFaceVo> queryListBySnapshotId(int snapshotId){
		List<OutputSnapshotFaceVo> listVo = new ArrayList<OutputSnapshotFaceVo>();

		TbSnapshotFaceExample example = new TbSnapshotFaceExample();
		TbSnapshotFaceExample.Criteria c = example.createCriteria();
		c.andSnapshotIdEqualTo(snapshotId);
		List<TbSnapshotFace> listData = mTbSnapshotMapperFaceMapper.selectByExample(example);
		if(listData != null && listData.size() > 0){
			for(TbSnapshotFace face : listData){
				OutputSnapshotFaceVo vo = new OutputSnapshotFaceVo();
				vo.setFace(face);
				TbGuest guest = mGuestService.getByCode(face.getGuestCode());
				if(guest != null){
					vo.setGuest(guest);
				}
				listVo.add(vo);
			}
		}

		return listVo;
	}

	public OutputSnapshotFaceVo queryOutputSnapshotFaceVoBySnapshotId(int snapshotId){
		List<OutputSnapshotFaceVo> listVo = queryListBySnapshotId(snapshotId);
		if(listVo != null && listVo.size() > 0){
			return listVo.get(0);
		}
		return null;
	}


}
