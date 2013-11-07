package com.tackle.app.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bill on 11/5/13.
 */
public class JSONWeatherParser {

    public Weather getWeather(String data, int index) throws JSONException {

        int dayIndex = index;

        Weather weather = new Weather();

        JSONObject jObj = new JSONObject(data);

        JSONArray list = jObj.getJSONArray("list");

        JSONObject holder = list.getJSONObject(dayIndex);

        JSONArray weatherArray = holder.getJSONArray("weather");

        JSONObject jWeather = weatherArray.getJSONObject(0);

        weather.setCondition(getString("main", jWeather));
        weather.setDescr(getString("description", jWeather));
        weather.setWeatherId(getInt("id", jWeather));

        return weather;

    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}
