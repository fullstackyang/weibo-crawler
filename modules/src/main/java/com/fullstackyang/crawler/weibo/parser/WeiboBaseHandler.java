package com.fullstackyang.crawler.weibo.parser;

import org.jsoup.nodes.Element;

public interface WeiboBaseHandler<T> {

    String getFieldName();

    void parse(T t, Element element);
}
