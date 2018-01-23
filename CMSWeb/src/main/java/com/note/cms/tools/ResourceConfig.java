package com.note.cms.tools;

/**
 * Created by xuxinjian on 16/9/14.
 * 配置的一些常量放到这里
 */
public class ResourceConfig {
    private static ResourceConfig instance = null;
    private int delayIdle = 10;//闲的时候的设备上报延时
    private int delayBusy = 1;//有用户看的时候的上报延时

    private ResourceConfig(){

    }

    public static ResourceConfig getInstance(){
        if(instance == null){
            instance = new ResourceConfig();
        }
        return instance;
    }

    public int getDelayIdle() {
        return delayIdle;
    }

    public void setDelayIdle(int delayIdle) {
        this.delayIdle = delayIdle;
    }

    public int getDelayBusy() {
        return delayBusy;
    }

    public void setDelayBusy(int delayBusy) {
        this.delayBusy = delayBusy;
    }
}
