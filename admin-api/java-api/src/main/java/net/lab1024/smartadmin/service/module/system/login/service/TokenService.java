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
     * 默认 token 过期时间 7 天
     */
    private static final long EXPIRE_SECONDS = 7* 24 * 3600;

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
    public String generateToken(Long employeeId) {
        long nowTimeMilli = System.currentTimeMillis();
        Claims jwtClaims = Jwts.claims();
        jwtClaims.put(CLAIM_ID_KEY, employeeId);
        return Jwts.builder()
                .setClaims(jwtClaims)
                .setIssuedAt(new Date(nowTimeMilli))
                .setExpiration(new Date(nowTimeMilli + EXPIRE_SECONDS * 1000L))
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .compact();
    }

    /**
     * 根据登陆token 获取员工信息BO
     *
     * @param
     * @return
     */
    public LoginUserDetail getLoginUserDetail(String token) {
        Long employeeId = decryptToken(token);
        if (employeeId == null) {
            return null;
        }
        // 查询用户信息
        LoginUserDetail loginInfo = employeeService.getDetailById(employeeId);
        if (loginInfo == null) {
            return null;
        }
//        if (loginInfo.de() || loginInfo.get()) {
//            return null;
//        }
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
