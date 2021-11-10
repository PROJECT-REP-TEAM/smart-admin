package net.lab1024.smartadmin.service.module.business.notice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeQuery;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeReadCountDTO;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeReceiveForm;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeReceiveQuery;
import net.lab1024.smartadmin.service.module.business.notice.domain.entity.NoticeEntity;
import net.lab1024.smartadmin.service.module.business.notice.domain.vo.NoticeDetailVO;
import net.lab1024.smartadmin.service.module.business.notice.domain.vo.NoticeVO;
import net.lab1024.smartadmin.service.module.support.datascope.DataScope;
import net.lab1024.smartadmin.service.module.support.datascope.constant.DataScopeTypeEnum;
import net.lab1024.smartadmin.service.module.support.datascope.constant.DataScopeWhereInTypeEnum;
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
     * @param query
     * @return NoticeEntity
     */
    @DataScope(dataScopeType = DataScopeTypeEnum.NOTICE, joinSql = "n.create_user in (#employeeIds)", whereInType = DataScopeWhereInTypeEnum.EMPLOYEE)
    List<NoticeVO> queryByPage(Page page, @Param("query") NoticeQuery query);


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
     * @param query
     * @return
     */
    List<NoticeReceiveForm> queryReceiveByPage(Page page, @Param("query") NoticeReceiveQuery query);

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
