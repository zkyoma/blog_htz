package cn.htz.blog.mapper;

import cn.htz.blog.po.Tag;

import java.util.List;

public interface TagMapper {

    /**
     * 按关键字查询所有标签
     * @return
     */
    List<Tag> selectListByKey(String key);

    /**
     * 保存标签
     * @param tag
     */
    void save(Tag tag);

    /**
     * 根据id查询标签信息
     * @param id
     * @return
     */
    Tag getById(Long id);

    /**
     * 更新标签
     * @param tag
     */
    void update(Tag tag);

    /**
     * 根据id删除标签
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除标签
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据标签名查询标签对象
     * @param name
     * @return
     */
    Tag getByName(String name);

    /**
     * 根据id集合查询标签
     * @param ids
     * @return
     */
    List<Tag> getTagsByIds(List<Long> ids);

    /**
     * 查询标签的数量
     * @return
     */
    Long getTagCount();
}
