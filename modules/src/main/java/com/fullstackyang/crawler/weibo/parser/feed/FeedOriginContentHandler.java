package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.jsoup.nodes.Element;

public class FeedOriginContentHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element contentElement = element.select(".WB_text").first();
        if (contentElement != null) {
            contentElement.select("a[action-type=feed_list_url]").remove();
            String content = contentElement.text().replaceAll("\\?\\?\\?\\?", "");
            weiboFeed.setContent(content);
        }
        boolean more = element.select("div[node-type=feed_list_forwardContent] a[action-type=fl_unfold]")
                .isEmpty();
        weiboFeed.setHasMore(!more);
    }

    @Override
    public String getFieldName() {
        return "content";
    }
}
