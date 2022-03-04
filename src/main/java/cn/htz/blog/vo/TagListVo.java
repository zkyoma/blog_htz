package cn.htz.blog.vo;

import cn.htz.blog.po.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagListVo extends Tag {
    /**
     * 标签对应的博客数量
     */
    private Long blogCount;
}
