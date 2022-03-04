package cn.htz.blog.vo;

import cn.htz.blog.po.Blog;
import cn.htz.blog.valid.AddGroup;
import cn.htz.blog.valid.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddBlogVo extends Blog {
    @NotNull(message = "博客对应的标签id集合不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private List<Long> tagIds;
}
