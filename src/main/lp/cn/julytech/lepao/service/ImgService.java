package cn.julytech.lepao.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.MediaFile;
import com.riguz.j2b.config.ConfigManager;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.service.CurdService;
import com.riguz.j2b.service.IdentityService;

import cn.julytech.lepao.entity.Img;
import cn.julytech.lepao.weixin.MediaApi;
import net.coobird.thumbnailator.Thumbnails;

public class ImgService extends CurdService<Img> {
    private static Logger logger     = Logger.getLogger(ImgService.class.getName());

    static final String   uploadPath = ConfigManager.getConfig("upload.path", PathKit.getWebRootPath());

    public List<Img> getSharedImages() {
        return Img.dao.find("SELECT * FROM IMG WHERE SHARE_STATUS=? AND STATUS>= 0", 1);
    }

    public Page<Img> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT *";
        String where = "FROM IMG WHERE STATUS >=0 ORDER BY UPLOAD_TIME DESC";

        // FIXME:ORDER BY and ARGUMENTS CONFILCT
        return this.getList(Img.dao, pageNumber, pageSize, select, where, args);
    }

    public String download(ApiConfig config, String mediaId) {
        ApiConfigKit.setThreadLocalApiConfig(config);
        try {
            MediaFile downloadedImg = MediaApi.getMedia(mediaId);
            String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String fileName = dateDir + "/" + IdentityService.getNewToken() + ".jpg";
            logger.info("Downloaded:" + downloadedImg.getFileName() + "/" + fileName);
            byte[] buffer = new byte[2048];
            BufferedInputStream stream = downloadedImg.getFileStream();
            String savePath = uploadPath + "/" + fileName;
            File imageFile = new File(savePath);
            logger.info("the Path is:" + savePath);
            File parentDir = imageFile.getParentFile();
            if (!parentDir.exists())
                parentDir.mkdirs();
            if (!imageFile.exists())
                imageFile.createNewFile();
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(imageFile));
            int bytesRead = 0;
            while (-1 != (bytesRead = stream.read(buffer, 0, buffer.length))) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
            outStream.close();
            stream.close();
            return fileName;
        }
        catch (IOException e) {
            logger.error("Download failed:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String buildThumb(String image) {
        logger.info("Building thumb for:" + image);
        File imageFile = new File(uploadPath + "/" + image);
        String thumbName = image + ".thumb.jpg";
        File thumbFile = new File(uploadPath + "/" + thumbName);
        try {
            Thumbnails.of(imageFile)
                    .size(100, 100)
                    .toFile(thumbFile.getAbsolutePath());
            logger.debug(thumbFile.getAbsolutePath());
            return thumbName;
        }
        catch (IOException e) {
            logger.error("Building failed " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean pass(String id, boolean pass) {
        Img img = Img.dao.findById(id);
        if (img == null)
            return false;
        img.set("SHARE_STATUS", pass ? 1 : -1);
        return img.update();
    }
}
