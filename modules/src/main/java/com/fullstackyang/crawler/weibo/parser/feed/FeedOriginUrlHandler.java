package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import com.google.common.base.Strings;
import org.jsoup.nodes.Element;

public class FeedOriginUrlHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element timeElement = element.select("a[node-type=feed_list_item_date]").first();
        if (timeElement != null) {
            String attr = timeElement.attr("href");
            String url = (!Strings.isNullOrEmpty(attr) && !attr.startsWith("http:")) ? "http://weibo.com" + attr : attr;
            weiboFeed.setUrl(url);
        }
    }

    @Override
    public String getFieldName() {
        return "url";
    }
}
