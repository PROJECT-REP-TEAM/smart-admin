package net.lab1024.smartadmin.module.business.notice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.common.code.UserErrorCode;
import net.lab1024.smartadmin.common.domain.PageParam;
import net.lab1024.smartadmin.common.domain.PageResult;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.notice.dao.NoticeDao;
import net.lab1024.smartadmin.module.business.notice.dao.NoticeReceiveRecordDao;
import net.lab1024.smartadmin.module.business.notice.domain.dto.*;
import net.lab1024.smartadmin.module.business.notice.domain.entity.NoticeEntity;
import net.lab1024.smartadmin.module.business.notice.domain.entity.NoticeReceiveRecordEntity;
import net.lab1024.smartadmin.module.business.notice.domain.vo.NoticeDetailVO;
import net.lab1024.smartadmin.module.business.notice.domain.vo.NoticeVO;
import net.lab1024.smartadmin.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private NoticeReceiveRecordDao noticeReceiveRecordDao;

    @Autowired
    private NoticeManage noticeManage;

    /**
     * @author 罗伊
     * @description 分页查询
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<PageResult<NoticeVO>> queryByPage(NoticeQuery queryForm) {
        queryForm.setDeletedFlag(false);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<NoticeVO> dtoList = noticeDao.queryByPage(page, queryForm);
        PageResult<NoticeVO> pageResult = SmartPageUtil.convert2PageResult(page, dtoList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 获取当前登录人的消息列表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<NoticeReceiveForm>> queryReceiveByPage(NoticeReceiveQuery queryForm) {
        queryForm.setSendStatus(true);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<NoticeReceiveForm> dtoList = noticeDao.queryReceiveByPage(page, queryForm);
        //根据用户的接收时间设置读取状态，以便前端对其设置
        dtoList.forEach(e -> {
            e.setReadStatus(e.getReceiveTime() != null);
        });
        PageResult<NoticeReceiveForm> pageResult = SmartPageUtil.convert2PageResult(page, dtoList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 获取我的未读消息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<NoticeVO>> queryUnreadByPage(PageParam queryForm, Long employeeId) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<NoticeVO> dtoList = noticeDao.queryUnreadByPage(page, employeeId, true);
        PageResult<NoticeVO> pageResult = SmartPageUtil.convert2PageResult(page, dtoList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * @author 罗伊
     * @description 添加
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<String> add(NoticeAddForm addForm) {
        NoticeEntity entity = SmartBeanUtil.copy(addForm, NoticeEntity.class);
        entity.setCreateUser(addForm.getCreateId());
        entity.setSendStatus(false);
        entity.setDeletedFlag(true);
        noticeDao.insert(entity);
        return ResponseDTO.ok();
    }

    /**
     * @author 罗伊
     * @description 编辑
     * @date 2019-07-11 16:19:48
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(NoticeUpdateForm updateForm) {
        NoticeEntity entity = noticeDao.selectById(updateForm.getId());
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "此系统通知不存在");
        }
        if (entity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "此系统通知已删除");
        }
        if (entity.getSendStatus()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "此系统通知已发送无法修改");
        }
        noticeManage.update(updateForm);
        return ResponseDTO.ok();
    }

    /**
     * @author 罗伊
     * @description 删除
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<String> delete(Long id) {
        NoticeEntity entity = noticeDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "此系统通知不存在");
        }
        noticeManage.delete(entity);
        return ResponseDTO.ok();
    }

    /**
     * @author 罗伊
     * @description 根据ID查询
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<NoticeDetailVO> detail(Long id) {
        NoticeDetailVO noticeDTO = noticeDao.detail(id);
        return ResponseDTO.ok(noticeDTO);
    }

    /**
     * 获取某人的未读消息数
     *
     * @param employeeId
     * @return
     */
    private Integer getUnreadCount(Long employeeId) {
        return noticeDao.noticeUnreadCount(employeeId, true);
    }

    /**
     * 发送给所有在线用户未读消息数
     *
     * @param id
     * @param employeeId
     * @return
     */
    public ResponseDTO<NoticeDetailVO> send(Long id, Long employeeId) {
        NoticeEntity entity = noticeDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "此系统通知不存在");
        }
        noticeManage.send(entity, employeeId);
        this.sendMessage(employeeId);
        return ResponseDTO.ok();
    }

    /**
     * 发送系统通知 ，发送人不进行接收,需再事务外调用 以防止数据隔离级别不同造成未读消息数异常
     *
     * @param sendEmployeeId
     */
    private void sendMessage(Long sendEmployeeId) {
/*        List<Long> onLineEmployeeIds = WebSocketServer.getOnLineUserList();
        if (CollectionUtils.isEmpty(onLineEmployeeIds)) {
            return;
        }
        //在线用户已读消息数
        Map<Long, Integer> readCountMap = new HashMap<>();
        List<NoticeReadCountDTO> readCountList = noticeDao.readCount(onLineEmployeeIds);
        if (CollectionUtils.isNotEmpty(readCountList)) {
            readCountMap = readCountList.stream().collect(Collectors.toMap(NoticeReadCountDTO::getEmployeeId, NoticeReadCountDTO::getReadCount));
        }
        //已发送消息数
        Integer noticeCount = noticeDao.noticeCount(true);
        for (Long employeeId : onLineEmployeeIds) {
            if (Objects.equals(employeeId, sendEmployeeId)) {
                continue;
            }
            int readCount = readCountMap.get(employeeId) == null ? 0 : readCountMap.get(employeeId);
            WebSocketServer.sendOneOnLineUser(String.valueOf(noticeCount - readCount), employeeId);
        }*/
    }

    /**
     * 读取消息
     *
     * @param id
     * @param employeeId
     * @return
     */
    public ResponseDTO<NoticeDetailVO> read(Long id, Long employeeId) {
        NoticeDetailVO noticeDTO = noticeDao.detail(id);

        NoticeReceiveRecordEntity recordEntity = noticeReceiveRecordDao.selectByEmployeeAndNotice(employeeId, id);
        if (recordEntity != null) {
            return ResponseDTO.ok(noticeDTO);
        }
        noticeManage.saveReadRecord(id, employeeId);
        this.sendMessage(employeeId);
        return ResponseDTO.ok(noticeDTO);
    }
}
