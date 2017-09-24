package com.fullstackyang.crawler.weibo.dto.converter;

import com.fullstackyang.crawler.weibo.dto.NormalWeiboFeed;
import com.fullstackyang.crawler.weibo.dto.WeiboDetail;
import com.fullstackyang.crawler.weibo.dto.WeiboFeed;
import com.google.common.base.Converter;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class DetailFeedConverter extends Converter<WeiboFeed, WeiboDetail> {
    @Override
    public WeiboDetail doForward(WeiboFeed weiboFeed) {
        WeiboDetail weiboDetail = new WeiboDetail();
        BeanUtilsBean beanUtilsBean=BeanUtilsBean.getInstance();
        beanUtilsBean.getConvertUtils().register(new DateConverter(null),LocalDateTime.class);
        BeanUtils.copyProperties(weiboFeed, weiboDetail);

        return weiboDetail;
    }

    @Override
    public WeiboFeed doBackward(WeiboDetail weiboDetail) {
        WeiboFeed weiboFeed = new NormalWeiboFeed();
        BeanUtils.copyProperties( weiboDetail,weiboFeed);
        return weiboFeed;
    }
}
