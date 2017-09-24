package com.fullstackyang.crawler.weibo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class NormalWeiboFeed extends WeiboFeed{

    private String oid; // 发布微博用户的pid

    private boolean isSticky; // 置顶
    private boolean isOrigin; // 是否原创

    private OriginWeiboFeed originWeibo;
    private boolean originRemoved; // 源博文 有可能被删除

}
