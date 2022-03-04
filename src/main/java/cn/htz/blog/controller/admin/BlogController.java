package cn.htz.blog.controller.admin;

import cn.htz.blog.common.BlogPageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.common.R;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.po.Blog;
import cn.htz.blog.po.Category;
import cn.htz.blog.po.Tag;
import cn.htz.blog.po.User;
import cn.htz.blog.service.BlogService;
import cn.htz.blog.service.CategoryService;
import cn.htz.blog.service.TagService;
import cn.htz.blog.valid.AddGroup;
import cn.htz.blog.valid.UpdateGroup;
import cn.htz.blog.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/admin/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    /**
     * 分页按条件查询
     * @param pageRequestQuery
     * @return
     */
    @PostMapping("/page")
    public R page(@Validated @RequestBody BlogPageRequestQuery pageRequestQuery) {
        PageResult<BlogVo> blogVos = blogService.selectPage(pageRequestQuery);
        return R.ok().put("data", blogVos);
    }

    /**
     * 修改boolean字段
     * @param blogUpdateVo
     * @return
     */
    @PostMapping("/update/boolvalue")
    public R update(@Validated @RequestBody BlogUpdateVo blogUpdateVo) {
        blogService.updateBoolValue(blogUpdateVo);
        return R.ok();
    }

    /**
     * 根据id删除博客，逻辑删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        blogService.deleteById(id);
        return R.ok();
    }

    /**
     * 根据ids批量删除
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public R deleteBatch(@NotEmpty(message = "根据id集合删除博客，id集合不能为空") @RequestBody List<Long> ids) {
        blogService.deleteByIds(ids);
        return R.ok();
    }

    /**
     * 查询所有分类和标签信息，以便到添加博客页面展示
     * @return
     */
    @GetMapping("/addPage")
    public R addPage() {
        BlogAddPageVo blogAddPageVo = blogService.getCategoriesAndTags();
        return R.ok().put("data", blogAddPageVo);
    }

    /**
     * 添加博客信息
     * @param addBlogVo
     * @return
     */
    @PostMapping("/addBlog")
    public R addBlog(@Validated(AddGroup.class) @RequestBody AddBlogVo addBlogVo, HttpSession session) {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            throw new MyException(ErrorEnum.NO_AUTH);
        }
        blogService.addBlog(addBlogVo, user.getId());
        return R.ok();
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        Blog blog = blogService.getById(id);
        EditBlogVo editBlogVo = new EditBlogVo();
        BeanUtils.copyProperties(blog, editBlogVo);
        editBlogVo.setAllCategories(categoryService.listWithTree());
        editBlogVo.setAllTags(tagService.selectList());
        if (!CollectionUtils.isEmpty(blog.getTags())) {
            List<Long> tagIds = blog.getTags().stream().map(Tag::getId).collect(Collectors.toList());
            editBlogVo.setTagIds(tagIds);
        }
        List<Category> categories = categoryService.queryParentCategoriesByCid(blog.getCategoryId());
        if (!CollectionUtils.isEmpty(categories)) {
            List<Long> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
            editBlogVo.setCategoryIds(categoryIds);
        }
        return R.ok().put("data", editBlogVo);
    }

    /**
     * 更新博客
     * @param addBlogVo
     * @return
     */
    @PostMapping("/updateBlog")
    public R updateBlog(@Validated(UpdateGroup.class) @RequestBody AddBlogVo addBlogVo) {
        blogService.updateBlog(addBlogVo);
        return R.ok();
    }
}
