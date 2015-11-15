package cn.julytech.lepao.config;

import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;

public final class ConfigFactory {
    public static ApiConfig getConfig(String key) {
        ApiConfig ac = new ApiConfig();

        // 配置微信 API 相关常量
        PropKit.use("wechat.properties", "UTF-8");
        String token = PropKit.use("wechat.properties").get("token");
        String appId = PropKit.use("wechat.properties").get("appId");
        String appSecret = PropKit.use("wechat.properties").get("appSecret");
        ac.setToken(token);
        ac.setAppId(appId);
        ac.setAppSecret(appSecret);
        ac.setEncryptMessage(PropKit.use("wechat.properties").getBoolean("encryptMessage", false));
        ac.setEncodingAesKey(PropKit.use("wechat.properties").get("encodingAesKey", "No aes key"));
        return ac;
    }
}
