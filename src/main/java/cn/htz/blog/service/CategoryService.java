package cn.htz.blog.service;

import cn.htz.blog.po.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有的一级分类，并封装其子分类集合
     * @return
     */
    List<Category> listWithTree();

    /**
     * 保存分类
     * @param category
     */
    void save(Category category);

    /**
     * 根据id查询分类信息
     * @param id 分类id
     * @return
     */
    Category getCategoryById(Long id);

    /**
     * 更新分类
     * @param category
     */
    void updateCategory(Category category);

    /**
     * 根据id删除最低级分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id集合批量删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据最低级的分类id查询其父分类的集合（包括自己）
     * @param id
     * @return
     */
    List<Category> queryParentCategoriesByCid(Long id);

    /**
     * 检查分类名称是否已经存在
     * @param name
     * @return
     */
    Boolean isExist(String name);

    /**
     * 查询分类集合，限制条数，按对应博客的多少从高到低选择limit条
     * @param limit 限制记录数
     * @return
     */
    List<Category> listCategoryTop(int limit);

    /**
     * 根据层级分类集合，拼接分类名称，父分类名称/子分类名称
     * @param categories
     * @return
     */
    String getNamesByCategoryList(List<Category> categories);

    /**
     * 查询分类的数量，最低级分类
     * @return
     */
    Long countCategory();
}
