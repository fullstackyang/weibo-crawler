package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import com.fullstackyang.crawler.weibo.utils.DateConvertor;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class FeedOriginPubTimeHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element dateElement = element.select(".WB_func .WB_from a[node-type=feed_list_item_date]").first();
        if (dateElement != null) {
            String text = dateElement.text().trim();
            weiboFeed.setPubTime(DateConvertor.convert(text));
        }
    }

    @Override
    public String getFieldName() {
        return "pubTime";
    }
}
