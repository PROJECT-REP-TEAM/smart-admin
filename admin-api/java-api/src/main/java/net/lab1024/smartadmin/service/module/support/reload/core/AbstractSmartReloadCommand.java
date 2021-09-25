package net.lab1024.smartadmin.service.module.support.reload.core;


import net.lab1024.smartadmin.service.module.support.reload.core.domain.ReloadItem;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.ReloadObject;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.SmartReloadResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 检测是否 Reload 的类
 *
 * @author zhuoda
 */
public abstract class AbstractSmartReloadCommand {

    /**
     * 当前ReloadItem的存储器
     */
    private ConcurrentHashMap<String, String> tagIdentifierMap = new ConcurrentHashMap<>();

    private SmartReloadManager smartReloadManager;

    /**
     *
     * @return
     */
    public void setReloadManager(SmartReloadManager smartReloadManager){
        this.smartReloadManager = smartReloadManager;
    }


    /**
     * 该方法返回一个List<ReloadItem></>:<br>
     * ReloadItem对象的tagIdentify为：该tag的 状态（状态其实就是个字符串，如果该字符串跟上次有变化则进行reload操作）<br>
     * ReloadItem对象的args为： reload操作需要的参数<br><br>
     *
     * @return List<ReloadItem>
     */
    public abstract List<ReloadItem> readReloadItem();

    /**
     * 处理Reload结果
     *
     * @param reloadResult
     */
    public abstract void handleReloadResult(SmartReloadResult reloadResult);


    /**
     * 获取本地缓存tag标识
     * @return
     */
    public ConcurrentHashMap<String, String> getTagIdentifierMap() {
        if (tagIdentifierMap != null) {
            return tagIdentifierMap;
        }
        List<ReloadItem> reloadItemList = this.readReloadItem();
        if (reloadItemList == null) {
            return tagIdentifierMap;
        }
        for (ReloadItem reloadItem : reloadItemList) {
            String tag = reloadItem.getTag();
            String identification = reloadItem.getIdentification();
            tagIdentifierMap.put(tag, identification);
        }
        return tagIdentifierMap;
    }

    /**
     * 设置新的缓存标识
     * @param tag
     * @param identification
     */
    public void putIdentifierMap(String tag, String identification) {
        tagIdentifierMap.put(tag, identification);
    }



    /**
     * 获取重载对象
     * @return
     */
    public ReloadObject reloadObject(String tag) {
        if(this.smartReloadManager == null){
            return null;
        }
        Map<String, ReloadObject> reloadObjectMap = smartReloadManager.reloadObjectMap();
        return reloadObjectMap.get(tag);
    }
}
