package com.note.cms.push;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.dto.GtReq;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 个推 App推送工具类
 */
public class GeTuiAppPush {

    //定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
    private static final Logger logger = LoggerFactory.getLogger(GeTuiAppPush.class);

    private final static String appId = "60rseZWehK8KSzq7NKttI9";
    private final static String appKey = "0rndlhwBur6qRFGeAlWqx";
    private final static String masterSecret = "78TVItKD6W67ubY2ncNzvA";
    private final static String url = "http://sdk.open.api.igexin.com/apiex.htm";
    public static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    /**
     *
     * @param title
     * @param content
     * @param CID 客户端id
     */
    public static void pushMessage(String title, String content, String CID) {

        try {
//            logger.debug("pushMessage():" + content + "==CID:" + CID);
            IGtPush push = new IGtPush(appKey, masterSecret);
            TransmissionTemplate template = createTemplate(title, content);
            SingleMessage message = new SingleMessage();
            message.setOffline(true);
            // 离线有效时间，单位为毫秒，可选
            message.setOfflineExpireTime(24 * 3600 * 1000);
            message.setData(template);
            // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
            message.setPushNetWorkType(0);
            Target target = new Target();
            target.setAppId(appId);
            target.setClientId(CID);
            //target.setAlias(Alias);
            IPushResult ret = null;

            ret = push.pushMessageToSingle(message, target);
            logger.info("pushMessageTo:" + CID + " ==RET:" + ret);
        } catch (RequestException e) {
            e.printStackTrace();
            logger.error("服务器响应异常");
            //ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }

    }

    private static TransmissionTemplate createTemplate(String title, String content) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(content);
        template.setTransmissionType(1);
        return template;
    }

    public static LinkTemplate linkTemplateDemo(String title, String content) {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
        template.setTitle(title);
        template.setText(content);
        // 配置通知栏图标
        template.setLogo("icon.png");
        // 配置通知栏网络图标，填写图标URL地址
        template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        // 设置打开的网址地址
        template.setUrl("http://www.baidu.com");
        return template;
    }
}

