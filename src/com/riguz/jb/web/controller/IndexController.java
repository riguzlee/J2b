package com.riguz.jb.web.controller;

public class IndexController extends AbstractJsonController {
    public void index() {
        this.render("/html/pages/index/index.html");
    }
}
