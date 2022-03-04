package cn.htz.blog.po;

import cn.htz.blog.valid.AddGroup;
import cn.htz.blog.valid.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * 分类实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    /**
     * 分类id
     */
    @Null(message = "新增不能指定分类的id", groups = {AddGroup.class})
    @NotNull(message = "修改必须指定分类的id", groups = {UpdateGroup.class})
    private Long id;
    /**
     * 分类名称
     */
    @NotBlank(message = "分类名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 父级分类id
     */
    @NotNull(message = "父级分类id不能为空", groups = {AddGroup.class})
    private Long parentId;
    /**
     * 该分类的层级
     */
    @NotNull(message = "分类层级不能为空", groups = {AddGroup.class})
    private Integer catLevel;
    /**
     * 分类图标
     */
    private String icon;
    /**
     * 子分类的集合
     */
    private List<Category> children;
    /**
     * 对应博客的数量
     */
    private Long blogCount;
}
