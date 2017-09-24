package com.fullstackyang.crawler.weibo.parser.detail;

import com.fullstackyang.crawler.weibo.dto.WeiboDetail;
import org.jsoup.nodes.Element;

public class DetailContentHandler implements WeiboDetailHandler {

    @Override
    public void parse(WeiboDetail weiboDetail, Element element) {
        Element contentElement = element.select("div.WB_feed_detail div[node-type=feed_list_content]").first();
        if (contentElement != null) {
            weiboDetail.setDetail(contentElement.text().trim());
        }
    }

    @Override
    public String getFieldName() {
        return "detail";
    }
}
