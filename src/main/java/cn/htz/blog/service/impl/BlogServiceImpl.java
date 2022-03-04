package cn.htz.blog.service.impl;

import cn.htz.blog.common.BlogPageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.mapper.BlogMapper;
import cn.htz.blog.mapper.BlogTagRelationMapper;
import cn.htz.blog.po.*;
import cn.htz.blog.service.BlogService;
import cn.htz.blog.service.CategoryService;
import cn.htz.blog.service.TagService;
import cn.htz.blog.service.UserService;
import cn.htz.blog.util.MarkdownUtils;
import cn.htz.blog.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogTagRelationMapper blogTagRelationMapper;
    @Autowired
    private UserService userService;

    @Override
    public Blog getById(Long id) {
        return blogMapper.getById(id);
    }

    /**
     * 根据最低级分类id，查询分类名称  一级名称/二级名称
     * @param categoryId
     * @return
     */
    private String getCategoryNames(Long categoryId) {
        if (categoryId != null) {
            // 查询父分类集合（包括自己）
            List<Category> parentCategories = categoryService.queryParentCategoriesByCid(categoryId);
            // 设置父子分类的拼接名称
            return categoryService.getNamesByCategoryList(parentCategories);
        }
        return null;
    }

    /**
     * 根据博客id，查询相关联的标签集合
     * @param blogId
     * @return
     */
    private List<Tag> getTags(Long blogId) {
        // 查询关系表集合
        List<BlogTagRelation> blogTagRelations = blogTagRelationMapper.getTagsByBlogId(blogId);
        if (!CollectionUtils.isEmpty(blogTagRelations)) {
            // 转化为标签id集合
            List<Long> tagIds = blogTagRelations.stream().map(BlogTagRelation::getTagId).collect(Collectors.toList());
            // 根据tagIds查询Tag集合
            return tagService.getTagsByIds(tagIds);
        }
        return null;
    }

    /**
     * 将blog集合转化为对应的Vo集合
     * @param blogs
     * @param pageInfo
     * @return
     */
    private PageResult<BlogListVo> getBlogListVos(List<Blog> blogs, PageInfo<Blog> pageInfo) {
        // 将blog集合转化为blogVo集合
        List<BlogListVo> blogListVos = blogs.stream()
                .map(blog -> {
                    BlogListVo blogListVo = new BlogListVo();
                    BeanUtils.copyProperties(blog, blogListVo);
                    if (blog.getCategoryId() != null) {
                        // 设置父子分类的拼接名称
                        blogListVo.setCategoryNames(getCategoryNames(blog.getCategoryId()));
                    }
                    // 设置标签
                    blogListVo.setTags(getTags(blog.getId()));
                    User user = userService.getUserById(blog.getUserId());
                    blogListVo.setUsername(user.getUsername());
                    blogListVo.setAvatar(user.getAvatar());
                    return blogListVo;
                }).collect(Collectors.toList());
        // 返回分页信息
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), blogListVos, pageInfo.getPageNum());
    }

    @Override
    public PageResult<BlogListVo> getBlogsForIndexPage(int pageNum) {
        int pageSize = 8;
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogs = blogMapper.selectPage();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        return getBlogListVos(blogs, pageInfo);
    }

    @Override
    public List<SimpleBlogListVo> getBlogListForIndexPage(int type) {
        List<Blog> blogs = blogMapper.findBlogListByType(type, 5);
        if (CollectionUtils.isEmpty(blogs)) {
            throw new MyException(ErrorEnum.PATH_NOT_FOUND);
        }
        return blogs.stream()
                .map(blog -> {
                    SimpleBlogListVo simpleBlogListVo = new SimpleBlogListVo();
                    BeanUtils.copyProperties(blog, simpleBlogListVo);
                    return simpleBlogListVo;
                }).collect(Collectors.toList());
    }

    @Override
    public PageResult<BlogListVo> getBlogsPageByTagId(Long tagId, Integer pageNum) {
        int pageSize = 8;
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogs = blogMapper.getBlogsPageByTagId(tagId);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        // 将blog集合转化为blogVo集合
        return getBlogListVos(blogs, pageInfo);
    }

    @Override
    public PageResult<BlogListVo> getBlogsPageBySearch(String keyword, Integer pageNum) {
        int pageSize = 8;
        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }
        if ("".equals(keyword)) {
            keyword = null;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogs = blogMapper.getBlogsPageByKeyWord(keyword);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        // 将blog集合转化为blogVo集合
        return getBlogListVos(blogs, pageInfo);
    }

    @Transactional
    @Override
    public BlogDetailVo getDetailBlogById(Long blogId) {
        // 查询博客
        Blog blog = getById(blogId);
        if (blog == null) {
            throw new MyException(ErrorEnum.PATH_NOT_FOUND);
        }
        // 封装成Vo对象
        BlogDetailVo blogDetailVo = new BlogDetailVo();
        BeanUtils.copyProperties(blog, blogDetailVo);
        blogDetailVo.setCategoryNames(getCategoryNames(blog.getCategoryId()));
        // 内容转为html格式
        // String htmlContent = MarkdownUtils.markdownToHtmlExtensions(blogDetailVo.getContent());
        String htmlContent = MarkdownUtils.markdownToHtml(blogDetailVo.getContent());
        blogDetailVo.setContent(htmlContent);
        // 封装user信息
        User user = userService.getUserById(blog.getUserId());
        if (user != null) {
            blogDetailVo.setUsername(user.getUsername());
            blogDetailVo.setAvatar(user.getAvatar());
        }
        // 更新浏览量
        blogMapper.updateReadNumById(blog.getId());
        return blogDetailVo;
    }

    @Override
    public List<SimpleBlogListVo> listRecommendBlogsTop(int type, int limit) {
        List<Blog> blogs = blogMapper.findBlogListByType(1, limit);
        if (CollectionUtils.isEmpty(blogs)) {
            return null;
        }
        return blogs.stream().map(blog -> {
            SimpleBlogListVo simpleBlogListVo = new SimpleBlogListVo();
            BeanUtils.copyProperties(blog, simpleBlogListVo);
            return simpleBlogListVo;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<SimpleBlogListVo>> archiveBlog() {
        List<String> years = blogMapper.findGroupYear();
        // 为了保证归档年份按顺序显示，使用LinkedHashMap存放
        Map<String, List<SimpleBlogListVo>> map = new LinkedHashMap<>();
        for (String year : years) {
            List<Blog> blogs = blogMapper.findByYear(year);
            List<SimpleBlogListVo> blogListVos = blogs.stream().map(blog -> {
                SimpleBlogListVo simpleBlogListVo = new SimpleBlogListVo();
                simpleBlogListVo.setTitle(blog.getTitle());
                simpleBlogListVo.setCreateTime(blog.getCreateTime());
                simpleBlogListVo.setFlag(blog.getFlag());
                simpleBlogListVo.setId(blog.getId());
                return simpleBlogListVo;
            }).collect(Collectors.toList());
            map.put(year, blogListVos);
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogMapper.selectCount();
    }

    @Override
    public PageResult<BlogListVo> getBlogsPageByCategoryId(Long categoryId, Integer page) {
        int pageSize = 8;
        PageHelper.startPage(page, pageSize);
        List<Blog> blogs = blogMapper.getBlogsPageByCategoryId(categoryId);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        // 将blog集合转化为blogVo集合
        return getBlogListVos(blogs, pageInfo);
    }

    @Override
    public Long getBlogReadNumTotal() {
        return blogMapper.selectTotalReadNum();
    }

    @Override
    public Long getBlogCommentTotal() {
        return blogMapper.getBlogCommentTotal();
    }

    @Override
    public PageResult<BlogVo> selectPage(BlogPageRequestQuery pageRequestQuery) {
        // 当前页
        int pageNum = pageRequestQuery.getPageNum();
        // 每页大小
        int pageSize = pageRequestQuery.getPageSize();
        // 查询条件
        Map<String, Object> params = pageRequestQuery.getParams();
        PageHelper.startPage(pageNum, pageSize);
        // 查询博客信息
        List<Blog> blogs = blogMapper.selectPageByCondition(params);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        // 将blog集合转化为blogVo集合
        List<BlogVo> blogVos = blogs.stream()
                .map(blog -> {
                    BlogVo blogVo = new BlogVo();
                    BeanUtils.copyProperties(blog, blogVo);
                    if (blog.getCategoryId() != null) {
                        // 设置父子分类的拼接名称
                        blogVo.setCategoryNames(getCategoryNames(blog.getCategoryId()));
                    }
                    // 查询关系表集合
                    blogVo.setTags(getTags(blog.getId()));
                    return blogVo;
                }).collect(Collectors.toList());
        // 返回分页信息
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), blogVos, pageNum);
    }

    @Transactional
    @Override
    public void updateBoolValue(BlogUpdateVo blogUpdateVo) {
        blogMapper.updateBoolValue(blogUpdateVo);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        blogTagRelationMapper.deleteByBlogId(id);
        blogMapper.deleteByUpdateShowStatus(id);
    }

    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        blogTagRelationMapper.deleteByBlogIds(ids);
        blogMapper.updateShowStatusByIds(ids);
    }

    @Override
    public BlogAddPageVo getCategoriesAndTags() {
        List<Category> categories = categoryService.listWithTree();
        List<Tag> tags = tagService.selectList();
        return new BlogAddPageVo(categories, tags);
    }

    @Transactional
    @Override
    public void addBlog(AddBlogVo addBlogVo, Long userId) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(addBlogVo, blog);
        // 保存博客信息
        // 设置一些默认属性
        blog.setUserId(userId);
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setLikeNum(0);
        blog.setCommentNum(0);
        blog.setReadNum(0);
        blog.setShowStatus(true);
        // 保存博客
        blogMapper.save(blog);
        // 维护博客和标签的中间表
        List<Long> tagIds = addBlogVo.getTagIds();
        // 维护中间表
        insertBlogTagList(tagIds, blog.getId());
    }

    @Transactional
    @Override
    public void updateBlog(AddBlogVo addBlogVo) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(addBlogVo, blog);
        blog.setUpdateTime(new Date());
        // 更新博客
        blogMapper.updateBlog(blog);
        // 维护中间表
        // 根据博客id删除所有的中间关联信息
        blogTagRelationMapper.deleteByBlogId(blog.getId());
        // 根据博客id和tagIds集合新增关联
        insertBlogTagList(addBlogVo.getTagIds(), blog.getId());
    }

    private void insertBlogTagList(List<Long> tagIds, Long blogId) {
        if (!CollectionUtils.isEmpty(tagIds)) {
            List<BlogTagRelation> blogTagRelations = tagIds.stream().map(tagId -> {
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setBlogId(blogId);
                blogTagRelation.setTagId(tagId);
                return blogTagRelation;
            }).collect(Collectors.toList());
            blogTagRelationMapper.insertList(blogTagRelations);
        }
    }
}
