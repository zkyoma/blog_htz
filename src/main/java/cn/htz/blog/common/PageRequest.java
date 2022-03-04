package cn.htz.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {
    /**
     * 当前页码
     */
    @NotNull(message = "分页的开始页码不能为空")
    private Integer pageNum = 1;
    /**
     * 每页数量
     */
    @NotNull(message = "分页的每页大小不能为空")
    private Integer pageSize = 6;
}
