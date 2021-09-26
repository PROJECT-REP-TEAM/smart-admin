package net.lab1024.smartadmin.service.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.common.domain.PageBaseDTO;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页工具类
 *
 * @author 1024lab
 * @date 2021年9月26日 20:51:40
 */
public class SmartPageUtil {

    /**
     * 转换为查询参数
     *
     * @param baseDTO
     * @return
     */
    public static Page<?> convert2PageQuery(PageBaseDTO baseDTO) {
        Page<?> page = new Page<>(baseDTO.getPageNum(), baseDTO.getPageSize());
        // 设置排序字段
        List<PageBaseDTO.SortItemDTO> sortItemList = baseDTO.getSortItemList();
        if (CollectionUtils.isNotEmpty(sortItemList)) {
            List<OrderItem> orderItemList = sortItemList.stream().map(e -> new OrderItem(e.getColumn(), e.getIsAsc())).collect(Collectors.toList());
            page.setOrders(orderItemList);
        }
        return page;
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList  原list
     * @param targetClazz 目标类
     * @return
     */
    public static <T, E> PageResultDTO<T> convert2PageResult(Page<?> page, List<E> sourceList, Class<T> targetClazz) {
        return convert2PageResult(page, SmartBeanUtil.copyList(sourceList, targetClazz));
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList list
     * @return
     */
    public static <E> PageResultDTO<E> convert2PageResult(Page<?> page, List<E> sourceList) {
        PageResultDTO<E> pageResultDTO = new PageResultDTO<>();
        pageResultDTO.setPageNum(page.getCurrent());
        pageResultDTO.setPageSize(page.getSize());
        pageResultDTO.setTotal(page.getTotal());
        pageResultDTO.setPages(page.getPages());
        pageResultDTO.setList(sourceList);
        pageResultDTO.setEmptyFlag(CollectionUtils.isEmpty(sourceList));
        return pageResultDTO;
    }

    /**
     * 转换分页结果对象
     *
     * @param pageResultDTO
     * @param targetClazz
     * @return
     */
    public static <E, T> PageResultDTO<T> convert2PageResult(PageResultDTO<E> pageResultDTO, Class<T> targetClazz) {
        PageResultDTO<T> newPageResultDTO = new PageResultDTO<>();
        newPageResultDTO.setPageNum(pageResultDTO.getPageNum());
        newPageResultDTO.setPageSize(pageResultDTO.getPageSize());
        newPageResultDTO.setTotal(pageResultDTO.getTotal());
        newPageResultDTO.setPages(pageResultDTO.getPages());
        newPageResultDTO.setEmptyFlag(pageResultDTO.getEmptyFlag());
        newPageResultDTO.setList(SmartBeanUtil.copyList(pageResultDTO.getList(), targetClazz));
        return newPageResultDTO;
    }
}
