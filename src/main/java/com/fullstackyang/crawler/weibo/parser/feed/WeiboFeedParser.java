package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.NormalWeiboFeed;
import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Slf4j
public class WeiboFeedParser extends WeiboBaseParser {

    private Map<String, String> htmlMap;

    public WeiboFeedParser(final String url, final String html) {
        super(url, html, new FeedContentHandler(),
                new FeedPubTimeHandler(),
                new FeedOidHandler(),
                new FeedMidHandler(),
                new FeedUrlHandler(),
                new FeedHasOriginHandler(),
                new FeedForwardCountHandler(),
                new FeedNicknameHandler());
        init();
    }

    public WeiboFeedParser(final String url, final String html, WeiboFeedHandler... handlers) {
        super(url, html, handlers);
        init();
    }

    private void init() {
        if (Strings.isNullOrEmpty(html))
            return;
        this.htmlMap = getPLHTML(html, Pl_OFFICIAL_MyProfileFeed);
    }

    public List<WeiboFeed> parse() {
        String html = htmlMap.get(Pl_OFFICIAL_MyProfileFeed);
        if (html == null) {
            log.error(this.url+" cannot get html!");
            return Lists.newArrayList();
        }
        Document document = Jsoup.parse(html);
        Elements feedItems = document.select("div[action-type=feed_list_item]");
        return feedItems.stream().map(e -> toWeiboFeed(new NormalWeiboFeed(), e)).collect(toList());
    }

    public WeiboFeed toWeiboFeed(WeiboFeed weiboFeed, Element element) {
        weiboFeed.getAllFields().stream().map(f ->
                getHanlder(f.getName())).filter(Objects::nonNull).forEach(p -> ((WeiboFeedHandler) p).parse(weiboFeed, element));
        return weiboFeed;
    }
}
