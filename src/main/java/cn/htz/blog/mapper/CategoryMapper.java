package cn.htz.blog.mapper;

import cn.htz.blog.po.Category;

import java.util.List;

public interface CategoryMapper {

    /**
     * 查询所有一级分类的集合
     * @return
     */
    List<Category> selectList();

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
    Category getById(Long id);

    /**
     * 更新分类
     * @param category
     */
    void update(Category category);

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id集合批量删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据分类名称查询分类
     * @param name
     * @return
     */
    Category getByName(String name);

    /**
     * 查询所有的最低级分类
     * @return
     */
    List<Category> getLastCategoryList();

    /**
     * 查询分类的数量，最低级分类
     * @return
     */
    Long getCategoryCount();
}
