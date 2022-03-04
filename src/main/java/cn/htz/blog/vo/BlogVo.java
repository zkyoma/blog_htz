package cn.htz.blog.vo;

import cn.htz.blog.po.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogVo extends Blog{
    /**
     * 分类名称（拼接后的）
     */
    private String categoryNames;
}
