package net.lab1024.smartadmin.service.module.support.reload.core.thread;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.support.reload.core.AbstractSmartReloadCommand;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.ReloadItem;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.ReloadObject;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.SmartReloadResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * [ reload thread ]
 * 
 * @author
 * @date
 */
@Slf4j
public class SmartReloadRunnable implements Runnable {

    private AbstractSmartReloadCommand abstractSmartReloadCommand;

    public SmartReloadRunnable(AbstractSmartReloadCommand abstractSmartReloadCommand) {
        this.abstractSmartReloadCommand = abstractSmartReloadCommand;
    }

    @Override
    public void run() {
        try {
            this.doTask();
        } catch (Throwable e) {
            log.error("", e);
        }
    }

    /**
     * 检测Identifier变化，执行reload
     */
    private void doTask() {
        List<ReloadItem> reloadItemList = this.abstractSmartReloadCommand.readReloadItem();
        ConcurrentHashMap<String, String> tagIdentifierMap = this.abstractSmartReloadCommand.getTagIdentifierMap();
        for (ReloadItem reloadItem : reloadItemList) {
            String tag = reloadItem.getTag();
            String tagIdentifier = reloadItem.getIdentification();
            String preTagChangeIdentifier = tagIdentifierMap.get(tag);
            // 数据不一致
            if (preTagChangeIdentifier == null || !preTagChangeIdentifier.equals(tagIdentifier)) {
                this.abstractSmartReloadCommand.putIdentifierMap(tag, tagIdentifier);
                // 执行重新加载此项的动作
                SmartReloadResult reloadResult = this.doReload(reloadItem);
                this.abstractSmartReloadCommand.handleReloadResult(reloadResult);
            }
        }
    }

    /**
     * 方法调用
     *
     * @param reloadItem
     * @return
     */
    private SmartReloadResult doReload(ReloadItem reloadItem) {
        SmartReloadResult result = new SmartReloadResult();
        ReloadObject reloadObject = this.abstractSmartReloadCommand.reloadObject(reloadItem.getTag());
        Method method = reloadObject.getMethod();
        result.setTag(reloadItem.getTag());
        result.setArgs(reloadItem.getArgs());
        result.setIdentification(reloadItem.getIdentification());
        result.setResult(true);
        int paramCount = method.getParameterCount();
        if (paramCount > 1) {
            result.setResult(false);
            result.setException("reload方法" + method.getName() + "参数太多");
            return result;
        }
        try {
            if (paramCount == 0) {
                method.invoke(reloadObject.getReloadObject());
            } else {
                method.invoke(reloadObject.getReloadObject(), reloadItem.getArgs());
            }
        } catch (Throwable throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);

            result.setResult(false);
            result.setException(throwable.toString());
        }
        return result;
    }

}
