package cn.julytech.lepao.weixin;

import java.text.MessageFormat;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;

public class OAuthKit {
    private static Logger logger        = Logger.getLogger(OAuthKit.class.getName());
    static final String   GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    public static String getAccessToken(String code) {
        String url = MessageFormat.format(GET_TOKEN_URL,
                PropKit.use("wechat.properties").get("appId"),
                PropKit.use("wechat.properties").get("appSecret"),
                code);
        logger.info("Getting access_token?code=" + code);
        String jsonResult = HttpKit.get(url);
        return jsonResult;
    }
}
