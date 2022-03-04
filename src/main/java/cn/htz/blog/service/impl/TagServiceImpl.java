package cn.htz.blog.service.impl;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.mapper.BlogTagRelationMapper;
import cn.htz.blog.mapper.TagMapper;
import cn.htz.blog.po.Tag;
import cn.htz.blog.service.TagService;
import cn.htz.blog.vo.TagListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogTagRelationMapper blogTagRelationMapper;

    @Override
    public PageResult<Tag> selectPage(PageRequestQuery pageRequestQuery) {
        // 当前页
        int pageNum = pageRequestQuery.getPageNum();
        // 每页大小
        int pageSize = pageRequestQuery.getPageSize();
        // 搜索关键字
        String key = pageRequestQuery.getKey();
        if (key != null) {
            key = key.trim();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Tag> tags = tagMapper.selectListByKey(key);
        PageInfo<Tag> pageInfo = new PageInfo<>(tags);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList(), pageNum);
    }

    @Transactional
    @Override
    public void save(Tag tag) {
        tag.setId(null);
        tagMapper.save(tag);
    }

    @Override
    public Tag getTagById(Long id) {
        Tag tag = tagMapper.getById(id);
        if (tag == null) {
            throw new MyException("标签不存在：" + id, ErrorEnum.PATH_NOT_FOUND.getCode());
        }
        return tag;
    }

    @Transactional
    @Override
    public void update(Tag tag) {
        tagMapper.update(tag);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        tagMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new MyException("批量删除标签的id为空，ids = " + ids ,ErrorEnum.PATH_NOT_FOUND.getCode());
        }
        tagMapper.deleteByIds(ids);
    }

    @Override
    public List<Tag> selectList() {
        return tagMapper.selectListByKey(null);
    }

    @Override
    public Boolean checkName(String name) {
        Tag tag = tagMapper.getByName(name);
        return tag != null;
    }

    @Override
    public List<Tag> getTagsByIds(List<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) {
            return null;
        }
        return tagMapper.getTagsByIds(tagIds);
    }

    @Override
    public List<TagListVo> listTagTop(int limit) {
        List<Tag> allTags = tagMapper.selectListByKey(null);
        if (allTags == null) {
            return null;
        }
        List<TagListVo> tagList = allTags.stream()
                .map(tag -> {
                    TagListVo tagListVo = new TagListVo();
                    tagListVo.setId(tag.getId());
                    tagListVo.setName(tag.getName());
                    Long blogCount = blogTagRelationMapper.getBlogCountByTagId(tag.getId());
                    tagListVo.setBlogCount(blogCount);
                    return tagListVo;
                }).sorted((o1, o2) -> (int) (o2.getBlogCount() - o1.getBlogCount()))
                .collect(Collectors.toList());
        if (tagList.size() <= limit) {
            return tagList;
        }
        return tagList.subList(0, limit);
    }

    @Override
    public Long countTag() {
        return tagMapper.getTagCount();
    }
}
