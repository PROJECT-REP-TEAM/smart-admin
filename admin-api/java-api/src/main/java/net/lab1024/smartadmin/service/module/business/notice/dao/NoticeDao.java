package net.lab1024.smartadmin.service.module.business.notice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeQueryDTO;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeReadCountDTO;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeReceiveDTO;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeReceiveQueryDTO;
import net.lab1024.smartadmin.service.module.business.notice.domain.entity.NoticeEntity;
import net.lab1024.smartadmin.service.module.business.notice.domain.vo.NoticeDetailVO;
import net.lab1024.smartadmin.service.module.business.notice.domain.vo.NoticeVO;
import net.lab1024.smartadmin.service.module.system.datascope.anno.DataScope;
import net.lab1024.smartadmin.service.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.smartadmin.service.module.system.datascope.constant.DataScopeWhereInTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Mapper
@Component
public interface NoticeDao extends BaseMapper<NoticeEntity> {

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return NoticeEntity
     */
    @DataScope(dataScopeType = DataScopeTypeEnum.NOTICE, joinSql = "n.create_user in (#employeeIds)", whereInType = DataScopeWhereInTypeEnum.EMPLOYEE)
    List<NoticeVO> queryByPage(Page page, @Param("queryDTO") NoticeQueryDTO queryDTO);


    /**
     * 获取某人的未读消息
     *
     * @param page
     * @param employeeId
     * @return
     */
    @DataScope(dataScopeType = DataScopeTypeEnum.NOTICE, joinSql = "e.department_id in (#departmentIds)", whereInType = DataScopeWhereInTypeEnum.DEPARTMENT)
    List<NoticeVO> queryUnreadByPage(Page page, @Param("employeeId") Long employeeId, @Param("sendStatus") Boolean sendStatus);


    /**
     * 获取
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<NoticeReceiveDTO> queryReceiveByPage(Page page, @Param("queryDTO") NoticeReceiveQueryDTO queryDTO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    NoticeDetailVO detail(@Param("id") Long id);

    /**
     * 根据id删除 逻辑删除
     *
     * @param id
     * @param deletedFlag
     */
    void logicDeleteById(@Param("id") Long id, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 批量逻辑删除
     *
     * @param idList
     * @param deletedFlag
     * @return
     */
    void logicDeleteByIds(@Param("idList") List<Long> idList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取消息总数
     *
     * @return
     */
    Integer noticeCount(@Param("sendStatus") Boolean sendStatus);


    /**
     * 获取已读消息数
     *
     * @param employeeIds
     * @return
     */
    List<NoticeReadCountDTO> readCount(@Param("employeeIds") List<Long> employeeIds);


    /**
     * 获取某人的未读消息数
     *
     * @param employeeId
     * @param sendStatus
     * @return
     */
    Integer noticeUnreadCount(@Param("employeeId") Long employeeId, @Param("sendStatus") Boolean sendStatus);

}
