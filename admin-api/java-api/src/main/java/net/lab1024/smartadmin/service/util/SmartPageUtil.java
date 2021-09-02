package net.lab1024.smartadmin.service.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.domain.PageBaseDTO;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页工具类
 *
 * @author 1024lab
 * @date 2017-12-23 16:40
 */

public class SmartPageUtil {

    public static Page convert2PageQuery(PageBaseDTO baseDTO) {
        Page page = new Page(baseDTO.getPageNum(), baseDTO.getPageSize());

        // 设置排序字段
        List<PageBaseDTO.SortItemDTO> sortItemList = baseDTO.getSortItemList();
        if (CollectionUtils.isNotEmpty(sortItemList)) {
            List<OrderItem> orderItemList = sortItemList.stream().map(e -> build(e.getColumn(), e.getIsAsc())).collect(Collectors.toList());
            page.setOrders(orderItemList);
        }
        return page;
    }

    public static <T> PageResultDTO<T> convert2PageResult(Page<T> page) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setList(page.getRecords());
        result.setEmptyFlag(CollectionUtils.isEmpty(page.getRecords()));
        return result;
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList  原list
     * @param targetClazz 目标类
     * @return
     * @author 罗伊
     * @date 2018年5月16日 下午6:05:28
     */
    public static <T, E> PageResultDTO<T> convert2PageResult(Page page, List<E> sourceList, Class<T> targetClazz) {
        PageResultDTO pageResultDTO = setPage(page);
        List<T> records = SmartBeanUtil.copyList(sourceList, targetClazz);
        page.setRecords(records);
        pageResultDTO.setList(records);
        pageResultDTO.setEmptyFlag(CollectionUtils.isEmpty(records));
        return pageResultDTO;
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList list
     * @return
     * @author 罗伊
     * @date 2018年5月16日 下午6:05:28
     */
    public static <E> PageResultDTO<E> convert2PageResult(Page page, List<E> sourceList) {
        PageResultDTO pageResultDTO = setPage(page);
        page.setRecords(sourceList);
        pageResultDTO.setList(sourceList);
        pageResultDTO.setEmptyFlag(CollectionUtils.isEmpty(sourceList));
        return pageResultDTO;
    }

    /**
     * 转换分页结果对象
     *
     * @param pageResultDTO
     * @param targetClazz
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> PageResultDTO<T> convert2PageResult(PageResultDTO<E> pageResultDTO, Class<T> targetClazz) {

        PageResultDTO newPageResultDTO = new PageResultDTO();
        newPageResultDTO.setPageNum(pageResultDTO.getPageNum());
        newPageResultDTO.setPageSize(pageResultDTO.getPageSize());
        newPageResultDTO.setTotal(pageResultDTO.getTotal());
        newPageResultDTO.setPages(pageResultDTO.getPages());

        List<E> list = pageResultDTO.getList();
        List<T> copyList = SmartBeanUtil.copyList(list, targetClazz);
        newPageResultDTO.setList(copyList);
        newPageResultDTO.setEmptyFlag(CollectionUtils.isEmpty(copyList));
        return newPageResultDTO;
    }

    private static PageResultDTO setPage(Page page) {
        PageResultDTO pageResultDTO = new PageResultDTO();
        pageResultDTO.setPageNum(page.getCurrent());
        pageResultDTO.setPageSize(page.getSize());
        pageResultDTO.setTotal(page.getTotal());
        pageResultDTO.setPages(page.getPages());
        return pageResultDTO;
    }

    private static OrderItem build(String column, boolean asc) {
        OrderItem item = new OrderItem();
        item.setColumn(column);
        item.setAsc(asc);
        return item;
    }


    public static <T> PageResultDTO subListPage(Integer pageNum, Integer pageSize, List<T> list) {
        PageResultDTO<T> pageRet = new PageResultDTO<T>();
        //总条数
        int count = list.size();
        int pages = count%pageSize == 0 ? count/pageSize:(count/pageSize +1);
        int fromIndex = (pageNum-1)*pageSize;
        int toIndex = pageNum*pageSize>count ? count : pageNum*pageSize;

        if(pageNum > pages) {
            pageRet.setList(Lists.newLinkedList());
            pageRet.setPageNum(pageNum.longValue());
            pageRet.setPages(Long.valueOf(pages));
            pageRet.setTotal(Long.valueOf(count));
            return pageRet;
        }
        List<T> pageList = list.subList(fromIndex, toIndex);
        pageRet.setList(pageList);
        pageRet.setPageNum(pageNum.longValue());
        pageRet.setPages(Long.valueOf(pages));
        pageRet.setTotal(Long.valueOf(count));
        return pageRet;
    }
}
