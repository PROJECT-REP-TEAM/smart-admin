package net.lab1024.smartadmin.service.module.support.reload;

import net.lab1024.smartadmin.service.module.support.reload.core.AbstractSmartReloadCommand;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.ReloadItem;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.SmartReloadResult;
import net.lab1024.smartadmin.service.module.support.reload.dao.ReloadItemDao;
import net.lab1024.smartadmin.service.module.support.reload.dao.ReloadResultDao;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadItemEntity;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadResultEntity;
import net.lab1024.smartadmin.service.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Smart Reload 业务
 *
 * @author listen
 * @date 2018/02/10 09:18
 */
@Component
public class SmartReloadCommand extends AbstractSmartReloadCommand {

    @Autowired
    private ReloadItemDao reloadItemDao;

    @Autowired
    private ReloadResultDao reloadResultDao;

    /**
     * 读取数据库中SmartReload项
     *
     * @return List<ReloadItem>
     */
    @Override
    public List<ReloadItem> readReloadItem() {
        List<ReloadItemEntity> reloadItemEntityList = reloadItemDao.selectList(null);
        return SmartBeanUtil.copyList(reloadItemEntityList, ReloadItem.class);
    }


    /**
     * 保存reload结果
     *
     * @param smartReloadResult
     */
    @Override
    public void handleReloadResult(SmartReloadResult smartReloadResult) {
        ReloadResultEntity reloadResultEntity = SmartBeanUtil.copy(smartReloadResult, ReloadResultEntity.class);
        reloadResultDao.insert(reloadResultEntity);
    }
}
