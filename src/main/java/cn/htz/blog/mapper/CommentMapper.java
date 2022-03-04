package cn.htz.blog.mapper;

import cn.htz.blog.po.Comment;

import java.util.List;

public interface CommentMapper {
    /**
     * 根据关键字查询评论
     * @param key
     * @return
     */
    List<Comment> selectListByKey(String key);

    /**
     * 根据id删除评论
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id集合批量删除评论
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据关键字查询最高级评论
     * @param key
     * @return
     */
    List<Comment> selectMaxLevelCommentsByKey(String key);

    /**
     * 根据博客id查询评论集合
     * @param blogId
     * @return
     */
    List<Comment> getCommentsByBlogId(Long blogId);

    /**
     * 根据博客id查询顶级分类集合
     * @param blogId
     * @return
     */
    List<Comment> getMaxLevelCommentsByBlogId(Long blogId);

    /**
     * 保存评论
     * @param comment
     */
    void save(Comment comment);
}
