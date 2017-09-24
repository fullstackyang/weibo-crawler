package com.fullstackyang.crawler.weibo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeiboUser extends AbstractDTO {

    private String nickname;

    private String url;

    private String pageid;

    public String getHomeUrl() {
        return "http://www.weibo.com/p/" + pageid + "/home";
    }
}
