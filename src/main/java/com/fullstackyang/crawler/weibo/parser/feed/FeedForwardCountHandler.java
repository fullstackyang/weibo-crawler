package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.NormalWeiboFeed;
import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class FeedForwardCountHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element forwardElement = element.select(".WB_feed_handle ul li a[action-type=fl_forward]").first();
        if (forwardElement != null) {
            Element em = forwardElement.select("em").last();

            String text = em.text().trim();
            if (!text.contains("转发") && StringUtils.isNumeric(text)) {
                weiboFeed.setForwardCount(Integer.parseInt(text));
            }

        }
    }

    @Override
    public String getFieldName() {
        return "forwardCount";
    }
}
