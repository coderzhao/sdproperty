package com.note.cms.data.sdk;

import com.note.cms.data.model.TbIpc;

import java.io.Serializable;

/**
 * Created by xuxinjian on 17/7/6.
 */
public class MSdkCamera implements Serializable {
    private String detector;
    private String id;
    private String meta;
    private String url;

    public String getDetector() {
        return detector;
    }

    public void setDetector(String detector) {
        this.detector = detector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
