package com.flowmaster.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应结果
 * 
 * @param <T> 数据类型
 * @author FlowMaster Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页码
     */
    private Long current;
    
    /**
     * 每页大小
     */
    private Long size;
    
    /**
     * 总页数
     */
    private Long pages;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
    
    /**
     * 创建分页结果
     * 
     * @param records 数据列表
     * @param total 总记录数
     * @param current 当前页码
     * @param size 每页大小
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long total, Long current, Long size) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        pageResult.setCurrent(current);
        pageResult.setSize(size);
        pageResult.setPages((total + size - 1) / size);
        pageResult.setHasNext(current < pageResult.getPages());
        pageResult.setHasPrevious(current > 1);
        return pageResult;
    }
}
