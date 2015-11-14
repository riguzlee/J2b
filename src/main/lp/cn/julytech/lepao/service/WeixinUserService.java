package cn.julytech.lepao.service;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.service.CurdService;

import cn.julytech.lepao.entity.WeixinUser;
import cn.julytech.lepao.validator.RegisterValidator;

public class WeixinUserService extends CurdService<WeixinUser> {
    public WeixinUser getUsrByOpenId(String openId) {
        return WeixinUser.dao.findFirst("select * from usr where open_id=?", openId);
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
        return user.update();
    }
}
