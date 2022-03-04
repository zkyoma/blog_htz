package cn.htz.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录Vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    @NotBlank(message = "用户登录时，用户名不能为空")
    private String username;
    @NotBlank(message = "用户登录时，密码不能为空")
    private String password;
    @NotBlank(message = "用户登录时，验证码不能为空")
    private String verifyCode;
}
