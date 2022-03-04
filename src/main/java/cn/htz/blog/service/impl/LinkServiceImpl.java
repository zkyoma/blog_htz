package cn.htz.blog.service.impl;

import cn.htz.blog.common.PageRequestQuery;
import cn.htz.blog.common.PageResult;
import cn.htz.blog.exception.ErrorEnum;
import cn.htz.blog.exception.MyException;
import cn.htz.blog.mapper.LinkMapper;
import cn.htz.blog.po.Link;
import cn.htz.blog.service.LinkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {
    @Autowired
    private LinkMapper linkMapper;

    @Override
    public PageResult<Link> selectPage(PageRequestQuery pageRequestQuery) {
        int pageSize = pageRequestQuery.getPageSize();
        int pageNum = pageRequestQuery.getPageNum();
        String key = pageRequestQuery.getKey();
        if (key != null) {
            key = key.trim();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Link> links = linkMapper.selectListByKey(key);
        PageInfo<Link> pageInfo = new PageInfo<>(links);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList(), pageNum);
    }

    @Transactional
    @Override
    public void save(Link link) {
        link.setId(null);
        link.setCreateTime(new Date());
        linkMapper.save(link);
    }

    @Override
    public Link getLinkById(Long id) {
        return linkMapper.getById(id);
    }

    @Transactional
    @Override
    public void update(Link link) {
        link.setCreateTime(null);
        linkMapper.update(link);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        linkMapper.updateShowStatusById(id);
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new MyException(ErrorEnum.PATH_NOT_FOUND);
        }
        linkMapper.updateShowStatusByIds(ids);
    }

    @Override
    public Map<Byte, List<Link>> getLinksForLinkPage() {
        List<Link> links = linkMapper.selectListByKey(null);
        if (!CollectionUtils.isEmpty(links)) {
            return links.stream().collect(Collectors.groupingBy(Link::getType));
        }
        return null;
    }
}
