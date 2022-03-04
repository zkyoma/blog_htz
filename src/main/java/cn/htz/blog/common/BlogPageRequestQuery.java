package cn.htz.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPageRequestQuery extends PageRequest {
    /**
     * 查询条件
     */
    private Map<String, Object> params;
    // /**
    //  * 是否推荐
    //  */
    // private Boolean recommend;
    // /**
    //  * 是否发布
    //  */
    // private Boolean publish;
    // /**
    //  * 是否赞赏
    //  */
    // private Boolean appreciation;
    // /**
    //  * 是否开启评论
    //  */
    // private Boolean comment;
    // /**
    //  * 是否原创
    //  */
    // private Boolean flag;
}
