package net.lab1024.smartadmin.service.module.business.goods;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import net.lab1024.smartadmin.service.module.business.category.CategoryQueryService;
import net.lab1024.smartadmin.service.module.business.category.constant.CategoryTypeEnum;
import net.lab1024.smartadmin.service.module.business.category.domain.CategoryEntity;
import net.lab1024.smartadmin.service.module.business.goods.domain.*;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品 业务
 *
 * @author 胡克
 * @date 2021/8/6 15:27
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsManager goodsManager;

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private GoodsDataTracerService goodsDataTracerService;

    /**
     * 添加商品
     *
     * @param addForm
     * @return
     */
    public ResponseDTO<String> add(GoodsAddForm addForm) {
        // 商品校验
        ResponseDTO<String> res = this.checkGoods(addForm, null);
        if (!res.getOk()) {
            return res;
        }
        GoodsEntity goodsEntity = SmartBeanUtil.copy(addForm, GoodsEntity.class);
        goodsDao.insert(goodsEntity);
        goodsDataTracerService.goodsAddRecord(goodsEntity, LocalDateTime.now(), addForm.getUpdateId(), addForm.getUpdateName());
        return ResponseDTO.ok();
    }

    /**
     * 更新商品
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(GoodsUpdateForm updateForm) {
        // 商品校验
        ResponseDTO<String> res = this.checkGoods(updateForm, updateForm.getGoodsId());
        if (!res.getOk()) {
            return res;
        }
        GoodsEntity originEntity = goodsDao.selectById(updateForm.getGoodsId());
        GoodsEntity goodsEntity = SmartBeanUtil.copy(updateForm, GoodsEntity.class);
        goodsDao.updateById(goodsEntity);
        goodsDataTracerService.goodsUpdateRecord(originEntity,goodsEntity, LocalDateTime.now(), updateForm.getUpdateId(), updateForm.getUpdateName());
        return ResponseDTO.ok();
    }

    /**
     * 添加/更新 商品校验
     *
     * @param addForm
     * @param goodsId 不为空 代表更新商品
     * @return
     */
    private ResponseDTO<String> checkGoods(GoodsAddForm addForm, Long goodsId) {
        // 校验商品名称重复
        Long categoryId = addForm.getCategoryId();

        GoodsBO goodsBO = new GoodsBO();
        goodsBO.setGoodsName(addForm.getGoodsName());
        goodsBO.setGoodsType(addForm.getGoodsType());
        goodsBO.setCategoryId(categoryId);
        goodsBO.setDeletedFlag(false);
        GoodsEntity goodsEntity = goodsDao.selectOne(goodsBO);
        if (null != goodsEntity) {
            if (null == goodsId || !Objects.equals(goodsEntity.getGoodsId(), goodsId)) {
                return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "商品名称不能重复~");
            }
        }
        // 校验类目id
        Optional<CategoryEntity> optional = categoryQueryService.queryCategory(categoryId);
        if (!optional.isPresent() || !CategoryTypeEnum.GOODS.equalsValue(optional.get().getCategoryType())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "商品类目不存在~");
        }

        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     *
     * @param delForm
     * @return
     */
    public ResponseDTO<String> del(GoodsDelForm delForm) {
        // 批量更新删除状态
        List<GoodsEntity> goodsList = delForm.getGoodsIdList().stream().map(id -> {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setGoodsId(id);
            goodsEntity.setDeletedFlag(true);
            return goodsEntity;
        }).collect(Collectors.toList());
        goodsManager.updateBatchById(goodsList);
        List<Long> goodsIdList = goodsList.stream().map(GoodsEntity::getGoodsId).collect(Collectors.toList());
        goodsDataTracerService.batchRecord(goodsIdList, "删除商品", DataTracerOperateTypeEnum.Common.DELETE, LocalDateTime.now(), delForm.getUpdateId(), delForm.getUpdateName());
        return ResponseDTO.ok();
    }

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<GoodsAdminVO>> query(GoodsQuery queryForm) {
        queryForm.setDeletedFlag(false);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<GoodsAdminVO> list = goodsDao.query(page, queryForm);
        PageResult<GoodsAdminVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        if (pageResult.getEmptyFlag()) {
            return ResponseDTO.ok(pageResult);
        }
        // 查询分类名称
        List<Long> categoryIdList = list.stream().map(GoodsAdminVO::getCategoryId).distinct().collect(Collectors.toList());
        Map<Long, CategoryEntity> categoryMap = categoryQueryService.queryCategoryList(categoryIdList);
        list.forEach(e -> e.setCategoryName(categoryMap.get(e.getCategoryId()).getCategoryName()));
        return ResponseDTO.ok(pageResult);
    }
}
