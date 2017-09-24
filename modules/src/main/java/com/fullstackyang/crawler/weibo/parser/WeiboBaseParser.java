package com.fullstackyang.crawler.weibo.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class WeiboBaseParser {

    public static final String PL_CORE_USER_INFO = "Pl_Core_UserInfo__";

    public static final String PL_CORE_T8_CUSTOM_TRI_COLUMN = "Pl_Core_T8CustomTriColumn__";

    public static final String PL_OFFICIAL_HEADERV6 = "Pl_Official_Headerv6__";

    public static final String PL_OFFICIAL_RIGHT_GROW_NEW = "Pl_Official_RightGrowNew__";

    public static final String PL_OFFICIAL_PERSONAL_INFO = "Pl_Official_PersonalInfo__";

    public static final String Pl_OFFICIAL_MyProfileFeed = "Pl_Official_MyProfileFeed__";

    public static final String Pl_OFFICIAL_WEIBODETAIL = "Pl_Official_WeiboDetail__";

    private final Map<String, WeiboBaseHandler> handlerMap;

    protected String url;
    protected String html;

    @Getter
    protected JSONObject configObject;

    public WeiboBaseParser(final String url, final String html, final WeiboBaseHandler... handlers) {
        this.url = url;
        setHtml(html);
        this.handlerMap = Maps.newConcurrentMap();
        for (final WeiboBaseHandler handler : handlers) {
            handlerMap.put(handler.getFieldName(), handler);
        }
    }

    public void setHtml(String html) {
        if (html != null) {
            this.html = escape(html);
            this.configObject = getHTMLConfig(this.html);
        }
    }

    private String escape(String html) {
        return html.replaceAll("\\\\\"", "\"")
                .replaceAll("\\\\/", "/")
                .replaceAll("\\\\r|\\\\n|\\\\t|\\u200B|\\u200b", "");

    }

    public void showConfig() {
        log.info(configObject.toString(2));
    }

    public boolean checkConfig() {
        return html != null && !configObject.isNull("page_id") && configObject.has("page_id");
    }

    /**
     * 每一个微博页面基本上都有CONFIG，其中包含了page_id,oid等基本信息
     *
     * @param html
     * @return
     */
    public JSONObject getHTMLConfig(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("script[type=text/javascript]");
        JSONObject jsonObject = new JSONObject();
        if (elements.isEmpty())
            return jsonObject;

        String text = "";
        for (Element element : elements) {
            String temp = element.html();
            if (temp.contains("$CONFIG")) {
                text = temp;
                break;
            }
        }
        String[] parts = text.split(";");
        for (String str : parts) {
            if (!str.contains("="))
                continue;
            String[] strings = str.trim().split("=");
            String key = strings[0].replaceAll("\\$CONFIG|\\[|\\]|'", "");
            String value = strings[1].replaceAll("'", "").trim();
            jsonObject.put(key, value + "");
        }
        return jsonObject;
    }

    /**
     * 抽取微博页面底部pl开头的各种子页面
     *
     * @param html
     * @param pl
     * @return
     */
    public static Map<String, String> getPLHTML(String html, String... pl) {
        Map<String, String> map = Maps.newHashMap();
        List<String> pls = Lists.newArrayList(pl);
        Document document = Jsoup.parse(html);
        Elements elements = document.select("script");
        if (elements.isEmpty())
            return map;
        for (Element element : elements) {
            String text = element.html();
            if (!text.startsWith("FM.view"))
                continue;
            for (String domid : pls) {
                String str = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"));

                if (str.contains("\"domid\":\"" + domid)) {
                    Pattern pattern = Pattern.compile("\"domid\":\"" + domid + ".+}?");
                    Matcher matcher = pattern.matcher(str);
                    if (matcher.find()) {
                        String value = matcher.group();
                        if (value.contains("\"html\":\"")) {
                            value = value.substring(value.indexOf("html\":\"") + 7, value.lastIndexOf("}") - 1);
                            map.put(domid, value);
                            pls.remove(domid);
                        }
                    }
                    break;
                }
            }
        }
        return map;
    }

    protected WeiboBaseHandler getHanlder(final String name) {
        return this.handlerMap.get(name);
    }

}
