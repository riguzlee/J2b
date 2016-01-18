package com.riguz.jb.web.controller;

import com.riguz.jb.web.ext.ajax.pagination.impl.JqGridAdapter;

public class IndexController extends AbstractJsonController {
    public IndexController() {
        super();
        this.dataGridAdapter = new JqGridAdapter(this);
    }

    public void index() {
        this.render("/html/pages/index/index.html");
    }
}
