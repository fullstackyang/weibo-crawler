package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class FeedOriginForwardCountHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element forwardElement = element.select(".WB_func ul li a em.ficon_forward").first();
        if (forwardElement != null) {
            Element sibling = forwardElement.nextElementSibling();
            String text = sibling.text().trim();
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
