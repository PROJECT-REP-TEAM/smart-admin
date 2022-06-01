package net.lab1024.smartadmin.module.business.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.module.business.goods.domain.*;
import net.lab1024.smartadmin.module.business.goods.domain.entity.GoodsEntity;
import net.lab1024.smartadmin.module.business.goods.domain.form.GoodsQueryForm;
import net.lab1024.smartadmin.module.business.goods.domain.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品 dao
 *
 * @author 胡克
 * @date 2021/8/6 15:26
 */
@Mapper
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
     * @param query
     * @return
     */
    List<GoodsVO> query(Page page, @Param("query") GoodsQueryForm query);
}
