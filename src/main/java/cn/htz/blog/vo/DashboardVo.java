package cn.htz.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardVo {
    private Long readNum;
    private Long messagesCount;
    private Long commentsCount;
    private Long blogsCount;
    private Long categoriesCount;
    private Long tagsCount;
}
