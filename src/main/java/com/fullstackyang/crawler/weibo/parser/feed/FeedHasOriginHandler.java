package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.NormalWeiboFeed;
import com.fullstackyang.crawler.weibo.dto.OriginWeiboFeed;
import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.jsoup.nodes.Element;

public class FeedHasOriginHandler implements WeiboFeedHandler {

    private WeiboFeedParser originParser = new WeiboFeedParser(null,null,
            new FeedOriginContentHandler(),
            new FeedOriginMidlHandler(),
            new FeedOriginPubTimeHandler(),
            new FeedOriginUrlHandler(),
            new FeedOriginForwardCountHandler(),
            new FeedNicknameHandler());

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        NormalWeiboFeed normalWeiboFeed = (NormalWeiboFeed) weiboFeed;
        normalWeiboFeed.setOrigin(!(element.hasAttr("isforward") && element.attr("isforward").equals("1")));
        if (!normalWeiboFeed.isOrigin()) {
            Element originElement = element.select("div[node-type=feed_list_forwardContent]").first();
            if (originElement != null) {
                Element originDateElement = originElement.select("a[node-type=feed_list_item_date]").first();
                if (originDateElement == null) {
                    normalWeiboFeed.setOriginRemoved(true);
                } else {
                    OriginWeiboFeed originWeibo = (OriginWeiboFeed) originParser.toWeiboFeed(new OriginWeiboFeed(), originElement);
                    normalWeiboFeed.setOriginWeibo(originWeibo);
                }
            }

        }
    }

    @Override
    public String getFieldName() {
        return "isOrigin";
    }
}
