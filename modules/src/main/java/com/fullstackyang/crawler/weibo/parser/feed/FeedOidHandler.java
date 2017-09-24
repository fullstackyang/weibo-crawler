package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.NormalWeiboFeed;
import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.jsoup.nodes.Element;

public class FeedOidHandler implements WeiboFeedHandler{

    /**
     * <p>出现在Feed中的微博不一定是当前微博账号所发布的，例如其点赞别人的weibo</p>
     * <p>oid记录该微博的原始博主</p>
     *
     * @param weiboFeed
     * @param element
     */
    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        String oid = element.attr("tbinfo");
        oid = oid.substring(oid.indexOf("ouid=") + 5, oid.contains("&") ? oid.indexOf("&") : oid.length());
        ((NormalWeiboFeed)weiboFeed).setOid(oid);

    }

    @Override
    public String getFieldName() {
        return "oid";
    }
}
