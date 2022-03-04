package cn.htz.blog.po;

import cn.htz.blog.valid.AddGroup;
import cn.htz.blog.valid.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * 用户id
     */
    @Null(message = "新增用户的id必须为空", groups = {AddGroup.class})
    @NotNull(message = "更新用户的id不能为空", groups = {UpdateGroup.class})
    private Long id;
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(min = 5, max = 20, groups = {AddGroup.class, UpdateGroup.class})
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空", groups = {AddGroup.class})
    private String password;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Email(message = "邮箱格式不合法",  groups = {AddGroup.class, UpdateGroup.class})
    private String email;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误", groups = {AddGroup.class, UpdateGroup.class})
    private String phone;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户类型（1：管理员，0：普通用户）
     */

    private Boolean type;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
