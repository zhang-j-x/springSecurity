package com.jx.security.security.validate.code.generator;


import com.jx.security.properties.SecurityProperties;
import com.jx.security.security.validate.code.constant.ValidateCodeConstant;
import com.jx.security.security.validate.code.dto.ValidateCode;
import com.jx.security.security.validate.code.dto.ValidateImageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: zhangjx
 * @Date: 2020/5/16 23:09
 * @Description: 图形验证码生成器
 */
@Component
public class ImageCodeGenerator  extends AbstractValidateCodeGenerator {


    private static Random random = new Random();

    @Autowired
    private SecurityProperties securityProperties;

    // 排除0、1、I、O这些不易识别的字符
    private static final char[] CHARS = { '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };


    /**
     * 生成随机验证码图片的dto
     * @param
     * @return: com.jx.core.validate.code.dto.ValidateImageCode
     */
    @Override
    public ValidateCode genValidateCode(){
        ValidateImageCode validateImageCode = new ValidateImageCode();
        String randomCode = getRandomCode(securityProperties.getValidateCode().getImageCode().getCodeNum());
        System.out.println("验证码：" + randomCode);
        String base64ImageStr = buildBase64ImageStr(randomCode);
        validateImageCode.setBase64ImageStr(base64ImageStr);
        validateImageCode.setUuid(UUID.randomUUID().toString());
        //向redis存入验证码
        getRedisUtil().set(
           ValidateCodeConstant.VALIDATE_IMAGE_CODE + ValidateCodeConstant.VALIDATE_CODE_REDIS_SEPARATOR + validateImageCode.getUuid(),
                randomCode,securityProperties.getValidateCode().getImageCode().getExpireTime());
        return validateImageCode;
    }





    /**
     * 获取四位随机字符
     * @param
     * @return: java.lang.String
     */
    public String getRandomCode(Integer codeNum) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < codeNum; i++) {
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }


    /**
     * 根据随机字符生成验证码图片的base64位字符串
     * @param randomCode 随机字符
     * @return: java.lang.String
     */
    public String  buildBase64ImageStr(String randomCode){
        BufferedImage bufferedImage = buildRandomCodeImg(randomCode);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String imgSrc = null;
        byte[] bf = null;
        try {
            //写入流中
            ImageIO.write(bufferedImage, "png", bos);
            bf = bos.toByteArray();
            imgSrc =Base64Utils.encodeToString(bf);
        } catch (IOException e) {

        }finally {
            try {
                bos.close();
            } catch (IOException e) {

            }
        }
        return imgSrc;
    }



    /**
     * 根据随机字符生成验证码图片
     * @param randomCode 随机字符
     * @return: java.awt.image.BufferedImage
     */
    public BufferedImage buildRandomCodeImg(String randomCode) {
        int x = 0;
        int fontHeight = 0;
        int codeY = 0;
        int red = 0;
        int green = 0;
        int blue = 0;

        //每个字符的宽度(左右各空出一个字符)
        x = securityProperties.getValidateCode().getImageCode().getImgWidth() / (randomCode.length() + 2);
        //字体的高度
        fontHeight = securityProperties.getValidateCode().getImageCode().getImgHeight() - 2;
        codeY = securityProperties.getValidateCode().getImageCode().getImgHeight() - 4;
        // 图像buffer
        BufferedImage buffImg = new BufferedImage(securityProperties.getValidateCode().getImageCode().getImgWidth(), securityProperties.getValidateCode().getImageCode().getImgHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 将图像背景填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, securityProperties.getValidateCode().getImageCode().getImgWidth(), securityProperties.getValidateCode().getImageCode().getImgHeight());
        //设置字体类型、字体大小、字体样式　
        Font font = new Font("微软雅黑",Font.PLAIN, fontHeight);
        g.setFont(font);
        for (int i = 0; i < securityProperties.getValidateCode().getImageCode().getLineCount(); i++) {
            // 设置随机开始和结束坐标
            int xs = random.nextInt(securityProperties.getValidateCode().getImageCode().getImgWidth());
            int ys = random.nextInt(securityProperties.getValidateCode().getImageCode().getImgHeight());
            int xe = xs + random.nextInt(securityProperties.getValidateCode().getImageCode().getImgWidth() / 8);
            int ye = ys + random.nextInt(securityProperties.getValidateCode().getImageCode().getImgHeight() / 8);
            g.setColor(new Color(76,74,117));
            g.drawLine(xs, ys, xe, ye);
        }

        for (int i = 0; i < randomCode.length(); i++) {
            //产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            String strRand = randomCode.charAt(i) + "";
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
        }
        return buffImg;
    }
}
