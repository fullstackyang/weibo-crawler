package com.fullstackyang.crawler.weibo.parser.user;

import com.fullstackyang.crawler.weibo.dto.WeiboUser;
import com.fullstackyang.crawler.weibo.parser.feed.WeiboBaseHandler;
import org.jsoup.nodes.Element;

public class ForwardUserHandler implements WeiboUserHandler {

    @Override
    public String getFieldName() {
        return "url";
    }

    @Override
    public void parse(WeiboUser weiboUser, Element element) {
        Element userElement = element.select("div.WB_face a[usercard]").first();
        if (userElement != null)
            weiboUser.setUrl("http://weibo.com" + userElement.attr("href"));
    }
}
