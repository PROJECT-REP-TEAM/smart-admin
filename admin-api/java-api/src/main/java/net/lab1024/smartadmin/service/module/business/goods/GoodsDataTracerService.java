package net.lab1024.smartadmin.service.module.business.goods;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;
import net.lab1024.smartadmin.service.module.business.goods.domain.GoodsEntity;
import net.lab1024.smartadmin.service.module.support.datatracer.DataTracerFieldService;
import net.lab1024.smartadmin.service.module.support.datatracer.DataTracerService;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerDTO;
import net.lab1024.smartadmin.service.module.support.datatracer.domain.DataTracerExtraDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
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
        DataTracerDTO dataTracerDTO = new DataTracerDTO();
        dataTracerDTO.setBusinessId(goodsEntity.getGoodsId());
        dataTracerDTO.setBusinessType(DataTracerBusinessTypeEnum.GOODS);
        dataTracerDTO.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerDTO.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerDTO.setOperateTime(operateTime);
        dataTracerService.saveOperateRecord(dataTracerDTO, operatorId, operatorName);
    }

    @Async
    public void goodsUpdateRecord(GoodsEntity originGoodsEntity, GoodsEntity newGoodsEntity, LocalDateTime operateTime, Long operatorId, String operatorName) {
        String operateContent = dataTracerFieldService.beanParse(originGoodsEntity, newGoodsEntity);
        DataTracerDTO dataTracerDTO = new DataTracerDTO();
        dataTracerDTO.setBusinessId(originGoodsEntity.getGoodsId());
        dataTracerDTO.setBusinessType(DataTracerBusinessTypeEnum.GOODS);
        dataTracerDTO.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerDTO.setOperateContent(operateContent);
        dataTracerDTO.setOperateTime(operateTime);
        dataTracerDTO.setExtraData(new DataTracerExtraDataDTO(GoodsEntity.class, originGoodsEntity, newGoodsEntity));
        dataTracerService.saveOperateRecord(dataTracerDTO, operatorId, operatorName);
    }

    @Async
    public void batchRecord(List<Long> goodsIdList, String operateContent, BaseEnum operateType, LocalDateTime operateTime, Long operatorId, String operatorName) {
        List<DataTracerDTO> dataTracerList = Lists.newArrayList();
        for (Long goodsId : goodsIdList) {
            DataTracerDTO dataTracerDTO = new DataTracerDTO();
            dataTracerDTO.setBusinessId(goodsId);
            dataTracerDTO.setBusinessType(DataTracerBusinessTypeEnum.GOODS);
            dataTracerDTO.setOperateType(operateType);
            dataTracerDTO.setOperateContent(operateContent);
            dataTracerDTO.setOperateTime(operateTime);
            dataTracerList.add(dataTracerDTO);
        }
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName);
    }

}
