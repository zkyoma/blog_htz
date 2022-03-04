# 项目难点
## 多级分类

分类表：

| id     | name     | parent_id | cat_level | icon     |
| ------ | -------- | --------- | --------- | -------- |
| 主键id | 分类名称 | 父分类id  | 分类层级  | 分类图标 |

### 查询分类的树形结构

```java
public List<Category> listWithTree() {
    // 1. 查询所有的分类
    List<Category> categories = categoryMapper.selectList();
    if (CollectionUtils.isEmpty(categories)) {
        return null;
    }
    // 2. 组装成父子的树形结构
    return categories.stream()
        // 过滤出所有的一级分类
        .filter(category -> category.getParentId() == 0)
        // 3. 设置一级分类的子分类集合
        .peek(firstCategory -> firstCategory.setChildren(getChildren(firstCategory, categories)))
        .sorted(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o2.getChildren().size() - o1.getChildren().size();
            }
        })
        .collect(Collectors.toList());
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
```

### 根据最低级分类查询其父分类的集合（包括自己）

```java
public List<Category> queryParentCategoriesByCid(Long id) {
    List<Category> parentCategories = new ArrayList<>();
    helpQueryParentCategories(id, parentCategories);
    return parentCategories;
}

private void helpQueryParentCategories(Long id, List<Category> parentCategories) {
    // id == 0 表示无父级分类了（已是顶级）
    if (id != null && id != 0) {
        // 根据id查询分类
        Category category = getCategoryById(id);
        if (category != null) {
            helpQueryParentCategories(category.getParentId(), parentCategories);
            parentCategories.add(category);
        }
    }
}
```

## 多级评论

**把顶级评论下的所有子评论封装到顶级评论的List集合中，子评论设置其父评论**

评论表：

| id   | content | blog_id | parent_comment_id | create_time | commentator_type | username | avatar | email |
| ---- | ------- | ------- | ----------------- | ----------- | ---------------- | -------- | ------ | ----- |
| 主键 | 内容    | 博客id  | 父评论id          | 创建时间    | 评论者类型       | 用户名   | 头像   | 邮箱  |

```java
/**
  * 把comment集合转化为Vo集合，并对每个顶级comment进行封装，填充其子comment
  * @param comments
  * @param maxLevelComments
  * @param pageInfo
  * @return
  */
private PageResult<CommentVo> getFirstLevelCommentVos(List<Comment> comments, List<Comment> maxLevelComments, PageInfo<Comment> pageInfo) {
    // 把comments转化成commentVos，过滤掉顶级评论
    List<CommentVo> commentVos = comments.stream()
        .filter(comment -> comment.getParentCommentId() != 0)
        .map(comment -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            // 查询当前评论对应的博客
            Blog blog = blogService.getById(comment.getBlogId());
            if (blog != null) {
                // 设置博客标题
                commentVo.setTitle(blog.getTitle());
            }
            return commentVo;
        }).collect(Collectors.toList());

    // 把最顶级comment转化为对应的vo
    List<CommentVo> firstLevelCommentVos = maxLevelComments.stream()
        .map(comment -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            // 查询当前评论对应的博客
            Blog blog = blogService.getById(comment.getBlogId());
            if (blog != null) {
                // 设置博客标题
                commentVo.setTitle(blog.getTitle());
            }
            return commentVo;
        }).collect(Collectors.toList());

    // 合并评论的各层子代到第一级子代集合中
    combineChildren(firstLevelCommentVos, commentVos);

    // 排序
    firstLevelCommentVos.forEach(firstLevelCommentVo -> {
        firstLevelCommentVo.getReplyComments().sort(new Comparator<CommentVo>() {
            @Override
            public int compare(CommentVo o1, CommentVo o2) {
                long time1 = o1.getCreateTime().getTime();
                long time2 = o2.getCreateTime().getTime();
                return Long.compare(time2, time1);
            }
        });
    });
    return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), firstLevelCommentVos, pageInfo.getPageNum());
}

//存放迭代找出的所有子代的集合
private List<CommentVo> tempReplies = new ArrayList<>();

private void combineChildren(List<CommentVo> firstLevelCommentVos, List<CommentVo> allComments) {
    // 遍历所有一级评论
    for (CommentVo commentVo : firstLevelCommentVos) {
        // 过滤出一级评论的直接子评论
        List<CommentVo> replyComments = allComments.stream()
            .filter(comment -> comment.getParentCommentId().equals(commentVo.getId()))
            .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(replyComments)) {
            // 遍历所有的子评论
            for (CommentVo commentChild : replyComments) {
                // 设置父评论
                CommentVo newComment = new CommentVo();
                BeanUtils.copyProperties(commentVo, newComment);
   // 直接设置commentVo会报错Infinite recursion (StackOverflowError)  through reference chain
                commentChild.setParentComment(newComment);
                // 子评论添加到临时存放集合
   			   tempReplies.add(commentChild);
                //循环迭代，找出子代，存放在tempReplies中
                recursively(commentChild, allComments);

            }
        }
        //修改顶级节点的children集合为迭代处理后的集合
        commentVo.setReplyComments(tempReplies);
        //清除临时存放区
        tempReplies = new ArrayList<>();
    }
}

/**
 * 递归迭代，剥洋葱
 * @param commentVo 被迭代的对象
 * @return
 */
private void recursively(CommentVo commentVo, List<CommentVo> allComments) {
    // 过滤出直接子评论
    List<CommentVo> replyComments = allComments.stream()
        .filter(comment -> comment.getParentCommentId().equals(commentVo.getId()))
        .collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(replyComments)) {
        for (CommentVo reply : replyComments) {
            // 设置父评论
            reply.setParentComment(commentVo);
            tempReplies.add(reply);
            List<CommentVo> replyComments2 = allComments.stream()
                .filter(comment -> comment.getParentCommentId().equals(reply.getId()))
                .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(replyComments2)) {
                recursively(reply, allComments);
            }
        }
    }
}
```

