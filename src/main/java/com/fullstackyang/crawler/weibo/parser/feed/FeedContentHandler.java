package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.NormalWeiboFeed;
import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.jsoup.nodes.Element;

public class FeedContentHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element contentElement = element.select("div[node-type=feed_list_content]").first();
        contentElement.select("a[action-type=feed_list_url]").remove();
        String content = contentElement.text().replaceAll("\\?\\?\\?\\?", "");
        if (content.startsWith("置顶")) {
            content=content.replace("置顶","").trim();
            ((NormalWeiboFeed)weiboFeed).setSticky(true);
        }
        weiboFeed.setContent(content);
        boolean more = element.select("div[node-type=feed_list_content] a[action-type=fl_unfold]").isEmpty();
        weiboFeed.setHasMore(!more);//正文不完整，需要点击展开
    }

    @Override
    public String getFieldName() {
        return "content";
    }
}
