/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import core.config.Config;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author tam
 */
public class ConfigApp {

    //web_server
    public static String WEB_SERVER_HOST;
    public static Integer WEB_SERVER_PORT;
    public static Integer WEB_SERVER_PORT_HTTPS;
    public static String WEB_SERVER_CONTEXT_PATH;
    public static String LOGIN_SECRET_KEY;
    //SSL
    public static String KEY_STORE_PATH;
    public static String TRUST_STORE_PATH;

    public static String KEY;

    public ConfigApp() {
        WEB_SERVER_HOST = Config.getParam("web_server", "host");
        WEB_SERVER_PORT = Integer.parseInt(Config.getParam("web_server", "port_http"));
        WEB_SERVER_PORT_HTTPS = Integer.parseInt(Config.getParam("web_server", "port_https"));

        WEB_SERVER_CONTEXT_PATH = Config.getParam("web_server", "context_path");
        LOGIN_SECRET_KEY = Config.getParam("web_server", "secret_key");

        KEY_STORE_PATH = Config.getParam("ssl", "key_store_path");
        TRUST_STORE_PATH = Config.getParam("ssl", "trust_store_path");

        KEY = Config.getParam("ssl", "key");

    }

    public static void init() {
        String log4jFile = System.getProperty("apppath") + "/conf/log4j.ini";

        PropertyConfigurator.configure(log4jFile);
        new ConfigApp();
    }
}
