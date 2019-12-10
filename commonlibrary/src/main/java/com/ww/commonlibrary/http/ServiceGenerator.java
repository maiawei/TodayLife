package com.ww.commonlibrary.http;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.ww.commonlibrary.util.LogUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {
    public static final String USER_AGENT="Mozilla/5.0 (Linux; Android 8.1.0; 1809-A01 Build/OPM1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/73.0.3683.90 Mobile Safari/537.36 JsSdk/2 NewsArticle/6.4.8 NetType/wifi";

    public static final String REFERER="http://lf.snssdk.com/api/2/wap/search/?from=search_tab&keyword=&plugin_enable=3&followbtn_template=%7B%22color_style%22%3A%22red%22%7D&iid=89771572818&device_id=60654663937&ac=wifi&channel=qiku_wy_yz1&aid=13&app_name=news_article&version_code=648&version_name=6.4.8&device_platform=android&ab_version=1103034%2C1265620%2C662176%2C665174%2C643894%2C649428%2C1268688%2C801968%2C707372%2C775823%2C1263389%2C661898%2C668775%2C1278719%2C1231418%2C1276097%2C1190525%2C1177116%2C668779%2C1251924%2C662099%2C1269601%2C668774%2C1240388%2C1268953%2C1275273%2C765196%2C1276339%2C1259165%2C857804%2C1282114%2C679101%2C1095474%2C660830%2C1279188%2C1054755%2C1230782%2C1243993%2C1244004%2C661781%2C648315&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=94567%2C102750%2C181431&ab_feature=94567%2C102750&abflag=3&device_type=1809-A01&device_brand=360&language=zh&os_api=27&os_version=8.1.0&e_uuid=3d333c303d30353635343430353137&openudid=77eb6aff92f90f29&manifest_version_code=648&resolution=1080*2160&dpi=480&update_version_code=64809&_rticket=1573630712951&plugin=10590&rom_version=27&search_sug=1&forum=1";
    public static final Charset UTF8 = Charset.forName("UTF-8");
    static SSLContext sslContext;
    //ssl证书问题
    static {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }

    private static OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS).sslSocketFactory(sslContext.getSocketFactory()).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl("http://192.168.254.42:8901")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//决定你的返回值是Observable还是Call
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("MM/dd/yyyy HH:mm:ss").create()));

    public ServiceGenerator() throws NoSuchAlgorithmException {
    }

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        return createService(serviceClass, authToken, builder);
    }

    private static <S> S createService(Class<S> serviceClass, final String authToken, Retrofit.Builder builder) {
        httpClient.interceptors().clear();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .method(original.method(), original.body());
                requestBuilder.header("User-Agent", USER_AGENT);
                requestBuilder.header("Referer", REFERER);
                Request request = requestBuilder.header("User-Agent", removeChineseWord(System.getProperty("http.agent"))).build();
                Response response = chain.proceed(request);
                if (LogUtils.allowLog) {
                    RequestBody requestBody = request.body();
                    Connection connection = chain.connection();
                    Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
                    String requestStartMessage =
                            "--> " + request.method() + ' ' + requestPath(request.url()) + ' ' + protocol(protocol);
                    boolean hasRequestBody = requestBody != null;
                    if (hasRequestBody) {
                        requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
                    }
                    LogUtils.e("<-- " + requestStartMessage);

                    Headers headers = request.headers();
                    for (int i = 0, count = headers.size(); i < count; i++) {
                        LogUtils.e(headers.name(i) + ": " + headers.value(i));
                    }

                    String endMessage = "--> END " + request.method();
                    if (hasRequestBody) {
                        Buffer buffer = new Buffer();
                        requestBody.writeTo(buffer);

                        Charset charset = UTF8;
                        MediaType contentType = requestBody.contentType();
                        if (contentType != null) {
                            contentType.charset(UTF8);
                        }
                        LogUtils.e("");
                        LogUtils.e(buffer.readString(charset));
                        endMessage += " (" + requestBody.contentLength() + "-byte body)";
                    }
                    LogUtils.e(endMessage);

                    ResponseBody responseBody = response.body();
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }
                    if (responseBody.contentLength() != 0) {
                        LogUtils.e("");
                        LogUtils.e(buffer.clone().readString(charset));
                    }
                }
                return response;
            }
        });

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }

    private static String requestPath(HttpUrl url) {
        String path = url.encodedPath();
        String query = url.encodedQuery();
        return query != null ? (path + '?' + query) : path;
    }

    private static String removeChineseWord(String str) {
        if (str.length() == 0) {
            return "";
        }
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
    }

    /**
     * 创建带响应进度(下载进度)回调的service
     */
    public static <T> T createResponseService(Class<T> tClass, ProgressResponseListener listener) {
        return builder
                .client(HttpClientHelper.addProgressResponseListener(listener))
                .build()
                .create(tClass);
    }


    /**
     * 创建带请求体进度(上传进度)回调的service
     */
    public static <T> T createRequestService(Class<T> tClass, ProgressRequestListener listener) {
        return builder
                .client(HttpClientHelper.addProgressRequestListener(listener))
                .build()
                .create(tClass);
    }


    public static Retrofit retrofit() {
        return builder.client(httpClient.build()).build();
    }
}
