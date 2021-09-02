package net.lab1024.smartadmin.service.module.system.department;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;

import java.util.List;

/**
 * t_department dao接口
 *
 * @author listen
 * @date 2017/12/19 10:58
 */
@Component
@Mapper
public interface DepartmentDao extends BaseMapper<DepartmentEntity> {

    /**
     * 根据部门id，查询此部门直接子部门的数量
     *
     * @param deptId
     * @return int 子部门的数量
     */
    Integer countSubDepartment(@Param("deptId") Long deptId);

    /**
     * 获取全部部门列表
     *
     * @return
     */
    List<DepartmentVO> listAll();

    /**
     * 获取根据父级ID查询部门列表
     *
     * @return
     */
    List<DepartmentVO> queryByParentId(@Param("parentId") Long parentId);

}
