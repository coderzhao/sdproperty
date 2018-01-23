package com.note.cms.data.vo;

import com.note.cms.data.model.TbIpc;

/**
 * Created by xuxinjian on 17/7/6.
 */
public class TbIpcVo extends TbIpc {
    private String doorName;

    public TbIpcVo(TbIpc input){
        this.setCreateTime(input.getCreateTime());
        this.setAddress(input.getAddress());
        this.setDoorId(input.getDoorId());
        this.setId(input.getId());
        this.setName(input.getName());
        this.setCameraId(input.getCameraId());
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }
}
