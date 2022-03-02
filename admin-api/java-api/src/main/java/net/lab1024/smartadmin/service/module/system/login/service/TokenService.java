package net.lab1024.smartadmin.service.module.system.login.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.common.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.service.constant.RedisKeyConst;
import net.lab1024.smartadmin.service.module.support.redis.RedisService;
import net.lab1024.smartadmin.service.module.system.login.constant.LoginDeviceEnum;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigKeyEnum;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * [  ]
 *
 * @author luoyi
 * @date
 */
@Slf4j
@Service
public class TokenService {

    /**
     * 万能密码 默认失效时间为1 hour
     */
    private static final int SUPER_PASSWORD_TOKEN_EXPIRE_SECONDS = 3600;

    /**
     * 使用 万能密码 登录系统的token前缀
     */
    private static final String SUPER_PASSWORD_TOKEN_PREFIX = "s";

    @Autowired
    private RedisService redisService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private LoginService loginService;

    /**
     * 生成 TOKEN，token 格式为： [设备类型 + uuid]  （其中设备类型 只占据第一个字符)
     *
     * @return
     * @auther 卓大
     */
    public String generateToken(Integer loginDevice, boolean isSuperPassword) {
        LoginDeviceEnum loginDeviceEnum = SmartBaseEnumUtil.getEnumByValue(loginDevice, LoginDeviceEnum.class);
        if (loginDeviceEnum == null) {
            throw new BusinessException("不支持的登录设备类型" + loginDevice);
        }

        if (isSuperPassword) {
            return loginDevice + UUID.randomUUID().toString().replaceAll("-", "");
        } else {
            return SUPER_PASSWORD_TOKEN_PREFIX + loginDevice + UUID.randomUUID().toString().replaceAll("-", "");
        }
    }

    /**
     * 保存登录token，并记录登录设备，同一个设备类型只允许一个账号登录
     *
     * @param token
     * @param requestUserId
     */
    public void saveToken(String token, Long requestUserId) {
        redisService.set(getTokenRedisKey(token), requestUserId, loginService.getLoginExpireSeconds());
    }


    /**
     * 删除登录状态
     *
     * @param token
     * @param token
     */
    public void deleteToken(String token) {
        redisService.delete(getTokenRedisKey(token));
    }

    /**
     * 根据token 获取 user id
     *
     * @param token
     * @return
     */
    public Long getRequestUserId(String token) {
        String requestUserIdStr = redisService.get(getTokenRedisKey(token));
        if (requestUserIdStr == null) {
            return null;
        }

        return NumberUtils.toLong(token);
    }


    private String getTokenRedisKey(String token) {
        return RedisKeyConst.System.TOKEN + token;
    }

}
