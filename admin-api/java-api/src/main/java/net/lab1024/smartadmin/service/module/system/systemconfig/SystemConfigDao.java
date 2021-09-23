package net.lab1024.smartadmin.service.module.system.systemconfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.module.system.systemconfig.domain.SystemConfigEntity;
import net.lab1024.smartadmin.service.module.system.systemconfig.domain.SystemConfigQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统参数配置 t_system_config Dao层
 *
 * @author 1024lab
 * @date 2017-12-23 14:25
 */
@Component
@Mapper
public interface SystemConfigDao extends BaseMapper<SystemConfigEntity> {

    /**
     * 分页查询系统配置
     *
     * @param page
     * @return
     */
    List<SystemConfigEntity> queryByPage(Page page, @Param("query") SystemConfigQueryDTO queryDTO);

    /**
     * 根据key查询获取数据
     *
     * @param key
     * @return
     */
    SystemConfigEntity selectByKey(String key);
}
