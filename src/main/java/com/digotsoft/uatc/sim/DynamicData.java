package com.digotsoft.uatc.sim;

import com.digotsoft.uatc.util.StringCallable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class DynamicData {

    public static void findRoute( String dep, String dest, StringCallable callable) {
        new Thread(() -> {
            String airac = "1801";
            String url = "http://vau.aero/route/getRoute.php?multi&dep=" + dep + "&nats=&airac=" + airac + "&dest=" + dest + "&rt=&lvl=H&transit=&transit1=&transit2=&omit=&omit1=&omit2=";
            String data = getHTML(url);
    
            String[] splitted = data.split("\\|");
    
            callable.call( splitted[3].split("\\+")[1] );
        }).start();
    }

    public static String getHTML(String urlToRead) {
        try
        {
            URLConnection connection = new URL(urlToRead).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            BufferedReader r  = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        return null;
    }


}
