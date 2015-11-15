package cn.julytech.lepao.controller;

import com.jfinal.log.Logger;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InCustomEvent;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMassEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

import cn.julytech.lepao.config.ConfigFactory;

public class WxController extends MsgController {
    private static Logger logger = Logger.getLogger(WxController.class.getName());
    private ApiConfig     config = ConfigFactory.getConfig("anything");

    @Override
    public ApiConfig getApiConfig() {
        return this.config;
    }

    @Override
    protected void processInCustomEvent(InCustomEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInFollowEvent(InFollowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInImageMsg(InImageMsg arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInLinkMsg(InLinkMsg arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInLocationEvent(InLocationEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInLocationMsg(InLocationMsg arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInMassEvent(InMassEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInMenuEvent(InMenuEvent inMenuEvent) {
        String eventKey = inMenuEvent.getEventKey();
        logger.info("=>" + eventKey);
        String out = "消息已经成功接收";
        if ("HOME".equals(eventKey)) {
            out = "点击<a href=\"http://lepao.riguz.com/lepao/home?open_id=" + inMenuEvent.getFromUserName() + "\">这里</a>进入您的主页";
        }
        else if ("SHARE".equals(eventKey)) {
            out = "点击<a href=\"http://lepao.riguz.com/lepao/share?open_id=" + inMenuEvent.getFromUserName() + "\">这里</a>进入开始分享";
        }
        else if ("MATCH".equals(eventKey)) {
            out = "点击<a href=\"http://lepao.riguz.com/lepao/match?open_id=" + inMenuEvent.getFromUserName() + "\">这里</a>进入开始配对";
        }
        OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
        outMsg.setContent(out);
        this.render(outMsg);
    }

    @Override
    protected void processInQrCodeEvent(InQrCodeEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInShortVideoMsg(InShortVideoMsg arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInSpeechRecognitionResults(InSpeechRecognitionResults arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInTemplateMsgEvent(InTemplateMsgEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInTextMsg(InTextMsg inTextMsg) {
        logger.info("=>" + inTextMsg.getContent());
    }

    @Override
    protected void processInVideoMsg(InVideoMsg arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void processInVoiceMsg(InVoiceMsg arg0) {
        // TODO Auto-generated method stub

    }
}