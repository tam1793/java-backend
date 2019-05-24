/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection_pool;

import com.google.gson.GsonBuilder;
import static connection_pool.HttpURLConnection.Count;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.http.HttpMethod;

/**
 *
 * @author tamnnq
 */
public class HttpClientConnection {

    private static final Logger logger = Logger.getLogger(HttpClientConnection.class);
    private static final String http = "http://tamnnq.mto.zing.vn/demoService";
    private static final String https = "https://tamnnq.mto.zing.vn/demoService";
    private static final String params = "action=plus&number1=1&number2=2";
    private static int COUNT = 0;
    private static long TIME_MILLIS = 0;

    public static String objectToString(Object obj) {
        try {
            return new GsonBuilder().serializeNulls().create().toJson(obj);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }

    public static String curl(String url, HttpClient httpClient, HttpMethod method, List<NameValuePair> params) {
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.addParameters(params);

            HttpRequestBase request = null;
            switch (method) {
                case GET:
                    request = new HttpGet(builder.build());
                    break;
                case POST:
                    request = new HttpPost(builder.build());
                    break;
                default:
                    throw new Exception("Don't support request method:" + method);
            }
            request.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            StringBuilder sb = new StringBuilder();
            if (br != null) {
                String output;
                while ((output = br.readLine()) != null) {
                    if (!output.isEmpty()) {
                        sb.append(output);
                    }
                }
            }
            return sb.toString();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static synchronized void Count(Boolean count) {
        if (count == true) {
            COUNT++;
        } else {
            COUNT = 0;
        }
    }

    public static void main(String[] args) {
        try {
            String log4jFile = System.getProperty("apppath") + "/conf/log4j.ini";
            PropertyConfigurator.configure(log4jFile);

            PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
// Increase max total connection to 200
            pool.setMaxTotal(200);
// Increase default max connection per route to 20
            pool.setDefaultMaxPerRoute(10);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(pool)
                    .build();

            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("action", "plus"));
            params.add(new BasicNameValuePair("number1", "1"));
            params.add(new BasicNameValuePair("number2", "2"));

            for (int i = 0; i < 20; i++) {
                new Thread(new Runnable() {

                    public void run() {
                        try {

                            while (true) {
                                String response = curl(https, httpClient, HttpMethod.PUT, params);
                                Count(Boolean.TRUE);
                                logger.info(("response: " + response));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            while (true) {
                long time = System.currentTimeMillis();
                long spendTime = time - TIME_MILLIS;
                if (spendTime >= 1000) {
                    logger.info("COUNT REQUEST in " + spendTime + "mili: " + COUNT);
                    Count(Boolean.FALSE);
                    TIME_MILLIS = time;
                }
            }

//            httpClient.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
