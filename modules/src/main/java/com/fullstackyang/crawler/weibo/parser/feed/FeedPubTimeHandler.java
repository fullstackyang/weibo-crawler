package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import com.fullstackyang.crawler.weibo.utils.DateConvertor;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;

public class FeedPubTimeHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element timeElement = element.select("a[node-type=feed_list_item_date]").first();
        String time = timeElement.text();
        LocalDateTime datetime = DateConvertor.convert(time);
        weiboFeed.setPubTime(datetime);
    }

    @Override
    public String getFieldName() {
        return "pubTime";
    }
}
