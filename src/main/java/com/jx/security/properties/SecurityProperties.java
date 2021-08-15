package com.jx.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: zhangjx
 * @Date: 2020/4/10 23:42
 * @Description: 从yml读出来的配置
 */
@ConfigurationProperties(prefix = "jx.security")
@Data
@Component
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties validateCode = new ValidateCodeProperties();
}
