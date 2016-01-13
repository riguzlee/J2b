package com.riguz.jb.web.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.riguz.jb.config.Constants;
import com.riguz.jb.web.ext.captcha.PatchcaRender;

public class RandomPictureService {
    static Prop p = PropKit.use("jfinal.properties");

    public PatchcaRender getRandomPictureRender() {
        PatchcaRender captcha = new PatchcaRender(160, 70);
        captcha.setCaptchaName(Constants.RANDOM_PIC_KEY);
        return captcha;
    }
}
