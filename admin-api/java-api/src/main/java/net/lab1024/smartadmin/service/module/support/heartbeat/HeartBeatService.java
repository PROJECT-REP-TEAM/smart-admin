package net.lab1024.smartadmin.service.module.support.heartbeat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.domain.PageBaseDTO;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Slf4j
@Service
public class HeartBeatService {

    @Autowired
    private HeartBeatRecordDao heartBeatRecordDao;


    public ResponseDTO<PageResultDTO<HeartBeatRecordVO>> pageQuery(PageBaseDTO pageBaseDTO) {
        Page pageQueryInfo = SmartPageUtil.convert2PageQuery(pageBaseDTO);
        List<HeartBeatRecordVO> recordVOList = heartBeatRecordDao.pageQuery(pageQueryInfo);
        PageResultDTO<HeartBeatRecordVO> pageResultDTO = SmartPageUtil.convert2PageResult(pageQueryInfo, recordVOList);
        return ResponseDTO.ok(pageResultDTO);
    }
}
