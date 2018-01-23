package com.note.cms.data.vo.app;

import java.io.Serializable;
import java.util.Date;

/**
 *消息推送对象
 */

public class MPushVO  implements Serializable {

    public final static int TYPE_ALERT = 1;//报警推送，此时id为 alertId

    private int type;
    private int id;//如果type ＝ TYPE_ALERT, 则此id表示 tb_alert_record 表中的id
    private String title;//消息标题
    private String content;//显示在消息列表中的第二行的
    private boolean hasRead;//是否已读
    private Date reportTime;

    public MPushVO(){
        hasRead = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }
}
