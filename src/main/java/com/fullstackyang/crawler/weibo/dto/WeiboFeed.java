package com.fullstackyang.crawler.weibo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.text.StringEscapeUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false, of = {"mid", "url"})
public abstract class WeiboFeed extends AbstractDTO {

    protected String nickname;
    protected String mid; // feed唯一标识
    protected String url;
    protected String content; // 内容
    @JsonIgnore
    protected LocalDateTime pubTime;

    protected int forwardCount=0;

    protected boolean hasMore;//展开正文

    public String getDisplayPubTime(){
        return this.pubTime.toLocalDate().toString()+" "+this.pubTime.toLocalTime().toString();
    }


}
