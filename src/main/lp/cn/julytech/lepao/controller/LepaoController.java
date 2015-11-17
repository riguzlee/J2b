package cn.julytech.lepao.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.controller.AbstractJsonController;
import com.riguz.j2b.service.IdentityService;

import cn.julytech.lepao.entity.Sig;
import cn.julytech.lepao.entity.WeixinUser;
import cn.julytech.lepao.service.WeixinUserService;

public class LepaoController extends AbstractJsonController {
    private static Logger logger     = Logger.getLogger(LepaoController.class.getName());
    WeixinUserService     usrService = new WeixinUserService();

    public void home() {
        String code = this.getPara("code");
        String openId = this.getPara("open_id");
        if (Strings.isNullOrEmpty(code) && Strings.isNullOrEmpty(openId)) {
            this.renderText("无法获取用户授权。请在微信中打开此网页");
            return;
        }
        // FIXME:
        this.setAttr("open_id", openId);
        WeixinUser usr = this.getCurrentUser();
        if (usr == null) {
            this.redirect("/lepao/license?open_id=" + openId);
            return;
        }
        this.setAttr("user", usr);
        this.render("/pages/lepao/home.html");
    }

    public void register() {
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
        String code = this.getPara("code");
        String openId = this.getPara("open_id");
        if (Strings.isNullOrEmpty(code) && Strings.isNullOrEmpty(openId)) {
            this.renderText("无法获取用户授权。请在微信中打开此网页");
            return;
        }
        WeixinUser user = this.getCurrentUser();
        if (user == null) {
            this.redirect("/lepao/license?open_id=" + openId);
            return;
        }
        this.keepPara();
        this.setAttr("me", user);
        Sig sig = this.usrService.getSignRecord(openId);
        boolean signed = sig != null;
        this.setAttr("sig", sig);
        this.setAttr("signed", signed);
        this.render("/pages/lepao/sign.html");
    }

    public void doSign() {
        String openId = this.getPara("open_id");
        WeixinUser user = this.getCurrentUser();
        if (user == null) {
            ResponseFactory.createErrorRespone(this, "无法获取微信授权！");
            return;
        }
        Sig sig = this.usrService.getSignRecord(openId);
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
        WeixinUser user = this.getCurrentUser();
        if (user == null) {
            ResponseFactory.createErrorRespone(this, "无法获取微信授权");
            return;
        }
        this.render("/pages/lepao/share.html");
    }

    public void match() {
        this.keepPara();
        WeixinUser user = this.getCurrentUser();
        if (user == null) {
            ResponseFactory.createErrorRespone(this, "无法获取微信授权");
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
        WeixinUser user = this.getCurrentUser();
        if (user == null) {
            ResponseFactory.createErrorRespone(this, "无法获取微信授权");
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

    public void zone() {
        this.render("/pages/lepao/zone.html");
    }

    public void portrait() {
        this.keepPara();
        this.render("/pages/lepao/portrait.html");
    }

    public void doUpload() {
        String path = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/temp");
        WeixinUser user = this.getCurrentUser();
        if (user == null) {
            ResponseFactory.createErrorRespone(this, "无法获取微信授权");
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

    private WeixinUser getCurrentUser() {
        String id = this.getPara("open_id");
        if (Strings.isNullOrEmpty(id))
            return null;
        return this.usrService.getUsrByOpenId(id);
    }

    public void search() {
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
}
