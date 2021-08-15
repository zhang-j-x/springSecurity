package com.jx.security.dto;

import lombok.Data;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author: zhangjx
 * @Date: 2021-03-16
 **/
@Data
public class LoginLog {
    private String userName;
    private String time;
    private Boolean result;
    private String loginMethod;

    @Override
    public String toString() {
        return "LoginLog{" +
                "[userName=" + userName + "] "+
                "[time=" + time + "] " +
                "[ result=" + result + "] " +
                "[ loginMethod=" + loginMethod + "] " +
                "}";
    }

    public static void main(String[] args) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserName("张飒");
        loginLog.setLoginMethod("pwd");
        loginLog.setResult(true);
        loginLog.setTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        System.out.println(loginLog.toString());
    }
}
