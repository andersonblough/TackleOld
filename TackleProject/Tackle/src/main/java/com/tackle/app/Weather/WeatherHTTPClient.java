package com.tackle.app.Weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bill on 11/5/13.
 */
public class WeatherHTTPClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private static String END_URL = "&cnt=5&mode=json&APPID=77f5881a8a9cdd3da3b1e38fa4bcd9cc";

    public String getWeatherData(String location){
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection)( new URL(BASE_URL + location + END_URL)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            br.close();
            return buffer.toString();
        }
        catch (Throwable t){
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch (Throwable t) {}
            try { con.disconnect(); } catch (Throwable t) {}
        }

        return null;
    }
}
