package com.fullstackyang.crawler.weibo.client;

import com.fullstackyang.httpclient.HttpClientInstance;
import com.fullstackyang.httpclient.HttpRequestUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 微博免登陆请求客户端
 *
 * @author fullstackyang
 */
@Slf4j
public class WeiboClient {

    private static CookieFetcher cookieFetcher = new CookieFetcher();

    private volatile String cookie;

    public WeiboClient() {
        this.cookie = cookieFetcher.getCookie();
    }

    private static Lock lock = new ReentrantLock();

    /**
     * when getting html failed, call this to get a new cookie and try again
     */
    public void cookieReset() {
        if (lock.tryLock()) {
            try {
                HttpClientInstance.instance().changeProxy();
                this.cookie = cookieFetcher.getCookie();
                log.info("cookie :" + cookie);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * new WeiboClient().get("http://weibo.com");
     * @param url
     * @return
     */
    public String get(String url) {
        if (Strings.isNullOrEmpty(url))
            return "";

        while (true) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader(HttpHeaders.COOKIE, cookie);
            httpGet.addHeader(HttpHeaders.HOST, "weibo.com");
            httpGet.addHeader("Upgrade-Insecure-Requests", "1");

            httpGet.setConfig(RequestConfig.custom().setSocketTimeout(3000)
                    .setConnectTimeout(3000).setConnectionRequestTimeout(3000).build());
            String html = HttpClientInstance.instance().tryExecute(httpGet, null, null);
            if (html == null)
                cookieReset();
            else return html;
        }
    }


    /**
     * 获取访问微博时必需的Cookie
     */
    @NoArgsConstructor
    static class CookieFetcher {

        static final String PASSPORT_URL = "https://passport.weibo.com/visitor/visitor?entry=miniblog&a=enter&url=http://weibo.com/?category=2"
                + "&domain=.weibo.com&ua=php-sso_sdk_client-0.6.23";

        static final String GEN_VISITOR_URL = "https://passport.weibo.com/visitor/genvisitor";

        static final String VISITOR_URL = "https://passport.weibo.com/visitor/visitor?a=incarnate";

        private String getCookie() {
            Map<String, String> map;
            while (true) {
                map = getCookieParam();
                if (map.containsKey("SUB") && map.containsKey("SUBP") &&
                        StringUtils.isNoneEmpty(map.get("SUB"), map.get("SUBP")))
                    break;
                HttpClientInstance.instance().changeProxy();
            }
            return " YF-Page-G0=" + "; _s_tentry=-; SUB=" + map.get("SUB") + "; SUBP=" + map.get("SUBP");
        }

        private Map<String, String> getCookieParam() {
            String time = System.currentTimeMillis() + "";
            time = time.substring(0, 9) + "." + time.substring(9, 13);
            String passporturl = PASSPORT_URL + "&_rand=" + time;

            String tid = "";
            String c = "";
            String w = "";
            {
                String str = postGenvisitor(passporturl);
                if (str.contains("\"retcode\":20000000")) {
                    JSONObject jsonObject = new JSONObject(str).getJSONObject("data");
                    tid = jsonObject.optString("tid");
                    try {
                        tid = URLEncoder.encode(tid, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                    c = jsonObject.has("confidence") ? "000" + jsonObject.getInt("confidence") : "100";
                    w = jsonObject.optBoolean("new_tid") ? "3" : "2";
                }
            }
            String s = "";
            String sp = "";
            {
                if (StringUtils.isNoneEmpty(tid, w, c)) {
                    String str = getVisitor(tid, w, c, passporturl);
                    str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
                    if (str.contains("\"retcode\":20000000")) {
                        JSONObject jsonObject = new JSONObject(str).getJSONObject("data");
                        s = jsonObject.getString("sub");
                        sp = jsonObject.getString("subp");
                    }

                }
            }
            Map<String, String> map = Maps.newHashMap();
            map.put("SUB", s);
            map.put("SUBP", sp);
            return map;
        }

        private String postGenvisitor(String passporturl) {

            Map<String, String> headers = Maps.newHashMap();
            headers.put(HttpHeaders.ACCEPT, "*/*");
            headers.put(HttpHeaders.ORIGIN, "https://passport.weibo.com");
            headers.put(HttpHeaders.REFERER, passporturl);

            Map<String, String> params = Maps.newHashMap();
            params.put("cb", "gen_callback");
            params.put("fp", fp());

            HttpPost httpPost = HttpRequestUtils.createHttpPost(GEN_VISITOR_URL, headers, params);

            String str = HttpClientInstance.instance().execute(httpPost, null);
            return str.substring(str.indexOf("(") + 1, str.lastIndexOf(""));
        }

        private String getVisitor(String tid, String w, String c, String passporturl) {
            String url = VISITOR_URL + "&t=" + tid + "&w=" + "&c=" + c.substring(c.length() - 3)
                    + "&gc=&cb=cross_domain&from=weibo&_rand=0." + rand();

            Map<String, String> headers = Maps.newHashMap();
            headers.put(HttpHeaders.ACCEPT, "*/*");
            headers.put(HttpHeaders.HOST, "passport.weibo.com");
            headers.put(HttpHeaders.COOKIE, "tid=" + tid + "__0" + c);
            headers.put(HttpHeaders.REFERER, passporturl);

            HttpGet httpGet = HttpRequestUtils.createHttpGet(url, headers);
            httpGet.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
            return HttpClientInstance.instance().execute(httpGet, null);
        }

        private static String rand() {
            return new BigDecimal(Math.floor(Math.random() * 10000000000000000L)).toString();
        }

        private static String fp() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("os", "1");
            jsonObject.put("browser", "Chrome59,0,3071,115");
            jsonObject.put("fonts", "undefined");
            jsonObject.put("screenInfo", "1680*1050*24");
            jsonObject.put("plugins",
                    "Enables Widevine licenses for playback of HTML audio/video content. (version: 1.4.8.984)::widevinecdmadapter.dll::Widevine Content Decryption Module|Shockwave Flash 26.0 r0::pepflashplayer.dll::Shockwave Flash|::mhjfbmdgcfjbbpaeojofohoefgiehjai::Chrome PDF Viewer|::internal-nacl-plugin::Native Client|Portable Document Format::internal-pdf-viewer::Chrome PDF Viewer");
            return jsonObject.toString();
        }
    }
}
