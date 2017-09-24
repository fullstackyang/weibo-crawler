package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.jsoup.nodes.Element;

public class FeedNicknameHandler implements WeiboFeedHandler {

    @Override
    public void parse(WeiboFeed weiboFeed, Element element) {
        Element nicknameElement = element.select("div.WB_info a[suda-uatrack]").first();
        if (nicknameElement != null) {
            String nickname = nicknameElement.text();
            if (nickname.startsWith("@"))
                nickname = nickname.substring(1);
            weiboFeed.setNickname(nickname);
        }
    }

    @Override
    public String getFieldName() {
        return "nickname";
    }
}
