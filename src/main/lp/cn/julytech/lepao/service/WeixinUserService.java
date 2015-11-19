package cn.julytech.lepao.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;
import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.service.CurdService;

import cn.julytech.lepao.config.ConfigFactory;
import cn.julytech.lepao.entity.Img;
import cn.julytech.lepao.entity.MatchRecord;
import cn.julytech.lepao.entity.Sig;
import cn.julytech.lepao.entity.WeixinUser;
import cn.julytech.lepao.validator.RegisterValidator;

public class WeixinUserService extends CurdService<WeixinUser> {
    private static Logger logger = Logger.getLogger(WeixinUserService.class.getName());

    public WeixinUser getUsrByOpenId(String openId) {
        return WeixinUser.dao.findFirst("SELECT * FROM USR WHERE OPEN_ID=?", openId);
    }

    public List<WeixinUser> search(String number) {
        return WeixinUser.dao.find("SELECT * FROM USR WHERE LEPAO_NUMBER LIKE ?", "%" + number + "%");

    }

    public Page<WeixinUser> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT USR_ID, REAL_NAME, GENDER, OPEN_ID, HOBBY, LEPAO_NUMBER, AGE, COMPANY";
        String where = "FROM USR";
        return this.getList(WeixinUser.dao, pageNumber, pageSize, select, where, args);
    }

    public WeixinUser get(String id) {
        return this.get(WeixinUser.dao, id, "USR_ID", "REAL_NAME", "GENDER", "OPEN_ID", "HOBBY", "LEPAO_NUMBER", "AGE", "COMPANY");
    }

    public WeixinUser getByNumber(String number) {
        number = number.trim();
        return WeixinUser.dao.findFirst("SELECT * FROM USR WHERE LEPAO_NUMBER=?", number);
    }

    public Sig getSignRecord(String openId) {
        return Sig.dao.findFirst("SELECT * FROM sig WHERE SIGN_USER_OPENID=?", openId);
    }

    public long getUserCount(int gender) {
        Record r = Db.use("db").findFirst("SELECT COUNT(*) AS CNT FROM USR WHERE GENDER=?", gender);
        return r.getLong("CNT");
    }

    public boolean doSign(WeixinUser user, String msg, String activaty) {
        Sig sig = new Sig();
        sig.set("ACTIVATY", activaty);
        sig.set("SIGN_USER_OPENID", user.getStr("OPEN_ID"));
        sig.set("SIGN_TIME", new Date());
        sig.set("MESSAGE", msg);
        sig.set("STATUS", 0);
        return sig.save();
    }

    public WeixinUser match(int gender, String hobby, String toUserId) {
        String sql = "SELECT * FROM USR WHERE GENDER=? AND USR_ID <> ? AND OPEN_ID IS NOT NULL AND OPEN_ID<>'' ";
        List<Object> params = new ArrayList<Object>();
        int herGender = gender == 1 ? 0 : 1;
        params.add(herGender);
        params.add(toUserId);
        String queryHobby = " AND (1<>1 ";
        if (!Strings.isNullOrEmpty(hobby)) {
            String[] temp = hobby.split(",");
            for (String h : temp) {
                queryHobby += " OR HOBBY LIKE ?";
                params.add("%" + h + "%");
            }
        }
        queryHobby += ")";
        sql += queryHobby;
        sql += " ORDER BY BE_MATCHED_COUNT ASC";
        return WeixinUser.dao.findFirst(sql, params.toArray());
    }

    public WeixinUser match(int gender, String toUserId) {
        String sql = "SELECT * FROM USR WHERE GENDER=? AND USR_ID <> ? AND OPEN_ID IS NOT NULL AND OPEN_ID<>'' ORDER BY BE_MATCHED_COUNT ASC";
        int herGender = gender == 1 ? 0 : 1;
        return WeixinUser.dao.findFirst(sql, herGender, toUserId);
    }

    public String doMatch(final WeixinUser user) {
        int gender = user.getInt("GENDER");
        String hobby = user.getStr("HOBBY");

        WeixinUser tmpUsr = this.match(gender, hobby, user.getStr("USR_ID"));
        if (tmpUsr == null) {
            logger.debug("no same hobby");
            tmpUsr = this.match(gender, user.getStr("USR_ID"));
        }
        if (tmpUsr == null) {
            logger.warn("no match found " + hobby);
            return null;
        }
        final WeixinUser she = tmpUsr;
        final MatchRecord r = new MatchRecord();
        r.set("FROM_USER_OPEN_ID", user.getStr("OPEN_ID"));
        r.set("TO_USER_OPEN_ID", she.getStr("OPEN_ID"));
        r.set("MATCH_TIME", new Date());
        Integer matchedCount = user.getInt("MATCHED_COUNT");
        if (matchedCount == null)
            matchedCount = 0;
        user.set("MATCHED_COUNT", matchedCount + 1);
        Integer beMatchedCount = she.getInt("BE_MATCHED_COUNT");
        if (beMatchedCount == null)
            beMatchedCount = 0;
        user.set("BE_MATCHED_COUNT", beMatchedCount + 1);
        boolean succeed = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return r.save() && user.update() && she.update();
            }
        });
        if (succeed)
            return she.getStr(she.getPrimaryKeyName());
        logger.error("Save failed");
        return null;
    }

    public WeixinUser getMatch(String myOpenId) {
        String sql = "SELECT * FROM match_record "
                + "LEFT JOIN usr ON usr.OPEN_ID=match_record.TO_USER_OPEN_ID "
                + "WHERE FROM_USER_OPEN_ID=? "
                + "ORDER BY MATCH_TIME DESC";
        return WeixinUser.dao.findFirst(sql, myOpenId);
    }

    @Before(RegisterValidator.class)
    public boolean doRegister(WeixinUser model) {
        WeixinUser user = this.getByNumber(model.getStr("LEPAO_NUMBER"));
        if (user == null) {
            this.errorMsg = "哎哟，这个运动员好像不存在:" + model.getStr("LEPAO_NUMBER");
            return false;
        }
        String realName = user.get("REAL_NAME");
        if (!model.getStr("REAL_NAME").equals(realName)) {
            String tmp = realName;
            if (tmp.length() <= 2)
                tmp = tmp.charAt(0) + "*";
            else {
                tmp = tmp.charAt(0) + "*" + tmp.substring(2);
            }
            this.errorMsg = "偷偷告诉你，号牌为" + model.getStr("LEPAO_NUMBER") + "的姓名为：" + tmp;
            return false;
        }
        user.set("OPEN_ID", model.getStr("OPEN_ID"));
        user.set("GENDER", model.getInt("GENDER"));
        user.set("MOBILE", model.getStr("MOBILE"));
        user.set("HOBBY", model.getStr("HOBBY"));
        user.set("AGE", model.getInt("AGE"));
        user.set("REGISTER_DATE", new Date());

        ApiConfigKit.setThreadLocalApiConfig(ConfigFactory.getConfig("anything"));
        ApiResult info = UserApi.getUserInfo(model.getStr("OPEN_ID"));
        String img = info.get("headimgurl");
        user.set("PORTRAIT", img);
        return user.update();
    }

    public boolean doShareImage(String openId, String url, String thumbUrl, String saySomething) {
        Img img = new Img();
        img.set("UPLOAD_USER_OPENID", openId);
        img.set("THUMB_PATH", thumbUrl);
        img.set("UPLOAD_TIME", new Date());
        img.set("FILE_PATH", url);
        img.set("STATUS", 0);
        img.set("SHARE_STATUS", -1);
        img.set("DESCRIBLE", saySomething);
        return img.save();
    }
}
