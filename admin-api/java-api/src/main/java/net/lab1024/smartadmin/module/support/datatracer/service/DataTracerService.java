package net.lab1024.smartadmin.module.support.datatracer.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.common.domain.PageResult;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.support.datatracer.dao.DataTracerDao;
import net.lab1024.smartadmin.module.support.datatracer.manager.DataTracerManger;
import net.lab1024.smartadmin.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.smartadmin.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.smartadmin.module.support.datatracer.domain.DataTracerEntity;
import net.lab1024.smartadmin.module.support.datatracer.domain.DataTracerQueryForm;
import net.lab1024.smartadmin.module.support.datatracer.domain.DataTracerVO;
import net.lab1024.smartadmin.common.util.SmartPageUtil;
import org.apache.commons.collections4.CollectionUtils;
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
public class DataTracerService {

    @Autowired
    private DataTracerDao dataTracerDao;
    @Autowired
    private DataTracerManger dataTracerManger;

    /**
     * 保存操作日志
     *
     * @param dataTracerForm
     * @param operatorId    操作人id
     * @param operatorName  操作人名称
     */
    public void saveOperateRecord(DataTracerForm dataTracerForm, Long operatorId, String operatorName) {
        DataTracerEntity dataTracerEntity = this.convertEntity(dataTracerForm, operatorId, operatorName);
        dataTracerDao.insert(dataTracerEntity);
    }

    /**
     * 批量保存
     *
     * @param dataTracerList
     * @param operatorId
     * @param operatorName
     */
    public void saveBatchOperateRecord(List<DataTracerForm> dataTracerList, Long operatorId, String operatorName) {
        if (CollectionUtils.isEmpty(dataTracerList)) {
            return;
        }
        List<DataTracerEntity> recordEntityList = Lists.newArrayList();
        for (DataTracerForm dataTracerForm : dataTracerList) {
            DataTracerEntity dataTracerEntity = this.convertEntity(dataTracerForm, operatorId, operatorName);
            recordEntityList.add(dataTracerEntity);
        }
        dataTracerManger.saveBatch(recordEntityList);
    }

    /**
     * dto 转实体对象
     *
     * @param dataTracerForm
     * @param operatorId
     * @param operatorName
     * @return
     */
    private DataTracerEntity convertEntity(DataTracerForm dataTracerForm, Long operatorId, String operatorName) {
        DataTracerEntity recordEntity = new DataTracerEntity();
        recordEntity.setBusinessId(dataTracerForm.getBusinessId());
        recordEntity.setBusinessType(dataTracerForm.getBusinessType().getValue());
        recordEntity.setBusinessTypeDesc(dataTracerForm.getBusinessType().getDesc());
        recordEntity.setOperateType((Integer) dataTracerForm.getOperateType().getValue());
        recordEntity.setOperateTypeDesc(dataTracerForm.getOperateType().getDesc());
        recordEntity.setOperateContent(dataTracerForm.getOperateContent());
        recordEntity.setOperatorId(operatorId);
        recordEntity.setOperatorName(operatorName);
        if (dataTracerForm.getExtraData() != null) {
            recordEntity.setExtraData(JSON.toJSONString(dataTracerForm.getExtraData()));
        }
        recordEntity.setCreateTime(dataTracerForm.getOperateTime());
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
    public ResponseDTO<PageResult<DataTracerVO>> query(DataTracerQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DataTracerVO> list = dataTracerDao.query(page, queryForm);
        PageResult<DataTracerVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }
}
