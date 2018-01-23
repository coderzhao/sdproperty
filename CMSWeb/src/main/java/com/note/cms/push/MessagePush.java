package com.note.cms.push;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.note.cms.data.vo.app.MPushVO;
import com.note.cms.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 推送工具类，包好短信推送和app推送
 * 6222080200021092559
 */
@Service("MessagePushService")
public class MessagePush {
    private static final Logger logger = LoggerFactory.getLogger(MessagePush.class);

    private final static String appId = "60rseZWehK8KSzq7NKttI9";
    private final static String appKey = "0rndlhwBur6qRFGeAlWqx";
    private final static String masterSecret = "78TVItKD6W67ubY2ncNzvA";

    @Autowired
    private UserService mUserService;
//    @Autowired
//    private TbPushMapper mTbPushMapper;

    /**
     * 发送告警信息透传内容，透传内容在payload
     * 此函数待完善， 应该采用 activemq，防止消息堵塞!!!!!!!!!!!!!!!!!!!!!!!!!!
     * ｛“type”:""｝
     *
     * @param title
     * @param content 6214857552045173
     * @param userIds
     */
    public void pushMessageAlert(Integer alertId, String title, String content, List<Integer> userIds) {

        try {
            //组合透传内容
            MPushVO vo = new MPushVO();
            vo.setTitle(title);
            vo.setType(MPushVO.TYPE_ALERT);
            vo.setContent(content);
            vo.setHasRead(Boolean.FALSE);
            vo.setId(alertId);
            vo.setReportTime(new Date());

            JSONObject obj = (JSONObject)JSON.toJSON(vo);
            String pushContent = obj.toJSONString();

            //发送
//            TbPushExample example = new TbPushExample();
//            TbPushExample.Criteria c = example.createCriteria();
//            c.andUserIdIn(userIds);
//            List<TbPush> listPush = mTbPushMapper.selectByExample(example);
//            if(listPush != null){
//                for(TbPush push : listPush){
//                    GeTuiAppPush.pushMessage(title, pushContent, push.getGetuiId());
//                }
//            }

        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }
}

