package net.lab1024.smartadmin.service.module.business.goods;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.business.category.CategoryQueryService;
import net.lab1024.smartadmin.service.module.business.category.constant.CategoryTypeEnum;
import net.lab1024.smartadmin.service.module.business.category.domain.CategoryEntity;
import net.lab1024.smartadmin.service.module.business.goods.domain.*;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 添加商品
     *
     * @param addDTO
     * @return
     */
    public ResponseDTO<String> add(GoodsAddDTO addDTO) {
        // 商品校验
        ResponseDTO<String> res = this.checkGoods(addDTO, null);
        if (!res.getOk()) {
            return res;
        }

        GoodsEntity goodsEntity = SmartBeanUtil.copy(addDTO, GoodsEntity.class);
        goodsDao.insert(goodsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新商品
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> update(GoodsUpdateDTO updateDTO) {
        // 商品校验
        ResponseDTO<String> res = this.checkGoods(updateDTO, updateDTO.getGoodsId());
        if (!res.getOk()) {
            return res;
        }

        GoodsEntity goodsEntity = SmartBeanUtil.copy(updateDTO, GoodsEntity.class);
        goodsDao.updateById(goodsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 添加/更新 商品校验
     *
     * @param addDTO
     * @param goodsId 不为空 代表更新商品
     * @return
     */
    private ResponseDTO<String> checkGoods(GoodsAddDTO addDTO, Long goodsId) {
        // 校验商品名称重复
        Long categoryId = addDTO.getCategoryId();

        GoodsBO goodsBO = new GoodsBO();
        goodsBO.setGoodsName(addDTO.getGoodsName());
        goodsBO.setGoodsType(addDTO.getGoodsType());
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
     * @param delDTO
     * @return
     */
    public ResponseDTO<String> del(GoodsDelDTO delDTO) {
        // 批量更新删除状态
        List<GoodsEntity> goodsList = delDTO.getGoodsIdList().stream().map(id -> {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setGoodsId(id);
            goodsEntity.setDeletedFlag(true);
            return goodsEntity;
        }).collect(Collectors.toList());
        goodsManager.updateBatchById(goodsList);
        return ResponseDTO.ok();
    }

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<GoodsAdminVO>> query(GoodsQueryDTO queryDTO) {
        queryDTO.setDeletedFlag(false);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<GoodsAdminVO> list = goodsDao.query(page, queryDTO);
        PageResultDTO<GoodsAdminVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
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
