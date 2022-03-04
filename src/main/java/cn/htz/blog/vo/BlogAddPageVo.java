package cn.htz.blog.vo;

import cn.htz.blog.po.Category;
import cn.htz.blog.po.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogAddPageVo {
    private List<Category> categories;
    private List<Tag> tags;
}
