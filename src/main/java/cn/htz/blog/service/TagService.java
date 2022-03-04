package cn.htz.blog.service;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.po.Tag;
import cn.htz.blog.vo.TagListVo;

import java.util.List;

public interface TagService {
    /**
     * 查询所有标签
     * @return
     */
    PageResult<Tag> selectPage(PageRequestQuery pageRequestQuery);

    /**
     * 保存标签
     * @param tag
     */
    void save(Tag tag);

    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    Tag getTagById(Long id);

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
     * 查询所有的标签信息
     * @return
     */
    List<Tag> selectList();

    /**
     * 检验标签名是否存在
     * @param name
     * @return 存在：true，不存在：false
     */
    Boolean checkName(String name);

    /**
     * 根据标签id集合查询
     * @param tagIds
     * @return
     */
    List<Tag> getTagsByIds(List<Long> tagIds);

    /**
     * 查询热门标签集合，限制limit条
     * @return
     */
    List<TagListVo> listTagTop(int limit);

    /**
     * 查询标签的数量
     * @return
     */
    Long countTag();
}
