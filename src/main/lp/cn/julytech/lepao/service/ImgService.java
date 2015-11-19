package cn.julytech.lepao.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.service.CurdService;
import com.riguz.j2b.service.IdentityService;

import cn.julytech.lepao.entity.Img;
import cn.julytech.lepao.weixin.Attachment;
import cn.julytech.lepao.weixin.HttpKit;
import net.coobird.thumbnailator.Thumbnails;

public class ImgService extends CurdService<Img> {
    private static Logger logger = Logger.getLogger(ImgService.class.getName());

    public List<Img> getSharedImages() {
        return Img.dao.find("SELECT * FROM IMG WHERE SHARE_STATUS=?", 1);
    }

    public Page<Img> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT *";
        String where = "FROM IMG ORDER BY UPLOAD_TIME DESC";
        return this.getList(Img.dao, pageNumber, pageSize, select, where, args);
    }

    public String download(String mediaId) {
        ApiConfig config = ApiConfigKit.getApiConfig();
        try {
            Attachment image = HttpKit.downloadFromWx(config.getToken(), mediaId);
            String path = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String fileName = IdentityService.getNewToken() + ".jpg";
            byte[] buffer = new byte[2048];
            BufferedInputStream stream = image.getFileStream();
            File imageFile = new File(PathKit.getWebRootPath() + "/" + fileName);
            if (!imageFile.exists())
                imageFile.createNewFile();
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(imageFile));
            int bytesRead = 0;
            while (-1 != (bytesRead = stream.read(buffer, 0, buffer.length))) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.close();
            stream.close();
            return fileName;
        }
        catch (ExecutionException | InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String buildThumb(String image) {
        File imageFile = new File(PathKit.getWebRootPath() + "/" + image);
        String thumbName = image + ".thumb.jpg";
        File thumbFile = new File(PathKit.getWebRootPath() + "/" + thumbName);
        try {
            Thumbnails.of(imageFile)
                    .size(100, 100)
                    .toFile(thumbFile.getAbsolutePath());
            logger.debug(thumbFile.getAbsolutePath());
        }
        catch (IOException e) {
            e.printStackTrace();
            return thumbName;
        }
        return null;
    }
}
