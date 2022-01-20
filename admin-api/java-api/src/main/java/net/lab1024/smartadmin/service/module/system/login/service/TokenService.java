package net.lab1024.smartadmin.service.module.system.login.service;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.constant.RedisKeyConst;
import net.lab1024.smartadmin.service.module.support.redis.RedisService;
import net.lab1024.smartadmin.service.module.system.employee.service.EmployeeService;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigKeyEnum;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * [  ]
 *
 * @author luoyi
 * @date
 */
@Slf4j
@Service
public class TokenService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SystemConfigService systemConfigService;

    private ConcurrentMap<String, String> loginUserDetailCache = new ConcurrentLinkedHashMap.Builder<String, String>()
            .maximumWeightedCapacity(1000).build();

    /**
     * 生成 TOKEN
     *
     * @return
     * @auther 胡克
     */
    public String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 保存登录token，并记录登录设备，同一个设备类型只允许一个账号登录
     *
     * @param token
     * @param requestUserId
     * @param device
     */
    public void saveToken(String token, String requestUserId, String device) {
        /**
         * 1、检查：当前设备是类型是否登录过
         * 2.1、 如果登录过，则移除token,然后加入redis
         * 2.2、 如果没登录过，则加入redis,
         */

        String deviceRedisKey = getDeviceRedisKey(requestUserId, device);
        String lastLoginToken = redisService.get(deviceRedisKey);
        if (lastLoginToken != null) {
            redisService.delete(lastLoginToken);
        }

        // 过期小时
        int expiresHour = NumberUtils.toInt(systemConfigService.getConfigValue(SystemConfigKeyEnum.LOGIN_EXPIRES_HOUR));
        long expireSeconds = expiresHour * 3600L;

        redisService.set(getTokenRedisKey(token), getTokenAndDeviceInfo(token, device), expireSeconds);
        redisService.set(getDeviceRedisKey(requestUserId, device), token, expireSeconds);
    }


    /**
     * 删除登录状态
     *
     * @param token
     * @param token
     */
    public void deleteToken(String token, String requestUserId) {
        String tokenKey = getTokenRedisKey(token);
        String tokenValue = redisService.get(tokenKey);
        redisService.delete(tokenKey);

        if (tokenValue != null) {
            redisService.delete(getDeviceRedisKey(requestUserId, parseDevice(tokenValue)));
        }

    }

    public Long getRequestUserId(String token) {
        String requestUserIdStr = redisService.get(getTokenRedisKey(token));
        if (requestUserIdStr == null) {
            return null;
        }

        return NumberUtils.toLong(parseToken(token));
    }

    private String getTokenAndDeviceInfo(String token, String device) {
        return token + "_" + device;
    }

    private String parseToken(String value) {
        return value.split("_")[0];
    }

    private String parseDevice(String value) {
        return value.split("_")[1];
    }

    private String getTokenRedisKey(String token) {
        return RedisKeyConst.System.TOKEN + token;
    }

    private String getDeviceRedisKey(String requestUserId, String device) {
        return RedisKeyConst.System.USER_TOKEN_DEVICE + requestUserId + RedisKeyConst.SEPARATOR + device;
    }
}
