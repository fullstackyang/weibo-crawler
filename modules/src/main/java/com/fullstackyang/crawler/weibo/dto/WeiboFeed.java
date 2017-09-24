package com.fullstackyang.crawler.weibo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
