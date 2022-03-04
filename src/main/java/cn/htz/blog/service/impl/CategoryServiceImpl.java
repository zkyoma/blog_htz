package cn.htz.blog.service.impl;

import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.mapper.BlogMapper;
import cn.htz.blog.mapper.CategoryMapper;
import cn.htz.blog.po.Category;
import cn.htz.blog.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<cn.htz.blog.po.Category> listWithTree() {
        // 1. 查询所有的分类
        List<Category> categories = categoryMapper.selectList();
        if (CollectionUtils.isEmpty(categories)) {
            return null;
        }
        // 2. 组装成父子的树形结构
        return categories.stream()
                // 过滤出所有的一级分类
                .filter(category -> category.getParentId() == 0)
                // 设置一级分类的子分类集合
                .peek(firstCategory -> firstCategory.setChildren(getChildren(firstCategory, categories)))
                .sorted(new Comparator<Category>() {
                    @Override
                    public int compare(Category o1, Category o2) {
                        return o2.getChildren().size() - o1.getChildren().size();
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void save(Category category) {
        // category.setId(null);
        categoryMapper.save(category);
    }

    @Override
    public cn.htz.blog.po.Category getCategoryById(Long id) {
        cn.htz.blog.po.Category category = categoryMapper.getById(id);
        if (category == null) {
            throw new MyException("分类不存在：" + id, ErrorEnum.PATH_NOT_FOUND.getCode());
        }
        return category;
    }

    @Transactional
    @Override
    public void updateCategory(Category category) {
        categoryMapper.update(category);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (id != null) {
            categoryMapper.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new MyException("批量删除分类的id为空，ids = " + ids ,ErrorEnum.PATH_NOT_FOUND.getCode());
        }
        categoryMapper.deleteByIds(ids);
    }

    @Override
    public List<Category> queryParentCategoriesByCid(Long id) {
        List<Category> parentCategories = new ArrayList<>();
        helpQueryParentCategories(id, parentCategories);
        return parentCategories;
    }

    @Override
    public Boolean isExist(String name) {
        cn.htz.blog.po.Category category = categoryMapper.getByName(name);
        return category != null;
    }

    @Override
    public List<Category> listCategoryTop(int limit) {
        List<cn.htz.blog.po.Category> categories = categoryMapper.getLastCategoryList();
        List<Category> categoryList = categories.stream().peek(category -> {
            List<Category> parentCategories = queryParentCategoriesByCid(category.getId());
            category.setName(getNamesByCategoryList(parentCategories));
        }).sorted((o1, o2) -> (int) (o2.getBlogCount() - o1.getBlogCount())).collect(Collectors.toList());
        if (categoryList.size() <= limit) {
            return categoryList;
        }
        return categoryList.subList(0, limit);
    }

    @Override
    public String getNamesByCategoryList(List<Category> categories) {
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            if (i == categories.size() - 1) {
                names.append(categories.get(i).getName());
            } else {
                names.append(categories.get(i).getName()).append("/");
            }
        }
        return names.toString();
    }

    @Override
    public Long countCategory() {
        return categoryMapper.getCategoryCount();
    }

    private void helpQueryParentCategories(Long id, List<Category> parentCategories) {
        if (id != null && id != 0) {
            cn.htz.blog.po.Category category = getCategoryById(id);
            if (category != null) {
                helpQueryParentCategories(category.getParentId(), parentCategories);
                parentCategories.add(category);
            }
        }
    }

    /**
     * 查询当前分类的所有子分类集合
     * @param rootCategory 需要查询的分类
     * @param allCategories 所有分类的集合
     * @return 当前分类的所有子分类的集合
     */
    private List<Category> getChildren(Category rootCategory, List<Category> allCategories) {
        return allCategories.stream()
                // 过滤出当前分类的子分类
                .filter(c -> c.getParentId().equals(rootCategory.getId()))
                // 设置该分类的子分类集合
                .peek(c -> c.setChildren(getChildren(c, allCategories)))
                // 对子菜单集合进行排序
                .sorted(new Comparator<Category>() {
                    @Override
                    public int compare(Category o1, Category o2) {
                        return (int) (o2.getBlogCount() - o1.getBlogCount());
                    }
                })
                .collect(Collectors.toList());
    }
}
