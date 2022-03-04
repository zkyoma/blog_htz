package cn.htz.blog.service;

import cn.htz.blog.common.BlogPageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.po.Blog;
import cn.htz.blog.vo.*;

import java.util.List;
import java.util.Map;

public interface BlogService {
    /**
     * 根据id查询文章
     * @param id
     * @return
     */
    Blog getById(Long id);

    /**
     * 分页按条件查询，携带全部的分类、标签集合
     * @param pageRequestQuery
     * @return
     */
    PageResult<BlogVo> selectPage(BlogPageRequestQuery pageRequestQuery);

    /**
     * 根据id修改bool字段的值
     * @param blogUpdateVo
     */
    void updateBoolValue(BlogUpdateVo blogUpdateVo);

    /**
     * 根据博客id删除博客
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id集合删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 获取所有的分类和标签信息
     * @return
     */
    BlogAddPageVo getCategoriesAndTags();

    /**
     * 添加博客
     * @param addBlogVo
     * @param userId 管理员的id
     */
    void addBlog(AddBlogVo addBlogVo, Long userId);

    /**
     * 更新博客信息
     * @param addBlogVo
     */
    void updateBlog(AddBlogVo addBlogVo);

    /**
     * 分页查询
     * @param pageNum
     * @return
     */
    PageResult<BlogListVo> getBlogsForIndexPage(int pageNum);

    /**
     * 查询首页侧边栏数据列表
     * 0-点击最多 1-最新发布
     * @param type
     * @return
     */
    List<SimpleBlogListVo> getBlogListForIndexPage(int type);

    /**
     * 分页查询标签对应的博客列表
     */
    PageResult<BlogListVo> getBlogsPageByTagId(Long tagId, Integer pageNum);

    /**
     * 根据关键字分页查询
     * @param keyword
     * @param page
     * @return
     */
    PageResult<BlogListVo> getBlogsPageBySearch(String keyword, Integer page);

    /**
     * 查询博客的详细信息，根据id
     * @param blogId
     * @return
     */
    BlogDetailVo getDetailBlogById(Long blogId);

    /**
     * 查询推荐博客
     * @param type 1：最热博客，0：最新博客
     * @param limit 限制条数
     * @return
     */
    List<SimpleBlogListVo> listRecommendBlogsTop(int type, int limit);

    /**
     * 归档，分组查询博客信息
     * @return
     */
    Map<String,List<SimpleBlogListVo>> archiveBlog();

    /**
     * 查询所有博客的数量
     * @return
     */
    Long countBlog();

    /**
     * 根据分类id分页查询
     * @param id
     * @param page
     * @return
     */
    PageResult<BlogListVo> getBlogsPageByCategoryId(Long id, Integer page);

    /**
     * 查询所有博客的浏览次数
     * @return
     */
    Long getBlogReadNumTotal();

    /**
     * 查询所有博客的评论数量
     * @return
     */
    Long getBlogCommentTotal();
}
