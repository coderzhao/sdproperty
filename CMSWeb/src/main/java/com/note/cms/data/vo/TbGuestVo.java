package com.note.cms.data.vo;

import com.note.cms.common.NUtil;
import com.note.cms.data.model.TbGuest;
import com.note.cms.data.model.TbIpc;
import com.note.common.utils.HTextUtils;

/**
 * Created by xuxinjian on 17/7/6.
 */
public class TbGuestVo extends TbGuest {
    private String guestRoleName; //访客角色名称
    private String AccessGuestName; //被访者名称
    private String photo;

    public TbGuestVo(TbGuest input){
        this.setCreateTime(input.getCreateTime());
        this.setId(input.getId());
        this.setName(input.getName());
        this.setImage(input.getImage());
        this.setCode(input.getCode());
        this.setGuestRoleId(input.getGuestRoleId());
        this.setFirstTime(input.getFirstTime());
        this.setLastTime(input.getLastTime());
        this.setCardNo(input.getCardNo());
        this.setAccessGuestId(input.getAccessGuestId());
        this.setAge(input.getAge());
        this.setGender(input.getGender());
        this.setRoom(input.getRoom());
        this.setCount(input.getCount());
        this.setLockStartTime(input.getLockStartTime());
        this.setLockEndTime(input.getLockEndTime());

//        this.setPhoto(NUtil.getUrlFromPhoto(input.getImage()));
    }

    public String getGuestRoleName() {
        return guestRoleName;
    }

    public void setGuestRoleName(String guestRoleName) {
        this.guestRoleName = guestRoleName;
    }

    public String getAccessGuestName() {
        return AccessGuestName;
    }

    public void setAccessGuestName(String accessGuestName) {
        AccessGuestName = accessGuestName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
