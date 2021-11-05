package net.lab1024.smartadmin.service.module.support.operatelog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.OperateLogEntity;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.dto.OperateLogDTO;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.dto.OperateLogQueryForm;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Service
public class OperateLogService {

    @Autowired
    private OperateLogDao operateLogDao;

    /**
     * @author 罗伊
     * @description 分页查询
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<PageResult<OperateLogDTO>> queryByPage(OperateLogQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<OperateLogEntity> logEntityList = operateLogDao.queryByPage(page, queryForm);
        PageResult<OperateLogDTO> pageResult = SmartPageUtil.convert2PageResult(page, logEntityList, OperateLogDTO.class);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * @author 罗伊
     * @description 添加
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<String> add(OperateLogDTO addDTO) {
        OperateLogEntity entity = SmartBeanUtil.copy(addDTO, OperateLogEntity.class);
        operateLogDao.insert(entity);
        return ResponseDTO.ok();
    }

    /**
     * @author 罗伊
     * @description 编辑
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<String> update(OperateLogDTO updateDTO) {
        OperateLogEntity entity = SmartBeanUtil.copy(updateDTO, OperateLogEntity.class);
        operateLogDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * @author 罗伊
     * @description 删除
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<String> delete(Long id) {
        operateLogDao.deleteById(id);
        return ResponseDTO.ok();
    }

    /**
     * @author 罗伊
     * @description 根据ID查询
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<OperateLogDTO> detail(Long id) {
        OperateLogEntity entity = operateLogDao.selectById(id);
        OperateLogDTO dto = SmartBeanUtil.copy(entity, OperateLogDTO.class);
        return ResponseDTO.ok(dto);
    }
}
