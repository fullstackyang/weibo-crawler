package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboDetail;
import com.google.common.base.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.Objects;

public class WeiboDetailParser extends WeiboBaseParser {

    private Map<String, String> htmlMap;

    public WeiboDetailParser(final String url,final String html) {
        super(url,html, new DetailContentHandler());
        init();
    }

    private void init() {
        if (Strings.isNullOrEmpty(html))
            return;
        this.htmlMap = getPLHTML(html, Pl_OFFICIAL_WEIBODETAIL);
    }

    public WeiboDetail parse() {
        String html = htmlMap.get(Pl_OFFICIAL_WEIBODETAIL);
        Document document = Jsoup.parse(html);
        return toWeiboDetail(new WeiboDetail(), document);
    }

    public WeiboDetail toWeiboDetail(WeiboDetail weiboDetail, Element element) {
        weiboDetail.getAllFields().stream().map(f ->
                getHanlder(f.getName())).filter(Objects::nonNull).forEach(p -> ((WeiboDetailHandler) p).parse(weiboDetail, element));
        return weiboDetail;
    }
}
