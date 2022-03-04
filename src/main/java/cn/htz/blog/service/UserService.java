package cn.htz.blog.service;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.po.User;
import cn.htz.blog.vo.UserLogin;

import java.util.List;

public interface UserService {
    /**
     * 分页按条件查询所有用户
     * @param pageRequestQuery
     * @return
     */
    PageResult<User> selectPage(PageRequestQuery pageRequestQuery);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 根据id删除用户
     * @param id
     */
    void deleteUserById(Long id);

    /**
     * 根据id集合批量删除
     * @param ids
     */
    void deleteBatchByIds(List<Long> ids);

    /**
     * 用户登录
     * @param user
     * @return
     */
    User adminLogin(UserLogin userLogin);
}
