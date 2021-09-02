package net.lab1024.smartadmin.service.module.business.notice;

import net.lab1024.smartadmin.service.module.business.notice.dao.NoticeDao;
import net.lab1024.smartadmin.service.module.business.notice.dao.NoticeReceiveRecordDao;
import net.lab1024.smartadmin.service.module.business.notice.domain.dto.NoticeUpdateDTO;
import net.lab1024.smartadmin.service.module.business.notice.domain.entity.NoticeEntity;
import net.lab1024.smartadmin.service.module.business.notice.domain.entity.NoticeReceiveRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Service
public class NoticeManage {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private NoticeReceiveRecordDao noticeReceiveRecordDao;

    /**
     * 发送消息
     *
     * @param entity
     * @param employeeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void send(NoticeEntity entity, Long employeeId) {
        entity.setSendStatus(true);
        noticeDao.updateById(entity);
        //默认发件人 已读此消息
        NoticeReceiveRecordEntity recordEntity = new NoticeReceiveRecordEntity();
        recordEntity.setEmployeeId(employeeId);
        recordEntity.setNoticeId(entity.getId());
        noticeReceiveRecordDao.insert(recordEntity);
    }


    /**
     * 保存读取记录
     *
     * @param noticeId
     * @param employeeId
     */
    public void saveReadRecord(Long noticeId, Long employeeId) {
        NoticeReceiveRecordEntity recordEntity = new NoticeReceiveRecordEntity();
        recordEntity.setEmployeeId(employeeId);
        recordEntity.setNoticeId(noticeId);
        noticeReceiveRecordDao.insert(recordEntity);
    }


    /**
     * 消息删除
     *
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(NoticeEntity entity) {
        if (entity.getSendStatus()) {
            //消息已发送 执行逻辑删除
            noticeDao.logicDeleteById(entity.getId(), true);
        } else {
            //消息未发送 执行真实删除
            noticeDao.deleteById(entity.getId());
        }
    }

    /**
     * 更新消息
     *
     * @param updateDTO
     */
    public void update(NoticeUpdateDTO updateDTO) {
        NoticeEntity updateEntity = new NoticeEntity();
        updateEntity.setId(updateDTO.getId());
        updateEntity.setTitle(updateDTO.getTitle());
        updateEntity.setContent(updateDTO.getContent());
        noticeDao.updateById(updateEntity);
    }
}
