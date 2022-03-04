package cn.htz.blog.mapper;

import cn.htz.blog.po.Blog;
import cn.htz.blog.vo.BlogUpdateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
    /**
     * 根据id查询文章
     * @param blogId
     * @return
     */
    Blog getById(Long blogId);

    /**
     * 根据条件查询所有博客信息
     * @param params
     * @return
     */
    List<Blog> selectPageByCondition(Map<String, Object> params);

    /**
     * 根据id修改boolean字段的值
     * @param blogUpdateVo
     */
    void updateBoolValue(BlogUpdateVo blogUpdateVo);

    /**
     * 根据id删除博客id
     * @param id
     */
    void deleteByUpdateShowStatus(Long id);

    /**
     * 根据id集合删除博客
     */
    void updateShowStatusByIds(List<Long> ids);

    /**
     * 保存博客
     * @param blog
     */
    void save(Blog blog);

    /**
     * 更新博客信息
     * @param blog
     */
    void updateBlog(Blog blog);

    /**
     * 分页查询博客部分信息，用于前台展示
     * @return
     */
    List<Blog> selectPage();

    /**
     * 根据博客类型查询博客列表
     * @param type
     * @param limit 限制记录条数
     * @return
     */
    List<Blog> findBlogListByType(@Param("type") int type, @Param("limit") int limit);

    /**
     * 根据标签id查询博客集合
     * @param tagId
     * @return
     */
    List<Blog> getBlogsPageByTagId(Long tagId);

    /**
     * 根据关键字查询博客集合
     * @param keyword
     * @return
     */
    List<Blog> getBlogsPageByKeyWord(String keyword);

    /**
     * 根据分类id，查询博客的数量
     * @param cid
     * @return
     */
    Long getCountByCid(Long cid);

    /**
     * 更新浏览量
     * @param id
     */
    void updateReadNumById(Long id);

    /**
     * 查询所有分组的年份
     * @return
     */
    List<String> findGroupYear();

    /**
     * 根据年份查询博客信息
     * @param year
     * @return
     */
    List<Blog> findByYear(String year);

    /**
     * 查询所有博客的数量
     * @return
     */
    Long selectCount();

    /**
     * 根据分类id查询博客信息
     * @param categoryId
     * @return
     */
    List<Blog> getBlogsPageByCategoryId(Long categoryId);

    /**
     * 查询所有博客的阅读量
     * @return
     */
    Long selectTotalReadNum();

    /**
     * 查询所有博客的评论数量
     * @return
     */
    Long getBlogCommentTotal();

    /**
     * 更新博客对应的评论数量
     * @param blogId
     */
    void updateCommentNumById(Long blogId);
}
