package cn.htz.blog.controller.admin;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.common.R;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.po.User;
import cn.htz.blog.service.UserService;
import cn.htz.blog.valid.UpdateGroup;
import cn.htz.blog.vo.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 分页按关键字查询所有用户
     * @param pageRequestQuery
     * @return
     */
    @PostMapping("/page")
    public R page(@Validated @RequestBody PageRequestQuery pageRequestQuery) {
        PageResult<User> pageResult = userService.selectPage(pageRequestQuery);
        return R.ok().put("data", pageResult);
    }

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return R.ok().put("data", user);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PutMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody User user) {
        userService.updateUser(user);
        return R.ok();
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return R.ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public R deleteBatch(@NotEmpty(message = "根据id集合删除用户，id集合不能为空") @RequestBody List<Long> ids) {
        userService.deleteBatchByIds(ids);
        return R.ok();
    }

    /**
     * 管理员登录
     * @param userLogin
     * @param session
     * @return
     */
    @PostMapping("/adminLogin")
    public R login(@RequestBody UserLogin userLogin,
                   HttpSession session) {
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !userLogin.getVerifyCode().equals(kaptchaCode)) {
            throw new MyException(ErrorEnum.CAPTCHA_WRONG);
        }
        session.removeAttribute("verifyCode");
        User u = userService.adminLogin(userLogin);
        if (u != null) {
            session.setAttribute("user", u);
        }
        return R.ok().put("data", u);
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public R logout(HttpSession session) {
        session.invalidate();
        return R.ok();
    }
}
