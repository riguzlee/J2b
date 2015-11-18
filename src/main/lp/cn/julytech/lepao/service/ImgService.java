package cn.julytech.lepao.service;

import java.util.List;

import com.jfinal.log.Logger;
import com.riguz.j2b.service.CurdService;

import cn.julytech.lepao.entity.Img;
import cn.julytech.lepao.entity.WeixinUser;

public class ImgService extends CurdService<WeixinUser> {
    private static Logger logger = Logger.getLogger(ImgService.class.getName());

    public List<Img> getSharedImages() {
        return Img.dao.find("SELECT * FROM IMG WHERE SHARE_STATUS=?", 1);
    }
}
