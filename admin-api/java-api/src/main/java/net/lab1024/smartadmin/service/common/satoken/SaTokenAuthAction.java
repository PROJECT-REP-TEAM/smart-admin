package net.lab1024.smartadmin.service.common.satoken;

import cn.dev33.satoken.action.SaTokenActionDefaultImpl;
import cn.dev33.satoken.stp.StpUtil;
import net.lab1024.smartadmin.service.common.annoation.NoNeedLogin;
import net.lab1024.smartadmin.service.common.annoation.NoValidPrivilege;
import net.lab1024.smartadmin.service.common.util.SmartStringUtil;
import net.lab1024.smartadmin.service.module.system.menu.service.MenuEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

/**
 * [ sa-token 为支持SmartAdmin 设置所有方法默认存在权限，权限key为类目+方法名 ]
 *
 * @author yandanyang
 * @date 2021/10/13 17:48
 */
@Component
public class SaTokenAuthAction extends SaTokenActionDefaultImpl {

    @Autowired
    private MenuEmployeeService menuEmployeeService;

    @Override
    public void validateAnnotation(AnnotatedElement target) {
        super.validateAnnotation(target);
        if (target instanceof Method) {
            Method method = (Method) target;
            NoNeedLogin noNeedLogin = method.getAnnotation(NoNeedLogin.class);
            if (noNeedLogin != null) {
                return;
            }
            NoValidPrivilege noValidPrivilege = method.getAnnotation(NoValidPrivilege.class);
            if (noValidPrivilege != null) {
                return;
            }
            Long employeeId = StpUtil.getLoginIdAsLong();
            Boolean isSuperman = menuEmployeeService.isSuperman(employeeId);
            if(isSuperman){
                return;
            }
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();
            List<String> list = SmartStringUtil.splitConvertToList(className, "\\.");
            String controllerName = list.get(list.size() - 1);
            String permissionName = controllerName + "." + methodName;
            StpUtil.stpLogic.checkPermission(permissionName);
        }
    }
}
