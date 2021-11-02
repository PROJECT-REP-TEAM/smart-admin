package net.lab1024.smartadmin.service.module.system.login.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.code.SystemErrorCode;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.common.domain.CaptchaVO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.constant.RedisKeyConst;
import net.lab1024.smartadmin.service.module.system.department.dao.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeDao;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeService;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginForm;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginResultVO;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.menu.domain.bo.MenuLoginBO;
import net.lab1024.smartadmin.service.module.system.menu.service.MenuEmployeeService;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigKeyEnum;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigService;
import net.lab1024.smartadmin.service.module.support.redis.RedisService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * @author zhuoda
 * @version 2.0
 * @company 1024lab.net
 * @copyright (c) 2019-2021 1024lab.netInc. All rights reserved.
 * @date 2021-11-1
 * @since JDK1.8
 */
@Slf4j
@Service
public class LoginService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MenuEmployeeService menuEmployeeService;

    @Autowired
    private SystemConfigService systemConfigService;


    /**
     * 员工登陆
     *
     * @param loginForm
     * @return 返回用户登录信息
     */
    public ResponseDTO<LoginResultVO> login(LoginForm loginForm) {

        /**
         * 1、校验redis里的验证码
         * 2、校验成功后，删除redis
         */
        String redisCaptchaKey = RedisKeyConst.Support.CAPTCHA + loginForm.getCaptchaUuid();
        String redisCaptchaValue = redisService.get(redisCaptchaKey);
        if (StringUtils.isBlank(redisCaptchaValue) || !StringUtils.equalsIgnoreCase(loginForm.getCaptchaCode(), redisCaptchaValue)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "验证码错误");
        }
        // 删除已使用的验证码
        redisService.delete(redisCaptchaKey);

        /**
         * 验证账号和账号状态
         */
        EmployeeEntity employeeEntity = employeeDao.getByLoginName(loginForm.getLoginName(), null);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名不存在");
        }

        if (employeeEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您的账号已被禁用,请联系工作人员");
        }

        /**
         * 验证密码：
         * 1、万能密码
         * 2、真实密码
         */
        String superPassword = EmployeeService.getEncryptPwd(systemConfigService.getConfigValue(SystemConfigKeyEnum.SUPER_PASSWORD));
        String requestPassword = EmployeeService.getEncryptPwd(loginForm.getPassword());
        if (!(superPassword.equals(loginForm.getPassword()) || requestPassword.equals(superPassword))) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名或密码错误！");
        }

        // 生成 登录token
        String token = jwtService.generateJwtToken(employeeEntity.getId());
        // 获取前端菜单以及功能权限
        MenuLoginBO menuLoginBORespDTO = menuEmployeeService.queryMenuTreeByEmployeeId(employeeEntity.getId());
        // 查询部门
        DepartmentEntity departmentEntity = departmentDao.selectById(employeeEntity.getDepartmentId());
        // 返回登录结果
        LoginResultVO loginResultDTO = SmartBeanUtil.copy(employeeEntity, LoginResultVO.class);
        loginResultDTO.setEmployeeId(employeeEntity.getId());
        loginResultDTO.setMenuTree(menuLoginBORespDTO.getMenuTree());
        loginResultDTO.setMenuList(menuLoginBORespDTO.getMenuList());
        loginResultDTO.setAllMenuList(menuLoginBORespDTO.getAllMenuList());
        loginResultDTO.setPointsList(menuLoginBORespDTO.getPointsList());
        loginResultDTO.setDepartmentName(null == departmentEntity ? StringConst.EMPTY_STR : departmentEntity.getName());
        loginResultDTO.setToken(token);
        loginResultDTO.setIsSuperMan(menuEmployeeService.isSuperman(employeeEntity.getId()));

        return ResponseDTO.ok(loginResultDTO);
    }

    /**
     * 退出登陆，清除token缓存
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> logoutByToken(Long employeeId) {
        return ResponseDTO.ok();
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public ResponseDTO<CaptchaVO> getCaptcha() {
        String uuid = UUID.randomUUID().toString().replace("-", StringConst.EMPTY_STR);
        String captchaText = defaultKaptcha.createText();
        String base64Code;
        BufferedImage image = defaultKaptcha.createImage(captchaText);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            base64Code = Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            log.error("verificationCode exception .{}", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    log.error("verificationCode outputStream close exception .{}", e);
                }
            }
        }
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setUuid(uuid);
        captchaVO.setCode("data:image/png;base64," + base64Code);
        redisService.set(RedisKeyConst.Support.CAPTCHA + uuid, captchaText, 60);
        return ResponseDTO.ok(captchaVO);
    }

    /**
     * 重新获取前端localStorage信息
     *
     * @param loginInfo
     * @return
     */
    public LoginResultVO getLoginInfo(RequestEmployee loginInfo) {
        Long employeeId = loginInfo.getEmployeeId();
        LoginResultVO loginDTO = SmartBeanUtil.copy(loginInfo, LoginResultVO.class);
        // 获取前端菜单以及功能权限
        MenuLoginBO menuLoginBORespDTO = menuEmployeeService.queryMenuTreeByEmployeeId(employeeId);
        loginDTO.setMenuTree(menuLoginBORespDTO.getMenuTree());
        loginDTO.setMenuList(menuLoginBORespDTO.getMenuList());
        loginDTO.setAllMenuList(menuLoginBORespDTO.getAllMenuList());
        loginDTO.setPointsList(menuLoginBORespDTO.getPointsList());
        //判断是否为超管
        Boolean isSuperman = menuEmployeeService.isSuperman(loginDTO.getEmployeeId());
        loginDTO.setIsSuperMan(isSuperman);
        return loginDTO;
    }


}
