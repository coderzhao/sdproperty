package com.note.cms.tools;

import com.note.cms.data.model.KConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by xuxinjian on 16/8/25.
 * http工具类
 */
public class ZtHttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZtHttpUtil.class);
    /**
     *
     * @param url 请求的地址
     * @param body 请求的消息题，http的body部分
     * @param method HttpMethod
     */
    public static ZtHttpUtilResult sendHttpRequest(String url, String body, HttpMethod method){
        InputStreamReader isr = null;
        try {
            logger.info(url + "  ==body:" + body);
            ZtHttpUtilResult result = new ZtHttpUtilResult();

            URI uri = new URI(url);
            SimpleClientHttpRequestFactory schr = new SimpleClientHttpRequestFactory();
            ClientHttpRequest chr = schr.createRequest(uri, method);
            chr.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");

            chr.getBody().write(body.getBytes());//body中设置请求参数
            ClientHttpResponse res = chr.execute();
            InputStream is = res.getBody(); //获得返回数据,注意这里是个流
            isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String str = "";
            while((str = br.readLine())!=null){
                System.out.println(str);//获得页面内容或返回内容
                result.content += str;
            }
            result.code = KConst.RESULT_OK;
            logger.info("http request:" + url + "=content=" + result.content);
            return result;

        } catch (URISyntaxException e1) {
            // TODO Auto-generated catch block
            logger.error(e1.getMessage());
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {
            if (isr != null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
