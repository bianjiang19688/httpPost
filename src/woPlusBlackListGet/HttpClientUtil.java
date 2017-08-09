package woPlusBlackListGet;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class HttpClientUtil {
    @SuppressWarnings("deprecation")
    public static String sendSSLPostRequest(String param, String url,
                                            String authorization) throws Exception {
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        // 获取一个SSLContext实例
        SSLContext sslcontext = SSLContext.getInstance("SSL");

        // 初始化一个SSLContext实例
        sslcontext.init(null, new TrustManager[] { trustManager }, null);
        SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getConnectionManager().getSchemeRegistry()
                .register(new Scheme("https", 443, sf));

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", authorization);     //认证token
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setHeader("Accept", "application/json;charset=UTF-8");
        StringEntity reqEntity = new StringEntity(param,"UTF-8");
        httpPost.setEntity(reqEntity);
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity resEntity = response.getEntity();
        return EntityUtils.toString(resEntity, "UTF-8");
    }
}
