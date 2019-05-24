/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection_pool;

import static connection_pool.HttpClientConnection.Count;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.http.HttpMethod;

/**
 *
 * @author tamnnq
 */
public class HttpURLConnection {

    private static Logger logger = Logger.getLogger(HttpURLConnection.class);
    private static String http = "http://tamnnq.mto.zing.vn/demoService";
    private static String https = "https://tamnnq.mto.zing.vn/demoService";
//        private static String http = "http://localhost:9090/demoService";
//    private static String https = "https://127.0.0.1:9091/demoService";
    private static String params = "action=plus&number1=1&number2=2";
    private static int COUNT = 0;
    private static long TIME_MILLIS = 0;

    public static String curl(String urlStr, HttpMethod method, String param, ContentType contentType,
            Boolean useProxy, Integer connectTimeout, Integer readTimeout) {
        long startTime = System.currentTimeMillis();
        try {
            if (HttpMethod.GET == method) {
                urlStr += ("?" + param);
            }
            URL url = new URL(urlStr);
            String proxyHost = System.getProperty("http.proxyHost");
            String proxyPort = System.getProperty("http.proxyPort");

            java.net.HttpURLConnection conn;
            if (useProxy == null) {
                useProxy = true;
            }
            if (useProxy && proxyHost != null && proxyPort != null) {
                try {
                    int port = Integer.parseInt(proxyPort);
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
                    conn = (java.net.HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    logger.error("Proxy " + proxyHost + " didn't works!. " + e.getMessage(), e);
                    conn = (java.net.HttpURLConnection) url.openConnection();
                }
            } else {
                conn = (java.net.HttpURLConnection) url.openConnection();
            }
            conn.setRequestProperty("Content-Type", contentType.getMimeType());

            conn.setReadTimeout(readTimeout == null ? 2000 : readTimeout); // Milliseconds
            conn.setConnectTimeout(connectTimeout == null ? 2000 : connectTimeout); // Milliseconds
            conn.setRequestMethod(method.name());
            conn.setDoOutput(true); // Triggers POST.
            if (HttpMethod.POST == method || HttpMethod.PUT == method) {
                try (OutputStream output = conn.getOutputStream()) {
                    output.write(param.getBytes());
                }
            }
            int code = conn.getResponseCode();

            BufferedReader br = null;
            if (code / 100 == 2) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    br = new BufferedReader(new InputStreamReader(errorStream));
                }
            }

            StringBuilder sb = new StringBuilder();
            if (br != null) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        sb.append(line);
                    }
                }
                br.close();
            }

//            logger.info("Curl: " + url + " - Param: " + param
//                    + " - ExecTime: " + (System.currentTimeMillis() - startTime)
//                    + " - Code: " + code + " - Result: " + sb.toString());
            return sb.toString();

        } catch (Exception e) {
            logger.error("Curl error: " + urlStr + " - Param: " + param
                    + " - ExecTime: " + (System.currentTimeMillis() - startTime)
                    + " - Exception: " + e.getMessage(), e);
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

    public static class CreateThread extends Thread {

        String name;
        String url;

        public CreateThread(String name, String url) {
            this.name = name;
            this.url = url;
        }

        @Override
        public void run() {
            while (true) {
                String response = HttpURLConnection.curl(url, HttpMethod.GET, params, ContentType.APPLICATION_FORM_URLENCODED, null, null, null);
//                logger.info(name + " - " + response);
                Count(Boolean.TRUE);
            }
        }
    }

    public static void main(String[] args) {
        String log4jFile = System.getProperty("apppath") + "/conf/log4j.ini";
        PropertyConfigurator.configure(log4jFile);

        String url = https;
        TIME_MILLIS = System.currentTimeMillis();

        for (int i = 0; i < 20; i++) {
            CreateThread thread = new HttpURLConnection.CreateThread("Thread" + i, url);
            thread.start();
        }

        while (true) {
           long time = System.currentTimeMillis();
            long spendTime = time - TIME_MILLIS;
            if (spendTime >= 1000) {
                logger.info("COUNT REQUEST in "+spendTime+"mili: " + COUNT);
                Count(Boolean.FALSE);
                TIME_MILLIS = time;
            }
        }

    }

}
