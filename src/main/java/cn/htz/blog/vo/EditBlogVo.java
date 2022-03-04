package cn.htz.blog.vo;

import cn.htz.blog.po.Category;
import cn.htz.blog.po.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditBlogVo extends AddBlogVo {
    private List<Category> allCategories;
    private List<Tag> allTags;
    private List<Long> categoryIds;
}
