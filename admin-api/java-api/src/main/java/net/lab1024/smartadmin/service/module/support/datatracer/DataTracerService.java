package net.lab1024.smartadmin.service.module.support.datatracer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerDTO;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerEntity;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerQuery;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerVO;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:00
 */
@Slf4j
@Service
public class DataTracerService {

    @Autowired
    private DataTracerDao dataTracerDao;
    @Autowired
    private DataTracerManger dataTracerManger;

    /**
     * 保存操作日志
     *
     * @param dataTracerDTO
     * @param operatorId    操作人id
     * @param operatorName  操作人名称
     */
    public void saveOperateRecord(DataTracerDTO dataTracerDTO, Long operatorId, String operatorName) {
        DataTracerEntity dataTracerEntity = this.convertEntity(dataTracerDTO, operatorId, operatorName);
        dataTracerDao.insert(dataTracerEntity);
    }

    /**
     * 批量保存
     *
     * @param dataTracerList
     * @param operatorId
     * @param operatorName
     */
    public void saveBatchOperateRecord(List<DataTracerDTO> dataTracerList, Long operatorId, String operatorName) {
        if (CollectionUtils.isEmpty(dataTracerList)) {
            return;
        }
        List<DataTracerEntity> recordEntityList = Lists.newArrayList();
        for (DataTracerDTO dataTracerDTO : dataTracerList) {
            DataTracerEntity dataTracerEntity = this.convertEntity(dataTracerDTO, operatorId, operatorName);
            recordEntityList.add(dataTracerEntity);
        }
        dataTracerManger.saveBatch(recordEntityList);
    }

    /**
     * dto 转实体对象
     *
     * @param dataTracerDTO
     * @param operatorId
     * @param operatorName
     * @return
     */
    private DataTracerEntity convertEntity(DataTracerDTO dataTracerDTO, Long operatorId, String operatorName) {
        DataTracerEntity recordEntity = new DataTracerEntity();
        recordEntity.setBusinessId(dataTracerDTO.getBusinessId());
        recordEntity.setBusinessType(dataTracerDTO.getBusinessType().getValue());
        recordEntity.setBusinessTypeDesc(dataTracerDTO.getBusinessType().getDesc());
        recordEntity.setOperateType((Integer) dataTracerDTO.getOperateType().getValue());
        recordEntity.setOperateTypeDesc(dataTracerDTO.getOperateType().getDesc());
        recordEntity.setOperateContent(dataTracerDTO.getOperateContent());
        recordEntity.setOperatorId(operatorId);
        recordEntity.setOperatorName(operatorName);
        if (dataTracerDTO.getExtraData() != null) {
            recordEntity.setExtraData(JSON.toJSONString(dataTracerDTO.getExtraData()));
        }
        recordEntity.setCreateTime(dataTracerDTO.getOperateTime());
        return recordEntity;
    }


    /**
     * 查询某个业务的所有的日志信息
     *
     * @param businessId
     * @param orderType
     * @return
     */
    public List<DataTracerVO> selectRecord(Long businessId, DataTracerBusinessTypeEnum orderType) {
        return dataTracerDao.selectRecord(businessId, orderType.getValue());
    }

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DataTracerVO>> query(DataTracerQuery queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DataTracerVO> list = dataTracerDao.query(page, queryForm);
        PageResult<DataTracerVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }
}
