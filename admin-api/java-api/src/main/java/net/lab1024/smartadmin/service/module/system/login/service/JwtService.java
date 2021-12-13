package net.lab1024.smartadmin.service.module.system.login.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.system.employee.service.EmployeeService;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * [  ]
 *
 * @author luoyi
 * @date
 */
@Slf4j
@Service
public class JwtService {

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

    /**
     * 生成 JWT TOKEN
     *
     * @param employeeId
     * @return
     * @auther 胡克
     */
    public String generateJwtToken(Long employeeId) {
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
     * 根据登陆token 获取员工信息BO
     *
     * @param
     * @return
     */
    public LoginUserDetail getEmployeeLoginBO(String token) {
        Long employeeId = decryptToken(token);
        if (employeeId == null) {
            return null;
        }
        // 查询用户信息
        LoginUserDetail loginInfo = employeeService.getBoById(employeeId);
        if (loginInfo == null) {
            return null;
        }
        if (loginInfo.getDisabledFlag() || loginInfo.getLeaveFlag()) {
            return null;
        }
        return loginInfo;
    }

    private Long decryptToken(String token) {
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
