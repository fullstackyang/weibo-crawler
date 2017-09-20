package com.fullstackyang.crawler.weibo.parser.user;

import com.fullstackyang.crawler.weibo.dto.NormalWeiboFeed;
import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import com.fullstackyang.crawler.weibo.dto.WeiboUser;
import com.fullstackyang.crawler.weibo.parser.feed.WeiboBaseParser;
import com.fullstackyang.crawler.weibo.parser.feed.WeiboFeedHandler;
import com.google.common.base.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class WeiboForwardParser extends WeiboBaseParser {

    private Map<String, String> htmlMap;

    public WeiboForwardParser(final String url, final String html) {
        super(url, html, new ForwardUserHandler());
    }

    public WeiboForwardParser(final String url, final String html, WeiboFeedHandler... handlers) {
        super(url, html, handlers);
    }


    public List<WeiboUser> parse() {
        Document document = Jsoup.parse(html);
        Elements feedItems = document.select("div[action-type=feed_list_item]");
        return feedItems.stream().map(e -> toWeiboUser(new WeiboUser(), e)).collect(toList());
    }

    public WeiboUser toWeiboUser(WeiboUser weiboUser, Element element) {
        weiboUser.getAllFields().stream().map(f ->
                getHanlder(f.getName())).filter(Objects::nonNull).forEach(p -> ((WeiboUserHandler) p).parse(weiboUser, element));
        return weiboUser;
    }
}
