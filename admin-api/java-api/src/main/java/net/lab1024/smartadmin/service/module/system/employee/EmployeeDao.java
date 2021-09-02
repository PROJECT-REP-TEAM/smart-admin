package net.lab1024.smartadmin.service.module.system.employee;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.EmployeeQueryDTO;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;

import java.util.Collection;
import java.util.List;

/**
 * 员工dao接口
 *
 * @author 开云
 * @date 2017年12月19日下午1:36:30
 */
@Mapper
@Component
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {
    /**
     * 查询员工列表
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<EmployeeVO> queryEmployee(Page page, @Param("queryDTO") EmployeeQueryDTO queryDTO);

    /**
     * 查询员工列表
     *
     * @param queryDTO
     * @return
     */
    List<EmployeeVO> queryEmployee(@Param("queryDTO") EmployeeQueryDTO queryDTO);

    /**
     * 批量更新禁用状态
     *
     * @param employeeIdList
     * @param disabledFlag
     */
    void batchUpdateDisableFlag(@Param("employeeIdList") List<Long> employeeIdList,
                                @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 更新单个
     *
     * @param id
     * @param disabledFlag
     */
    void updateDisableFlag(@Param("id") Long id, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 根据 账号 密码 查询
     *
     * @param loginName
     * @param loginPwd
     * @param deletedFlag
     * @return
     */
    EmployeeEntity selectByLoginNameAndPwd(@Param("loginName") String loginName,
                                           @Param("loginPwd") String loginPwd,
                                           @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 通过登录名查询
     *
     * @param loginName
     * @param disabledFlag
     * @return
     */
    EmployeeDTO getByLoginName(@Param("loginName") String loginName,
                               @Param("disabledFlag") Boolean disabledFlag,
                               @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 通过手机号查询
     *
     * @param phone
     * @param disabledFlag
     * @return
     */
    EmployeeDTO getByPhone(@Param("phone") String phone, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 获取所有员工
     *
     * @return
     */
    List<EmployeeDTO> listAll();

    /**
     * 获取某个部门员工数
     *
     * @param departmentId
     * @param deletedFlag  可以null
     * @return
     */
    Integer countByDepartmentId(@Param("departmentId") Long departmentId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取一批员工
     *
     * @param employeeIds
     * @return
     */
    List<EmployeeDTO> getEmployeeByIds(@Param("ids") Collection<Long> employeeIds);


    /**
     * 查询单个员工信息
     *
     * @param employeeId
     * @return
     */
    EmployeeDTO getEmployeeById(@Param("id") Long employeeId);


    /**
     * 获取某个部门的员工
     *
     * @param departmentId
     * @param disabledFlag
     * @param deletedFlag
     * @return
     */
    List<EmployeeEntity> selectByDepartmentId(@Param("departmentId") Long departmentId,  @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取某批部门的员工Id
     *
     * @param departmentIds
     * @return
     */
    List<Long> getEmployeeIdByDepartmentIdList(@Param("departmentIds") List<Long> departmentIds, @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取所有
     * @param disabledFlag
     * @param deletedFlag
     * @return
     */
    List<Long> getEmployeeId( @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取某个部门的员工Id
     * @param departmentId
     * @param disabledFlag
     * @param deletedFlag
     * @return
     */
    List<Long> getEmployeeIdByDepartmentId(@Param("departmentId") Long departmentId, @Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 员工重置密码
     *
     * @param employeeId
     * @param password
     * @return
     */
    Integer updatePassword(@Param("employeeId") Integer employeeId, @Param("password") String password);

}