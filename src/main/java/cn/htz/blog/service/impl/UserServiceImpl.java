package cn.htz.blog.service.impl;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.mapper.UserMapper;
import cn.htz.blog.po.User;
import cn.htz.blog.service.UserService;
import cn.htz.blog.vo.UserLogin;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<User> selectPage(PageRequestQuery pageRequestQuery) {
        int pageNum = pageRequestQuery.getPageNum();
        int pageSize = pageRequestQuery.getPageSize();
        String key = pageRequestQuery.getKey();
        if (key != null) {
            key = key.trim();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectListByKey(key);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList(), pageNum);
    }

    @Override
    public User getUserById(Long id) {
        User user = userMapper.getById(id);
        if (user == null) {
            throw new MyException("用户不存在：" + id, ErrorEnum.PATH_NOT_FOUND.getCode());
        }
        return user;
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        user.setUpdateTime(new Date());
        userMapper.update(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteBatchByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new MyException("批量删除用户的id为空，ids = " + ids ,ErrorEnum.PATH_NOT_FOUND.getCode());
        }
        userMapper.deleteByIds(ids);
    }

    @Override
    public User adminLogin(UserLogin userLogin) {
        User u = new User();
        u.setUsername(userLogin.getUsername());
        u.setPassword(userLogin.getPassword());
        User user = userMapper.selectAdminByUserNameAndPassword(u);
        if (user == null) {
            throw new MyException(ErrorEnum.USERNAME_OR_PASSWORD_WRONG);
        }
        return user;
    }
}
