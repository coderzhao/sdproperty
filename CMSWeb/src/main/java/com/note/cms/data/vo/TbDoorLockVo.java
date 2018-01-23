package com.note.cms.data.vo;

import com.note.cms.data.model.TbDoorLock;
import com.note.cms.data.model.TbIpc;

/**
 * Created by xuxinjian on 17/7/6.
 */
public class TbDoorLockVo extends TbDoorLock {
    private String doorName;

    public TbDoorLockVo(TbDoorLock input){
        this.setCreateTime(input.getCreateTime());
        this.setIp(input.getIp());
        this.setDoorId(input.getDoorId());
        this.setId(input.getId());
        this.setName(input.getName());
        this.setPort(input.getPort());
        this.setLine(input.getLine());
        this.setTime(input.getTime());
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }
}
