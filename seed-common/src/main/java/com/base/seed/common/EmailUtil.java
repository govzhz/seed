package com.base.seed.common;

import io.github.biezhi.ome.OhMyEmail;

import java.util.Properties;

/**
 * @author zz 2019/12/11
 *
 * @see: https://github.com/biezhi/oh-my-email
 */
public class EmailUtil {

    /**
     * 配置初始化，执行一次
     */
    public void init(){
        Properties props = OhMyEmail.defaultConfig(false);
        props.put("mail.smtp.host", "smtp.mxhichina.com"); // 钉钉企业邮箱
        OhMyEmail.config(props, "from email", "from email password");
    }

    /**
     * 发送模版
     */
    public void send(){
//        File excel = generatorExcel(); // create Eemp Email
//        OhMyEmail.subject(fileName)
//                .from("xxx")
//                .to("to email")
//                .bcc("bcc email")
//                .html("fileName")
//                .attach(excel, "fileName.xls")
//                .send();
//        excel.deleteOnExit();
    }

}
