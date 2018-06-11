package com.spil.dev.tms.Activity.Model;

import org.json.JSONObject;

/**
 * Created by andresual on 1/30/2018.
 */

public class ReturnModel {

    int statusCode;
    String message;
    JSONObject response;
    JSONObject obj;

    public ReturnModel(int statusCode, String message, JSONObject obj) {
        this.statusCode = statusCode;
        this.message = message;
        this.obj = obj;
    }

    public JSONObject getResponse() {
        return response;
    }

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }
}
