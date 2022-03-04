package cn.htz.blog.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogTagRelation {
    /**
     * 博客id
     */
    private Long blogId;
    /**
     * 标签id
     */
    private Long tagId;
}
