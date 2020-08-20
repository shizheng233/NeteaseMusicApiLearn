package com.studies.flutters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class musicUnit {

    public static String getMusicName(String ids) throws JSONException, IOException {

        String urls = "http://musicapi.leanapp.cn/song/detail?ids=" + ids;

        Connection connection_ = Jsoup.connect(urls);
        connection_.timeout(2000);
        connection_.ignoreContentType(true);
        String json_beta = connection_.get().body().text();
        JSONObject jsonObject = new JSONObject(json_beta);

        JSONArray jsonArray = jsonObject.getJSONArray("songs");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

        return jsonObject1.getString("name");

    }

    public static String getArtistName(String ids) throws JSONException, IOException {
        String urls = "http://musicapi.leanapp.cn/song/detail?ids=" + ids;

        Connection connection_ = Jsoup.connect(urls);
        connection_.timeout(2000);
        connection_.ignoreContentType(true);
        String json_beta = connection_.get().body().text();
        JSONObject jsonObject = new JSONObject(json_beta);
        JSONArray jsonArray = jsonObject.getJSONArray("songs");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        JSONArray jsonArray1 = jsonObject1.getJSONArray("ar");
        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);

        return jsonObject2.getString("name");
    }

    public static String getAlumsUrl(String ids) throws JSONException, IOException {
        String urls = "http://musicapi.leanapp.cn/song/detail?ids=" + ids;

        Connection connection_ = Jsoup.connect(urls);
        connection_.timeout(2000);
        connection_.ignoreContentType(true);
        String json_beta = connection_.get().body().text();
        JSONObject jsonObject = new JSONObject(json_beta);
        JSONArray jsonArray = jsonObject.getJSONArray("songs");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        JSONObject alums_object = jsonObject1.getJSONObject("al");

        return alums_object.getString("picUrl");
    }



}
