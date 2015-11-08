package com.riguz.j2b.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.menu.MenuFactory;
import com.riguz.j2b.model.bean.MenuItem;
import com.riguz.j2b.model.entity.Role;
import com.riguz.j2b.model.entity.User;
import com.riguz.j2b.service.SecurityService;
import com.riguz.j2b.util.MailKit;
import com.riguz.j2b.validator.RandkeyValidator;

import cn.dreampie.captcha.CaptchaRender;

public class IndexController extends AbstractJsonController {
	private static Logger logger = Logger.getLogger(IndexController.class.getName());
	SecurityService securityService = new SecurityService(this);

	/**
	 * 系统首页
	 */
	public void index() {
		this.render("/pages/index/index.html");
	}

	/**
	 * 登录页面
	 */
	public void login() {
		this.render("/pages/index/login.html");
	}

	/**
	 * 用户登录
	 */
	@Before(RandkeyValidator.class)
	public void doLogin() {
		logger.info("User trying to login...");
		try {
			if (this.securityService.doLoginAction()) {
				User u = SecurityService.getLoginUser();
				List<Role> roles = u.getRoles();
				if (roles != null && roles.size() > 0) {
					Role role = roles.get(0);
					String roleIdent = role.getStr("ident");
					List<MenuItem> menu = MenuFactory.getRoleMenu(roleIdent);
					// TODO:support muti roles
					Map<String, Object> vars = new HashMap<String, Object>();
					vars.put("roleMenu", menu);
					vars.put("loginUser", u.get("login_name"));
					BeetlRenderFactory.groupTemplate.setSharedVars(vars);
				}

				ResponseFactory.createSuccessResponse(this);
				return;
			}
		} catch (Exception ex) {
		}
		ResponseFactory.createErrorRespone(this, "登录失败");
	}

	/**
	 * 用户登出
	 */
	public void doLogout() {
		this.securityService.doLogoutAction();
		this.redirect("/login");
	}

	/**
	 * 发送重置邮件事件
	 */
	public void sendReset() {
		String randomStr = this.getPara("rand");
		String email = this.getPara("email");
		logger.info("User trying to reset his password:" + email + "/" + randomStr);
		User user = User.dao.findByEmail(email);
		if (user == null) {
			ResponseFactory.createErrorRespone(this, "系统中未找到此用户：" + email);
			return;
		}
		// 生成一个随机字符串存储到用户的activate_code中
		String code = this.securityService.generateActiveCode(user);
		if (code == null) {
			ResponseFactory.createErrorRespone(this, "系统内部错误，请稍后再试");
			return;
		}
		// 发送重置密码邮件给用户
		String subject = PropKit.use("jfinal.properties").get("email.reset.subject");
		String body = MessageFormat.format(PropKit.use("jfinal.properties").get("email.reset.body"), code);
		MailKit.sendMail(email, subject, body);
		logger.info("Mail(Reset) sent to (" + email + "):" + body);

		ResponseFactory.createSuccessResponse(this);

	}

	public void reset() {
		this.render("/pages/index/reset.html");
	}

	@Before({ RandkeyValidator.class })
	public void doReset() {
		String token = this.getPara("token");
		String newPasswd = this.getPara("new-password");
		logger.info("Reset passwd：" + token);
		if (this.securityService.resetPasswd(token, newPasswd)) {
			ResponseFactory.createSuccessResponse(this);
		} else
			ResponseFactory.createErrorRespone(this, "系统错误，重置密码失败");
	}

	/**
	 * 获取验证码，通过RandkeyValidator验证验证码是否正确
	 */
	@Clear
	public void randpic() {
		int width = 0, height = 0, minnum = 0, maxnum = 0, fontsize = 0, fontmin = 0, fontmax = 0;
		CaptchaRender captcha = new CaptchaRender();
		if (isParaExists("width")) {
			width = getParaToInt("width");
		}
		if (isParaExists("height")) {
			height = getParaToInt("height");
		}
		if (width > 0 && height > 0)
			captcha.setImgSize(width, height);
		if (isParaExists("minnum")) {
			minnum = getParaToInt("minnum");
		}
		if (isParaExists("maxnum")) {
			maxnum = getParaToInt("maxnum");
		}
		if (minnum > 0 && maxnum > 0)
			captcha.setFontNum(minnum, maxnum);
		if (isParaExists("fontsize")) {
			fontsize = getParaToInt("fontsize");
		}
		if (fontsize > 0)
			captcha.setFontSize(fontsize, fontsize);
		captcha.setCaptchaName(DefaultSettings.RANDOM_PIC_KEY);
		// 干扰线数量 默认0
		captcha.setLineNum(2);
		// 噪点数量 默认50
		captcha.setArtifactNum(30);
		// 使用字符 去掉0和o 避免难以确认
		captcha.setCode("123456789");
		// 验证码在session里的名字 默认 captcha,创建时间为：名字_time
		// captcha.setCaptchaName("captcha");
		// 验证码颜色 默认黑色
		// captcha.setDrawColor(new Color(255,0,0));
		// 背景干扰物颜色 默认灰
		// captcha.setDrawBgColor(new Color(0,0,0));
		// 背景色+透明度 前三位数字是rgb色，第四个数字是透明度 默认透明
		// captcha.setBgColor(new Color(225, 225, 0, 100));
		// 滤镜特效 默认随机特效 //曲面Curves //大理石纹Marble //弯折Double //颤动Wobble //扩散Diffuse
		captcha.setFilter(CaptchaRender.FilterFactory.Curves);
		// 随机色 默认黑验证码 灰背景元素
		captcha.setRandomColor(true);
		render(captcha);
	}

}
