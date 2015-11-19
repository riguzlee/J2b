package cn.julytech.lepao.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.controller.AbstractJsonController;
import com.riguz.j2b.service.IdentityService;
import com.riguz.j2b.util.JsonUtil;

import cn.julytech.lepao.entity.Img;
import cn.julytech.lepao.entity.Sig;
import cn.julytech.lepao.entity.WeixinUser;
import cn.julytech.lepao.service.ImgService;
import cn.julytech.lepao.service.WeixinUserService;
import cn.julytech.lepao.weixin.OAuthKit;

public class LepaoController extends AbstractJsonController {
    private static Logger logger     = Logger.getLogger(LepaoController.class.getName());
    WeixinUserService     usrService = new WeixinUserService();
    ImgService            imgService = new ImgService();

    public void home() {
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }
        this.setAttr("user", user);
        this.render("/pages/lepao/home.html");
    }

    public void register() {
        String openId = this.getOpenId();
        if (Strings.isNullOrEmpty(openId)) {
            this.renderText("无法获取微信授权，请确保在微信中打开链接");
            return;
        }
        this.keepPara();
        this.render("/pages/lepao/register.html");
    }

    public void doRegister() {
        WeixinUser user = this.getModel(WeixinUser.class, "user");
        user.set("GENDER", "on".equals(this.getPara("GENDER")) ? 1 : 0);
        if (this.usrService.doRegister(user))
            ResponseFactory.createSuccessResponse(this);
        else {
            ResponseFactory.createErrorRespone(this, this.usrService.getErrorMsg());
        }
    }

    public void sign() {
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }
        this.keepPara();
        this.setAttr("me", user);
        Sig sig = this.usrService.getSignRecord(this.getOpenId());
        boolean signed = sig != null;
        this.setAttr("sig", sig);
        this.setAttr("signed", signed);
        this.render("/pages/lepao/sign.html");
    }

    public void doSign() {
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }
        Sig sig = this.usrService.getSignRecord(this.getOpenId());
        if (sig != null) {
            ResponseFactory.createErrorRespone(this, "您已经签到过啦！");
            return;
        }
        String msg = this.getPara("saySomething");
        String activaty = this.getPara("activaty");
        boolean result = this.usrService.doSign(user, msg, activaty);
        if (result == true) {
            ResponseFactory.createSuccessResponse(this);
        }
        else
            ResponseFactory.createErrorRespone(this, "签到失败，请重试");

    }

    public void share() {
        this.keepPara();
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }
        this.render("/pages/lepao/share.html");
    }

    public void match() {
        this.keepPara();
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }
        String type = this.getPara("type");
        if ("welcome".equals(type)) {
            this.render("/pages/lepao/matchWelcome.html");
            return;
        }
        if ("ing".equals(type)) {
            this.render("/pages/lepao/matchIng.html");
            return;
        }
        WeixinUser myMatch = this.usrService.getMatch(user.getStr("open_id"));
        this.setAttr("myMatch", myMatch);
        long maleCount = this.usrService.getUserCount(1);
        long femaleCount = this.usrService.getUserCount(0);
        this.setAttr("males", maleCount);
        this.setAttr("females", femaleCount);
        this.render("/pages/lepao/match.html");
    }

    public synchronized void doMatch() {
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }
        String result = this.usrService.doMatch(user);
        if (result != null) {
            ResponseFactory.createSuccessResponse(this, result);
        }
        else {
            logger.error("Matched failed");
            ResponseFactory.createErrorRespone(this, "匹配失败，请稍后再试试吧！");
            return;
        }
    }

    public void shake() {
        String openId = this.getOpenId();
        if (Strings.isNullOrEmpty(openId)) {
            this.renderText("无法获取微信授权，请确保在微信中打开链接");
            return;
        }
        this.keepPara();
        this.render("/pages/lepao/shake.html");
    }

    public void zone() {
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }
        List<Img> imgs = this.imgService.getSharedImages();
        this.setAttr("imgs", imgs);
        this.render("/pages/lepao/zone.html");
    }

    public void portrait() {
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }
        this.keepPara();
        this.render("/pages/lepao/portrait.html");
    }

    public void doShare() {
        String path = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/temp");
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }

        File source = file.getFile();
        String fileName = file.getFileName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String prefix;
        if (".png".equals(extension) || ".jpg".equals(extension)) {
            prefix = "img";
            fileName = IdentityService.getNewToken() + extension;
            try {
                String imgUrl = "/upload/" + path + "/" + fileName;
                File newFile = new File(PathKit.getWebRootPath() + imgUrl);
                FileUtils.copyFile(source, newFile);

                String thumb = this.imgService.buildThumb(imgUrl);

                if (this.usrService.doShareImage(user.getStr("OPEN_ID"), imgUrl, thumb, "")) {
                    this.renderText("分享成功！请等待管理员审核！您可以继续分享");
                    return;
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.renderText("分享失败，请重试");
    }

    public void doUpload() {
        String path = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/temp");
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }

        File source = file.getFile();
        String fileName = file.getFileName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String prefix;
        if (".png".equals(extension) || ".jpg".equals(extension)) {
            prefix = "img";
            fileName = IdentityService.getNewToken() + extension;
            try {
                String imgUrl = "/upload/" + path + "/" + fileName;
                File newFile = new File(PathKit.getWebRootPath() + imgUrl);
                FileUtils.copyFile(source, newFile);
                user.set("PORTRAIT", imgUrl);
                if (!user.update()) {
                    ResponseFactory.createErrorRespone(this, "提交数据库失败");
                    return;
                }
                this.redirect("/lepao/home?open_id=" + this.getPara("open_id"));
                return;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        ResponseFactory.createErrorRespone(this);
    }

    public void license() {
        this.keepPara();
        this.render("/pages/lepao/license.html");
    }

    public void doAcceptLicense() {
        this.redirect("/lepao/register?open_id=" + this.getPara("open_id"));
    }

    public void search() {
        WeixinUser user = this.validateBindedUser();
        if (user == null) {
            this.redirectToLicencePage();
            return;
        }

        String param = this.getPara("number");
        if (Strings.isNullOrEmpty(param)) {
            this.setAttr("users", new ArrayList<WeixinUser>());
        }
        else {
            List<WeixinUser> users = this.usrService.search(param);
            this.keepPara();
            this.setAttr("users", users);
        }

        this.render("/pages/lepao/search.html");
    }

    public static final Map<String, String> codeMap = new HashMap<String, String>();

    private String getOpenId() {
        String id = this.getPara("open_id");
        String code = this.getPara("code");
        if (!Strings.isNullOrEmpty(code)) {
            // 诡异的微信请求，请求会发生两次 refer:http://www.tuicool.com/articles/bi2YRj
            // 尝试从已经保存的内存取出openId
            if (codeMap.containsKey(code)) {
                logger.warn("Got openId from cache.:" + code);
                return codeMap.get(code);
            }
            String userInfo = OAuthKit.getAccessToken(code);
            if (userInfo.contains("errcode")) {
                logger.error("Failed to get user openId via OAuth2.0:" + userInfo);
                return null;
            }
            Map<String, Object> map = JsonUtil.toMap(userInfo);
            String openId = (String) map.get("openid");
            logger.info("Put code/openID =>" + code + "/" + openId);
            codeMap.put(code, openId);
            return openId;
        }
        return id;
    }

    private void redirectToLicencePage() {
        String openId = this.getOpenId();
        if (Strings.isNullOrEmpty(openId)) {
            this.renderText("无法获取微信授权，请确保在微信中打开链接");
            return;
        }
        this.redirect("/lepao/license?open_id=" + openId);
    }

    private WeixinUser validateBindedUser() {
        String openId = this.getOpenId();
        if (Strings.isNullOrEmpty(openId))
            return null;
        this.setAttr("open_id", openId);
        WeixinUser user = this.usrService.getUsrByOpenId(openId);
        return user;
    }
}
