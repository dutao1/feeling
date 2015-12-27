package com.feeling.utils.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

public class  HttpConnectionManager{
 
    static DefaultHttpClient httpClient  =null;
    static int MAX_PER_ROUTE = 40;  //客户端总并行链接最大数    
    static int MAX_TOTAL = 200;  //每个主机的最大并行链接数   
    static int CONNECTION_TIMEOUT = 5 * 1000; //设置请求超时5秒钟 根据业务调整
    static int SO_TIMEOUT = 5 * 1000; //设置等待数据超时时间5秒钟 根据业务调整 
    static long CONN_MANAGER_TIMEOUT = 1000; //该值就是连接不够用的时候等待超时时间，一定要设置，而且不能太大 ()
    static String HTTP = "http";
    static String HTTPS = "https";
    static String TLS = "TLS";
    /** 闲置连接超时时间, 由bean factory设置，缺省为120秒钟 */
    static int DEFAULT_IDLE_TIMEOUT = 120000;
    static IdleConnectionMonitorThread idleEvictThread;
    static PoolingClientConnectionManager pccm =null;
    static{
        // 设置组件参数, HTTP协议的版本,1.1/1.0/0.9   
        HttpParams params = new BasicHttpParams();   
       /* HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);   
        HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");   
        HttpProtocolParams.setUseExpectContinue(params, true); */   
        //params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);    
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);   
        params.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, CONN_MANAGER_TIMEOUT);
        //设置访问协议   
        X509TrustManager xtm = new X509TrustManager() {

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
        SchemeRegistry schreg = new SchemeRegistry();   
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance(TLS);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ctx.init(null, new TrustManager[] { xtm }, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
        socketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
        schreg.register(new Scheme(HTTP,80,PlainSocketFactory.getSocketFactory()));   
        schreg.register(new Scheme(HTTPS, 443, socketFactory));         
        
        //多连接的线程安全的管理器   
        pccm = new PoolingClientConnectionManager(schreg);  
        pccm.setDefaultMaxPerRoute(MAX_PER_ROUTE); 
        pccm.setMaxTotal(MAX_TOTAL);    
        idleEvictThread = new IdleConnectionMonitorThread(pccm);
        idleEvictThread.start();
        httpClient = new DefaultHttpClient(pccm, params);  
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }
    public static void shutdown() {
        idleEvictThread.shutdown();
        pccm.shutdown();
    }
    /**
     * 守护线程，定时清理过期和空闲时间超时的连接
     */
    private static class IdleConnectionMonitorThread extends Thread {

        private final ClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(ClientConnectionManager connMgr) {
            this.connMgr = connMgr;
            this.setDaemon(true);// 守护线程
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // Close expired connections
                        connMgr.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 30 sec
                        connMgr.closeIdleConnections(DEFAULT_IDLE_TIMEOUT,
                                TimeUnit.MILLISECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            if (!shutdown) {
                shutdown = true;
                synchronized (this) {
                    notifyAll();
                }
            }
        }

    }
}