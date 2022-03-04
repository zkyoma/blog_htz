package cn.htz.blog.po;

import cn.htz.blog.valid.AddGroup;
import cn.htz.blog.valid.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 标签实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    /**
     * 标签id
     */
    @Null(message = "新增标签的id必须为空", groups = {AddGroup.class})
    @NotNull(message = "修改标签的id不能为空", groups = {UpdateGroup.class})
    private Long id;
    /**
     * 标签名称
     */
    @NotBlank(message = "标签的名称不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private String name;
}
