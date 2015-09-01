package com.riguz.j2b.model;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;

public class Url extends Model<Url> {
    private static final long serialVersionUID = -7676027466630170568L;
    private static Logger     logger           = Logger.getLogger(Url.class.getName());
    public static final Url   dao              = new Url();

    public Url() {
        super();
    }
}
