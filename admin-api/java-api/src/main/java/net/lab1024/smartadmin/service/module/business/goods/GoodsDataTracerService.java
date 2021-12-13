package net.lab1024.smartadmin.service.module.business.goods;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;
import net.lab1024.smartadmin.service.module.business.goods.domain.GoodsEntity;
import net.lab1024.smartadmin.service.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.smartadmin.service.module.support.datatracer.service.DataTracerService;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerExtraData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/8/23 10:38
 */
@Service
public class GoodsDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;



    @Async
    public void goodsAddRecord(GoodsEntity goodsEntity, LocalDateTime operateTime, Long operatorId, String operatorName) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(goodsEntity.getGoodsId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.GOODS);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setOperateTime(operateTime);
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName);
    }

    @Async
    public void goodsUpdateRecord(GoodsEntity originGoodsEntity, GoodsEntity newGoodsEntity, LocalDateTime operateTime, Long operatorId, String operatorName) {
        String operateContent = dataTracerFieldService.beanParse(originGoodsEntity, newGoodsEntity);
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(originGoodsEntity.getGoodsId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.GOODS);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(operateContent);
        dataTracerForm.setOperateTime(operateTime);
        dataTracerForm.setExtraData(new DataTracerExtraData(GoodsEntity.class, originGoodsEntity, newGoodsEntity));
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName);
    }

    @Async
    public void batchRecord(List<Long> goodsIdList, String operateContent, BaseEnum operateType, LocalDateTime operateTime, Long operatorId, String operatorName) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long goodsId : goodsIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(goodsId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.GOODS);
            dataTracerForm.setOperateType(operateType);
            dataTracerForm.setOperateContent(operateContent);
            dataTracerForm.setOperateTime(operateTime);
            dataTracerList.add(dataTracerForm);
        }
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName);
    }

}
