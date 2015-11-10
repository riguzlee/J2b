package com.riguz.j2b.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.riguz.j2b.config.DefaultSettings;

import cn.dreampie.captcha.CaptchaRender;

public class RandomPictureService {
    static Prop p = PropKit.use("jfinal.properties");

    public CaptchaRender getRandomPictureRender() {
        CaptchaRender captcha = new CaptchaRender();
        captcha.setCaptchaName(DefaultSettings.RANDOM_PIC_KEY);
        // 干扰线数量 默认0
        captcha.setLineNum(p.getInt("captcha.lineNum", 1));
        // 噪点数量 默认50
        captcha.setArtifactNum(p.getInt("captcha.artifactNum", 30));
        // 使用字符 去掉0和o 避免难以确认
        captcha.setCode(p.get("captcha.chars", "123456789"));
        // 验证码在session里的名字 默认 captcha,创建时间为：名字_time
        // captcha.setCaptchaName("captcha");
        // 验证码颜色 默认黑色
        // captcha.setDrawColor(new Color(255,0,0));
        // 背景干扰物颜色 默认灰
        // captcha.setDrawBgColor(new Color(0,0,0));
        // 背景色+透明度 前三位数字是rgb色，第四个数字是透明度 默认透明
        // captcha.setBgColor(new Color(225, 225, 0, 100));
        // 滤镜特效 默认随机特效 //曲面Curves //大理石纹Marble //弯折Double //颤动Wobble //扩散Diffuse
        String type = p.get("captcha.filter", "Curves");
        if ("Marble".equals(type))
            captcha.setFilter(CaptchaRender.FilterFactory.Marble);
        else if ("Double".equals(type))
            captcha.setFilter(CaptchaRender.FilterFactory.Double);
        else if ("Diffuse".equals(type))
            captcha.setFilter(CaptchaRender.FilterFactory.Diffuse);
        else
            captcha.setFilter(CaptchaRender.FilterFactory.Curves);

        // 随机色 默认黑验证码 灰背景元素
        captcha.setRandomColor(true);
        return captcha;
    }
}
