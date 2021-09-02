package net.lab1024.smartadmin.service.module.business.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.module.business.goods.domain.GoodsAdminVO;
import net.lab1024.smartadmin.service.module.business.goods.domain.GoodsBO;
import net.lab1024.smartadmin.service.module.business.goods.domain.GoodsEntity;
import net.lab1024.smartadmin.service.module.business.goods.domain.GoodsQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品 dao
 *
 * @author 胡克
 * @date 2021/8/6 15:26
 */
@Component
public interface GoodsDao extends BaseMapper<GoodsEntity> {

    /**
     * 查询1个商品 具体条件看sql
     *
     * @param goodsBO
     * @return
     */
    GoodsEntity selectOne(GoodsBO goodsBO);

    /**
     * 分页 查询商品
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<GoodsAdminVO> query(Page page, @Param("query") GoodsQueryDTO queryDTO);
}
