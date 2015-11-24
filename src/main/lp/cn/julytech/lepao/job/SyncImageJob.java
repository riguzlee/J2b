package cn.julytech.lepao.job;

import com.google.common.base.Strings;
import com.jfinal.log.Logger;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;

import cn.julytech.lepao.config.ConfigFactory;
import cn.julytech.lepao.service.ImgService;
import cn.julytech.lepao.service.WeixinUserService;

public class SyncImageJob implements Runnable {
    private static Logger          logger      = Logger.getLogger(SyncImageJob.class.getName());
    static final ImgService        imgService  = new ImgService();
    static final WeixinUserService userService = new WeixinUserService();
    static ApiConfig               config      = ConfigFactory.getConfig("anything");

    InImageMsg                     imgMsg      = null;

    public SyncImageJob(InImageMsg imgMsg) {
        super();
        this.imgMsg = imgMsg;
    }

    @Override
    public void run() {
        logger.info("=>Queue:Handling InImgMsg" + imgMsg.getMediaId());
        try {
            String fileName = imgService.download(config, imgMsg.getMediaId());
            logger.info("Downloaded into :" + fileName);
            if (!Strings.isNullOrEmpty(fileName)) {
                String thumbName = imgService.buildThumb(fileName);
                userService.doShareImage(imgMsg.getFromUserName(), fileName, thumbName, "");
                logger.info("Image success saved");
            }
        }
        catch (Exception ex) {
            logger.error("Download failed");
            ex.printStackTrace();
        }
    }

}
