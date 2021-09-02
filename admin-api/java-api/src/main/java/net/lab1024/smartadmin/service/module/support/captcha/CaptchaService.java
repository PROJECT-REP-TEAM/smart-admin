package net.lab1024.smartadmin.service.module.support.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.codeconst.EmployeeResponseCodeConst;
import net.lab1024.smartadmin.service.common.codeconst.ResponseCodeConst;
import net.lab1024.smartadmin.service.common.constant.CommonConst;
import net.lab1024.smartadmin.service.common.constant.RedisKeyConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.captcha.domain.CaptchaVO;
import net.lab1024.smartadmin.service.third.SmartRedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * 图形验证码 服务
 *
 * @author 胡克
 * @date 2021/8/31 20:52
 */
@Slf4j
@Service
public class CaptchaService {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private SmartRedisService redisService;

    /**
     * 获取生成图形验证码
     * 默认 1 分钟有效期
     *
     * @return
     */
    public ResponseDTO<CaptchaVO> generateCaptcha() {
        String base64Code;
        String captchaText = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(captchaText);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", os);
            base64Code = Base64Utils.encodeToString(os.toByteArray());
        } catch (Exception e) {
            log.error("verificationCode exception:", e);
            return ResponseDTO.wrapMsg(ResponseCodeConst.SYSTEM_ERROR, "generate captcha error" );
        }
        // uuid 唯一标识
        String uuid = UUID.randomUUID().toString().replace("-", CommonConst.EMPTY_STR);

        /**
         * 返回验证码对象
         * 图片 base64格式
         * 默认有效时长 80s
         */
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaId(uuid);
        captchaVO.setCaptchaImg("data:image/png;base64," + base64Code);
        redisService.set(buildCaptchaRedisKey(uuid), captchaText, 80L);
        return ResponseDTO.succData(captchaVO);
    }

    /**
     * 校验图形验证码
     *
     * @param captchaId
     * @param captcha
     * @return
     */
    public ResponseDTO<String> checkCaptcha(String captchaId, String captcha) {
        if (StringUtils.isBlank(captchaId) || StringUtils.isBlank(captcha)) {
            return ResponseDTO.wrapMsg(EmployeeResponseCodeConst.ERROR_PARAM, "请输入正确验证码" );
        }
        String redisKey = buildCaptchaRedisKey(captchaId);
        String redisCode = redisService.get(redisKey);
        if (StringUtils.isBlank(redisCode)) {
            return ResponseDTO.wrapMsg(EmployeeResponseCodeConst.VERIFICATION_CODE_INVALID, "验证码错误或已过期，请刷新重试" );
        }
        if (!Objects.equals(redisCode, captcha)) {
            return ResponseDTO.wrapMsg(EmployeeResponseCodeConst.VERIFICATION_CODE_INVALID, "验证码错误或已过期，请刷新重试" );
        }
        // 校验通过 移除
        redisService.del(redisKey);
        return ResponseDTO.succ();
    }

    private String buildCaptchaRedisKey(String codeId) {
        return RedisKeyConst.Base.CAPTCHA + codeId;
    }
}
