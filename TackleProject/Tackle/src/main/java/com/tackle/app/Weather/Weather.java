package com.tackle.app.Weather;

/**
 * Created by Bill on 11/5/13.
 */
public class Weather {

    private int weatherId;
    private String condition;
    private String description;

    public int getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getDescr() {
        return description;
    }
    public void setDescr(String descr) {
        this.description = descr;
    }

}
