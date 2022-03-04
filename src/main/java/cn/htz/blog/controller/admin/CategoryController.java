package cn.htz.blog.controller.admin;

import cn.htz.blog.common.R;
import cn.htz.blog.po.Category;
import cn.htz.blog.service.CategoryService;
import cn.htz.blog.valid.AddGroup;
import cn.htz.blog.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有一级分类及其子结构
     * @return
     */
    @GetMapping("/list/tree")
    public R list() {
        List<Category> categories = categoryService.listWithTree();
        return R.ok().put("data", categories);
    }

    /**
     * 保存分类
     * @return
     */
    @PostMapping("/save")
    public R save(@Validated(AddGroup.class) @RequestBody Category category) {
        categoryService.save(category);
        return R.ok();
    }

    /**
     * 根据id查询分类信息
     * @return
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        return R.ok().put("data", category);
    }

    /**
     * 更新分类
     * @param category
     * @return
     */
    @PutMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody Category category) {
        categoryService.updateCategory(category);
        return R.ok();
    }

    /**
     * 根据id删除最低级分类
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return R.ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public R deleteBatch(@NotEmpty(message = "根据id集合删除分类，id集合不能为空") @RequestBody List<Long> ids) {
        categoryService.deleteByIds(ids);
        return R.ok();
    }

    /**
     * 检查分类名称是否已经存在
     * @param name
     * @return
     */
    @GetMapping("/checkName")
    public R checkName(@NotBlank(message = "根据名称查询分类，名称不能为空") @RequestParam("name") String name) {
        Boolean isExist = categoryService.isExist(name);
        return R.ok().put("data", isExist);
    }
}
