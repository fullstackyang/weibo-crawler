package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.jsoup.nodes.Element;

public class FeedMidHandler implements WeiboFeedHandler {

    /**
     * mid为请求评论和转发时所需参数
     *
     * @param weiboFeed
     * @param element
     */
    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element midElement = element.select("div[action-type=feed_list_item]").first();
        if (midElement != null) {
            weiboFeed.setMid(midElement.attr("mid"));
        }
    }

    @Override
    public String getFieldName() {
        return "mid";
    }
}
