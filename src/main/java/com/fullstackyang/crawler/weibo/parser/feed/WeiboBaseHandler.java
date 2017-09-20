package com.fullstackyang.crawler.weibo.parser.feed;

import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public interface WeiboBaseHandler<T> {

    String getFieldName();

    void parse(T t, Element element);
}
