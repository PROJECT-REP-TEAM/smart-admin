package net.lab1024.smartadmin.service.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleEmployeeEntity;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleEmployeeQueryForm;
import net.lab1024.smartadmin.service.module.system.role.domain.vo.RoleEmployeeVO;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Mapper
@Component
public interface RoleEmployeeDao extends BaseMapper<RoleEmployeeEntity> {

    /**
     * 根据员工id 查询所有的角色
     * @param employeeId
     * @return
     */
    List<Long> selectRoleIdByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据员工id 查询所有的角色
     * @param employeeIdList
     * @return
     */
    List<RoleEmployeeEntity> selectRoleIdByEmployeeIdList(@Param("employeeIdList") List<Long> employeeIdList);

    /**
     * 根据员工id 查询所有的角色
     * @param employeeIdList
     * @return
     */
    List<RoleEmployeeVO> selectRoleByEmployeeIdList(@Param("employeeIdList") List<Long> employeeIdList);

    /**
     * 查询角色下的人员id
     * @param roleIdList
     * @return
     */
    List<Long> selectEmployeeIdByRoleIdList(@Param("roleIdList") List<Long> roleIdList);
    /**
     *
     * @param page
     * @param roleEmployeeQueryForm
     * @return
     */
    List<EmployeeVO> selectRoleEmployeeByName(Page page, @Param("queryForm") RoleEmployeeQueryForm roleEmployeeQueryForm);

    /**
     *
     * @param roleId
     * @return
     */
    List<EmployeeVO> selectEmployeeByRoleId(@Param("roleId") Long roleId);
    /**
     * 根据员工信息删除
     * @param employeeId
     */
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 删除某个角色的所有关系
     * @param roleId
     */
    void deleteByRoleId(@Param("roleId")Long roleId);

    /**
     * 根据员工和 角色删除关系
     * @param employeeId
     * @param roleId
     */
    void deleteByEmployeeIdRoleId(@Param("employeeId") Long employeeId,@Param("roleId")Long roleId);

    /**
     * 批量删除某个角色下的某批用户的关联关系
     * @param roleId
     * @param employeeIds
     */
    void batchDeleteEmployeeRole(@Param("roleId") Long roleId,@Param("employeeIds")List<Long> employeeIds);
}
