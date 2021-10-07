package net.lab1024.smartadmin.service.module.support.reload.core.thread;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.support.reload.core.AbstractSmartReloadCommand;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.SmartReloadItem;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.SmartReloadObject;
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
        List<SmartReloadItem> smartReloadItemList = this.abstractSmartReloadCommand.readReloadItem();
        ConcurrentHashMap<String, String> tagIdentifierMap = this.abstractSmartReloadCommand.getTagIdentifierMap();
        for (SmartReloadItem smartReloadItem : smartReloadItemList) {
            String tag = smartReloadItem.getTag();
            String tagIdentifier = smartReloadItem.getIdentification();
            String preTagChangeIdentifier = tagIdentifierMap.get(tag);
            // 数据不一致
            if (preTagChangeIdentifier == null || !preTagChangeIdentifier.equals(tagIdentifier)) {
                this.abstractSmartReloadCommand.putIdentifierMap(tag, tagIdentifier);
                // 执行重新加载此项的动作
                SmartReloadResult smartReloadResult = this.doReload(smartReloadItem);
                this.abstractSmartReloadCommand.handleReloadResult(smartReloadResult);
            }
        }
    }

    /**
     * 方法调用
     *
     * @param smartReloadItem
     * @return
     */
    private SmartReloadResult doReload(SmartReloadItem smartReloadItem) {
        SmartReloadResult result = new SmartReloadResult();
        SmartReloadObject smartReloadObject = this.abstractSmartReloadCommand.reloadObject(smartReloadItem.getTag());
        Method method = smartReloadObject.getMethod();
        result.setTag(smartReloadItem.getTag());
        result.setArgs(smartReloadItem.getArgs());
        result.setIdentification(smartReloadItem.getIdentification());
        result.setResult(true);
        int paramCount = method.getParameterCount();
        if (paramCount > 1) {
            result.setResult(false);
            result.setException("reload方法" + method.getName() + "参数太多");
            return result;
        }
        try {
            if (paramCount == 0) {
                method.invoke(smartReloadObject.getReloadObject());
            } else {
                method.invoke(smartReloadObject.getReloadObject(), smartReloadItem.getArgs());
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
