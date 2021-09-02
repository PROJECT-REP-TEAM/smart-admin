package net.lab1024.smartadmin.service.module.system.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.constant.SystemEnvironmentEnum;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeService;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginBO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Slf4j
@Service
public class EmployeeLoginTokenService {

    /**
     * 默认 token 过期时间 1 天
     */
    private static final int EXPIRE_SECONDS = 24 * 3600;

    /**
     * 默认 jwt key
     */
    private static final String JWT_KEY = "smart-admin-jwt-key";

    /**
     * jwt加密字段
     */
    private static final String CLAIM_ID_KEY = "id";

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SystemEnvironmentEnum systemEnvironment;

    /**
     * 生成 JWT TOKEN
     *
     * @param employeeId
     * @return
     * @auther listen
     */
    public String generateToken(Long employeeId) {
        long nowTimeMilli = System.currentTimeMillis();
        Claims jwtClaims = Jwts.claims();
        jwtClaims.put(CLAIM_ID_KEY, employeeId);
        return Jwts.builder()
                   .setClaims(jwtClaims)
                   .setIssuedAt(new Date(nowTimeMilli))
                   .setExpiration(new Date(nowTimeMilli + EXPIRE_SECONDS * 1000))
                   .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                   .compact();
    }

    /**
     * 根据登陆token 获取员工信息
     *
     * @param
     * @return
     */
    public EmployeeLoginInfoDTO getEmployeeLoginInfo(String token) {
        /**
         * 非生产环境 直接使用 token 作为id
         * 不需要的话 注释了吧
         */
        if (SystemEnvironmentEnum.PROD != systemEnvironment && NumberUtils.isParsable(token)) {
            return employeeService.getById(Long.parseLong(token));
        }

        Long employeeId = getEmployeeIdByToken(token);
        if (employeeId == null) {
            return null;
        }
        // 查询用户信息
        EmployeeLoginInfoDTO loginInfo = employeeService.getById(employeeId);
        if (loginInfo == null) {
            return null;
        }
        if (loginInfo.getDisabledFlag()) {
            return null;
        }
        return loginInfo;
    }

    /**
     * 根据登陆token 获取员工信息BO
     *
     * @param
     * @return
     */
    public EmployeeLoginBO getEmployeeLoginBO(String token) {
        /**
         * 非生产环境 直接使用 token 作为id
         * 不需要的话 注释了吧
         */
        if (SystemEnvironmentEnum.PROD != systemEnvironment && NumberUtils.isParsable(token)) {
            return employeeService.getBoById(Long.parseLong(token));
        }

        Long employeeId = getEmployeeIdByToken(token);
        if(employeeId == null){
            return null;
        }
        // 查询用户信息
        EmployeeLoginBO loginInfo = employeeService.getBoById(employeeId);
        if (loginInfo == null) {
            return null;
        }
        if (loginInfo.getDisabledFlag()) {
            return null;
        }
        return loginInfo;
    }

    private Long getEmployeeIdByToken(String token) {
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(JWT_KEY)
                                .parseClaimsJws(token)
                                .getBody();
            return Long.parseLong(claims.get(CLAIM_ID_KEY).toString());
        } catch (Exception e) {
            log.error("parse employee token error:", e);
        }
        return null;
    }

}
