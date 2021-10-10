package net.lab1024.smartadmin.service.module.support.reload;

import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.reload.dao.ReloadItemDao;
import net.lab1024.smartadmin.service.module.support.reload.dao.ReloadResultDao;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadItemEntity;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadItemUpdateDTO;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadItemVO;
import net.lab1024.smartadmin.service.module.support.reload.domain.ReloadResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * reload Service
 *
 * @author 开云
 */
@Service
public class ReloadService {

    @Autowired
    private ReloadItemDao reloadItemDao;

    @Autowired
    private ReloadResultDao reloadResultDao;

    /**
     * 查询
     *
     * @return
     */
    public ResponseDTO<List<ReloadItemVO>> query() {
        List<ReloadItemVO> list = reloadItemDao.query();
        return ResponseDTO.ok(list);
    }

    public ResponseDTO<List<ReloadResultVO>> queryReloadItemResult(String tag) {
        List<ReloadResultVO> reloadResultList = reloadResultDao.query(tag);
        return ResponseDTO.ok(reloadResultList);
    }


    /**
     * 通过标签更新标识符
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateByTag(ReloadItemUpdateDTO updateDTO) {
        ReloadItemEntity reloadItemEntity = reloadItemDao.selectById(updateDTO.getTag());
        if (null == reloadItemEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        reloadItemEntity.setIdentification(updateDTO.getIdentification());
        reloadItemEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        reloadItemDao.updateById(reloadItemEntity);
        return ResponseDTO.ok();
    }
}
