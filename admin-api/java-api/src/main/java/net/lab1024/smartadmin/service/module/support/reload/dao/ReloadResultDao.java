package net.lab1024.smartadmin.service.module.support.reload.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadResultEntity;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * t_reload_result 数据表dao
 *
 * @author listen
 * @date 2018/02/10 09:23
 */
@Component
@Mapper
public interface ReloadResultDao extends BaseMapper<ReloadResultEntity> {

    List<ReloadResultVO> query(@Param("tag") String tag);
}
