package net.lab1024.smartadmin.service.module.system.systemconfig;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.reload.core.anno.SmartReload;
import net.lab1024.smartadmin.service.module.system.systemconfig.domain.*;
import net.lab1024.smartadmin.service.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.service.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.util.SmartPageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置业务类
 *
 * @author huke
 * @date 2021年9月23日 20:49:13
 */
@Slf4j
@Service
public class SystemConfigService {

    /**
     * 一个简单的系统配置缓存
     */
    private final ConcurrentHashMap<String, SystemConfigEntity> CONFIG_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private SystemConfigDao systemConfigDao;

    @SmartReload("system_config")
    public void configReload(String param) {
        this.initConfigCache();
    }

    /**
     * 初始化系统设置缓存
     */
    @PostConstruct
    private void initConfigCache() {
        CONFIG_CACHE.clear();
        List<SystemConfigEntity> entityList = systemConfigDao.selectList(null);
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        entityList.forEach(entity -> this.CONFIG_CACHE.put(entity.getConfigKey().toLowerCase(), entity));
        log.info("################# 系统配置缓存初始化完毕:{} ###################", CONFIG_CACHE.size());
    }

    /**
     * 刷新系统设置缓存
     */
    private void refreshConfigCache(Long configId) {
        Optional<SystemConfigEntity> optional = this.CONFIG_CACHE.values().stream().filter(e -> Objects.equals(configId, e.getConfigId())).findFirst();
        // 移除缓存
        optional.ifPresent(e -> this.CONFIG_CACHE.remove(e.getConfigKey()));

        // 重新查询 加入缓存
        SystemConfigEntity configEntity = systemConfigDao.selectById(configId);
        if (null == configEntity) {
            return;
        }
        this.CONFIG_CACHE.put(configEntity.getConfigKey().toLowerCase(), configEntity);
    }

    /**
     * 分页查询系统配置
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<SystemConfigVO>> queryConfigPage(SystemConfigQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<SystemConfigEntity> entityList = systemConfigDao.queryByPage(page, queryDTO);
        PageResultDTO<SystemConfigVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, entityList, SystemConfigVO.class);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 查询配置缓存
     *
     * @param configKey
     * @return
     */
    public SystemConfigDTO getConfig(SystemConfigKeyEnum configKey) {
        return this.getConfig(configKey.getValue());
    }

    /**
     * 查询配置缓存
     *
     * @param configKey
     * @return
     */
    public SystemConfigDTO getConfig(String configKey) {
        boolean check = SmartBaseEnumUtil.checkEnum(configKey, SystemConfigKeyEnum.class);
        Assert.isTrue(check, "config key error");

        SystemConfigEntity entity = this.CONFIG_CACHE.get(configKey);
        Assert.notNull(entity, "缺少系统配置[" + configKey + "]");

        return SmartBeanUtil.copy(entity, SystemConfigDTO.class);
    }

    /**
     * 查询配置缓存参数
     *
     * @param configKey
     * @return
     */
    public String getConfigValue(SystemConfigKeyEnum configKey) {
        return this.getConfig(configKey).getConfigValue();
    }

    /**
     * 根据参数key查询 并转换为对象
     *
     * @param configKey
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getConfigValue2Obj(SystemConfigKeyEnum configKey, Class<T> clazz) {
        String configValue = this.getConfigValue(configKey);
        return JSON.parseObject(configValue, clazz);
    }

    /**
     * 添加系统配置
     *
     * @param configAddDTO
     * @return
     */
    public ResponseDTO<String> add(SystemConfigAddDTO configAddDTO) {
        SystemConfigEntity entity = systemConfigDao.selectByKey(configAddDTO.getConfigKey());
        if (null != entity) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        entity = SmartBeanUtil.copy(configAddDTO, SystemConfigEntity.class);
        systemConfigDao.insert(entity);

        // 刷新缓存
        this.refreshConfigCache(entity.getConfigId());
        return ResponseDTO.ok();
    }

    /**
     * 更新系统配置
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateSystemConfig(SystemConfigUpdateDTO updateDTO) {
        Long configId = updateDTO.getConfigId();
        SystemConfigEntity entity = systemConfigDao.selectById(configId);
        if (null == entity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        SystemConfigEntity alreadyEntity = systemConfigDao.selectByKey(updateDTO.getConfigKey());
        if (null != alreadyEntity && !Objects.equals(configId, alreadyEntity.getConfigId())) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "config key 已存在");
        }

        // 更新数据
        entity = SmartBeanUtil.copy(updateDTO, SystemConfigEntity.class);
        systemConfigDao.updateById(entity);

        // 刷新缓存
        this.refreshConfigCache(configId);
        return ResponseDTO.ok();
    }

    /**
     * 更新系统配置
     *
     * @param key
     * @param value
     * @return
     */
    public ResponseDTO<String> updateValueByKey(SystemConfigKeyEnum key, String value) {
        SystemConfigDTO config = this.getConfig(key);
        if (null == config) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 更新数据
        Long configId = config.getConfigId();
        SystemConfigEntity entity = new SystemConfigEntity();
        entity.setConfigId(configId);
        entity.setConfigValue(value);
        systemConfigDao.updateById(entity);

        // 刷新缓存
        this.refreshConfigCache(configId);
        return ResponseDTO.ok();
    }
}
