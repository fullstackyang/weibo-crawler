package com.fullstackyang.crawler.weibo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class WeiboDetail extends WeiboFeed {

    private String detail;
}
