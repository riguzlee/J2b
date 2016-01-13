package com.riguz.jb.web.ext.captcha;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import com.jfinal.render.Render;
import com.riguz.jb.util.EncryptUtil;
import com.riguz.jb.web.ext.captcha.background.BackgroundFactory;
import com.riguz.jb.web.ext.captcha.color.ColorFactory;
import com.riguz.jb.web.ext.captcha.color.GradientColorFactory;
import com.riguz.jb.web.ext.captcha.filter.FilterFactory;
import com.riguz.jb.web.ext.captcha.filter.predefined.CurvesRippleFilterFactory;
import com.riguz.jb.web.ext.captcha.font.RandomFontFactory;
import com.riguz.jb.web.ext.captcha.service.ConfigurableCaptchaService;
import com.riguz.jb.web.ext.captcha.text.renderer.RandomYBestFitTextRenderer;
import com.riguz.jb.web.ext.captcha.text.renderer.TextRenderer;
import com.riguz.jb.web.ext.captcha.utils.encoder.EncoderHelper;
import com.riguz.jb.web.ext.captcha.word.AdaptiveRandomWordFactory;
import com.riguz.jb.web.ext.captcha.word.RandomWordFactory;

public class PatchcaRender extends Render {
    ConfigurableCaptchaService configurableCaptchaService = new ConfigurableCaptchaService();

    int                        width                      = 160;
    int                        height                     = 70;

    String                     captchaName                = "captcha";

    ColorFactory               colorFactory               = new GradientColorFactory();
    RandomFontFactory          fontFactory                = new RandomFontFactory();
    RandomWordFactory          wordFactory                = new AdaptiveRandomWordFactory();
    BackgroundFactory          backgroundFactory          = new SimpleBackgroundFactory();
    FilterFactory              filterFactory              = new CurvesRippleFilterFactory();

    TextRenderer               textRenderer               = new RandomYBestFitTextRenderer();
    String                     chars                      = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";

    public PatchcaRender(int width, int height) {

        this.width = width;
        this.height = height;

        this.wordFactory.setCharacters(this.chars);

        this.wordFactory.setMaxLength(4);
        this.wordFactory.setMinLength(4);
        // this.configurableCaptchaService.setColorFactory(this.colorFactory);
        this.configurableCaptchaService.setBackgroundFactory(this.backgroundFactory);
        // this.configurableCaptchaService.setFontFactory(this.fontFactory);
        this.configurableCaptchaService.setWordFactory(this.wordFactory);
        // this.configurableCaptchaService.setFilterFactory(this.filterFactory);
        // this.configurableCaptchaService.setTextRenderer(this.textRenderer);

        this.configurableCaptchaService.setWidth(this.width);
        this.configurableCaptchaService.setHeight(this.height);

    }

    public ConfigurableCaptchaService getConfigurableCaptchaService() {
        return configurableCaptchaService;
    }

    public void setConfigurableCaptchaService(ConfigurableCaptchaService configurableCaptchaService) {
        this.configurableCaptchaService = configurableCaptchaService;
    }

    public String getCaptchaName() {
        return captchaName;
    }

    public void setCaptchaName(String captchaName) {
        this.captchaName = captchaName;
    }

    @Override
    public void render() {
        HttpSession session = request.getSession();
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            String captchaCode = EncoderHelper.getChallangeAndWriteImage(this.configurableCaptchaService, "png", outputStream);
            session.setAttribute(captchaName, EncryptUtil.encrypt("SHA-1", captchaCode.toLowerCase()));
            session.setAttribute(captchaName + "_time", new Date().getTime());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (outputStream != null)
                try {
                    outputStream.flush();
                    outputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
