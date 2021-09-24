package net.lab1024.smartadmin.service.module.support.reload.domain;

import net.lab1024.smartadmin.service.module.support.reload.annotation.SmartReload;
import net.lab1024.smartadmin.service.module.support.reload.domain.entity.ReloadItem;
import net.lab1024.smartadmin.service.module.support.reload.domain.entity.SmartReloadResult;

import java.lang.reflect.Method;

/**
 * Reload 处理程序的实现类
 * 用于包装以注解 SmartReload 实现的处理类
 *
 * @author zhuoda
 */
public class AnnotationReloadObject extends AbstractSmartReloadObject {

    private Object reloadObject;

    private Method method;

    public AnnotationReloadObject(Object reloadObject, Method method) {
        super();
        this.reloadObject = reloadObject;
        this.method = method;
        this.method.setAccessible(true);
    }

    @Override
    public SmartReloadResult reload(ReloadItem reloadItem) {
        SmartReloadResult result = new SmartReloadResult();
        String tag = ((SmartReload)this.method.getAnnotation(SmartReload.class)).value();
        result.setTag(tag);
        result.setArgs(reloadItem.getArgs());
        result.setIdentification(reloadItem.getIdentification());
        result.setResult(true);
        int paramCount = this.method.getParameterCount();
        if (paramCount > 1) {
            result.setResult(false);
            result.setException("reload方法" + this.method.getName() + "参数太多");
            return result;
        } else {
            try {
                if (paramCount == 0) {
                    this.method.invoke(this.reloadObject);
                } else {
                    this.method.invoke(this.reloadObject, reloadItem.getArgs());
                }
            } catch (Throwable var6) {
                result.setResult(false);
                result.setException(this.getStackTrace(var6));
            }

            return result;
        }
    }

}
