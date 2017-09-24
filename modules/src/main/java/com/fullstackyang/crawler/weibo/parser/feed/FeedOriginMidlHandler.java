package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.jsoup.nodes.Element;

public class FeedOriginMidlHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element midElement = element.select("div.WB_info a[suda-uatrack]").first();
        if (midElement != null) {
            String mid = midElement.attr("suda-uatrack");
            if (mid.contains(":")) {
                mid = mid.substring(mid.indexOf(":") + 1);
                weiboFeed.setMid(mid);
            }
        }
    }

    @Override
    public String getFieldName() {
        return "mid";
    }
}
