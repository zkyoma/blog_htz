package cn.htz.blog.mapper;

import cn.htz.blog.po.Link;

import java.util.List;

public interface LinkMapper {
    /**
     * 根据关键字查询友链集合
     * @param key
     * @return
     */
    List<Link> selectListByKey(String key);

    /**
     * 保存友链
     * @param link
     */
    void save(Link link);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Link getById(Long id);

    /**
     * 更新友链
     * @param link
     */
    void update(Link link);

    /**
     * 根据id删除
     * @param id
     */
    void updateShowStatusById(Long id);

    /**
     * 根据id集合删除
     * @param ids
     */
    void updateShowStatusByIds(List<Long> ids);
}
