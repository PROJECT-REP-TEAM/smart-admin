package net.lab1024.smartadmin.service.module.support.operatelog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.OperateLogEntity;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.OperateLogQueryForm;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.OperateLogVO;
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
    public ResponseDTO<PageResult<OperateLogVO>> queryByPage(OperateLogQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<OperateLogEntity> logEntityList = operateLogDao.queryByPage(page, queryForm);
        PageResult<OperateLogVO> pageResult = SmartPageUtil.convert2PageResult(page, logEntityList, OperateLogVO.class);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * @author 罗伊
     * @description 添加
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<String> add(OperateLogVO addDTO) {
        OperateLogEntity entity = SmartBeanUtil.copy(addDTO, OperateLogEntity.class);
        operateLogDao.insert(entity);
        return ResponseDTO.ok();
    }

}
