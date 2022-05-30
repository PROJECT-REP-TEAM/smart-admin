package net.lab1024.smartadmin.module.support.operatelog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.common.domain.PageResult;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.common.util.SmartPageUtil;
import net.lab1024.smartadmin.module.support.operatelog.domain.OperateLogEntity;
import net.lab1024.smartadmin.module.support.operatelog.domain.OperateLogQueryForm;
import net.lab1024.smartadmin.module.support.operatelog.domain.OperateLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作记录
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
     */
    public ResponseDTO<PageResult<OperateLogVO>> queryByPage(OperateLogQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<OperateLogEntity> logEntityList = operateLogDao.queryByPage(page, queryForm);
        PageResult<OperateLogVO> pageResult = SmartPageUtil.convert2PageResult(page, logEntityList, OperateLogVO.class);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * @author 罗伊
     * @description 添加
     */
    public ResponseDTO<String> log(OperateLogVO addDTO) {
        OperateLogEntity entity = SmartBeanUtil.copy(addDTO, OperateLogEntity.class);
        operateLogDao.insert(entity);
        return ResponseDTO.ok();
    }

}
