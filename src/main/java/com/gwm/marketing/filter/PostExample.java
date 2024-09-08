package com.gwm.marketing.filter;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;


/**
 * httpclient rpc
 *
 * @param
 * @author fanht
 */
public class PostExample {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    final OkHttpClient client = new OkHttpClient();

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE'," + "'name':'Bowling'," + "'round':4," + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785," + "'players':[" + "{'name':'" + player1
                + "','history':[10,8,6,7,8],'color':-13388315,'total':39}," + "{'name':'" + player2
                + "','history':[6,10,5,10,10],'color':-48060,'total':41}" + "]}";
    }

    public static void main(String[] args) throws IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");
        JSONObject content = new JSONObject();
        content.put("content", "下班啦");
        jsonObject.put("text", content);
        JSONObject isAtAll = new JSONObject();
        isAtAll.put("isAtAll", true);
        jsonObject.put("at", isAtAll);

        String webHook = "https://oapi.dingtalk.com/robot/send?access_token=45a2bac3dff33cb1747ee1bbb8980736dd81d0cd96bfc1bd53625eb2c56ad54a";

        PostExample example = new PostExample();
        String response = example.post(webHook, jsonObject.toJSONString());
        System.out.println(response);

    }
}