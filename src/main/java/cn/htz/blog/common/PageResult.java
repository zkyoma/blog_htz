package cn.htz.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果类
 * @param <T> 数据类型
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageResult<T>{
    private Long total;  //总条数
    private Integer totalPage;  //总页数
    private List<T> items;  //当前页数据
    private Integer currPage; //当前页
}
