package cn.htz.blog.mapper;

import cn.htz.blog.po.User;

import java.util.List;

public interface UserMapper {
    /**
     * 根据关键字查询用户
     * @param key
     * @return
     */
    List<User> selectListByKey(String key);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User getById(Long id);

    /**
     * 更新用户
     * @param user
     */
    void update(User user);

    /**
     * 根据id删除用户
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id集合删除用户
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据用户名和密码查询用户
     * @param u
     * @return
     */
    User selectAdminByUserNameAndPassword(User u);
}
