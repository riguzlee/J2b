package cn.julytech.lepao.controller;

import com.riguz.j2b.controller.AbstractJsonController;

public class LepaoController extends AbstractJsonController {
    public void home() {

    }

    public void register() {
        this.render("/pages/lepao/register.html");
    }

    public void sign() {

    }

    public void share() {

    }

    public void portrait() {
        this.render("/pages/lepao/portrait.html");
    }

    public void license() {

    }
}
