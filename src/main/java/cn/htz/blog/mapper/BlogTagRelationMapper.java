package cn.htz.blog.mapper;

import cn.htz.blog.po.BlogTagRelation;

import java.util.List;

public interface BlogTagRelationMapper {
    /**
     * 批量插入
     */
    void insertList(List<BlogTagRelation> blogTagRelations);

    /**
     * 根据博客id删除
     * @param blogId
     */
    void deleteByBlogId(Long blogId);

    /**
     * 根据博客blogId查询
     * @param blogId
     * @return
     */
    List<BlogTagRelation> getTagsByBlogId(Long blogId);

    /**
     * 根据标签id查询标签对应博客的数量
     * @param id
     * @return
     */
    Long getBlogCountByTagId(Long id);

    /**
     * 根据博客id集合删除
     * @param ids
     */
    void deleteByBlogIds(List<Long> ids);
}
