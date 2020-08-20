package com.studies.flutters;

import org.apache.commons.lang3.text.StrBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class getUserInfo {

    private String ID;
    private String baseURL = "http://musicapi.leanapp.cn/user/detail?uid=";
    private String completely_url;

    public getUserInfo(String id) {
        this.ID = id;
        this.completely_url = baseURL + id;
    }

    public int isUser() throws IOException, JSONException {
        Connection connection_detail = Jsoup.connect(completely_url);
        connection_detail.ignoreContentType(true);
        connection_detail.timeout(2000);
        Document document_detail = connection_detail.get();
        JSONObject object = new JSONObject(document_detail.body().text());
        int code = object.getInt("code");
        if (code == 200){
            return StringBean.USER_IS_FOUND;
        }else {
            return StringBean.USER_IS_NOT_FOUND;
        }
    }

    public String get_nickName() throws IOException, JSONException {

        Connection connection_detail = Jsoup.connect(completely_url);
        connection_detail.ignoreContentType(true);
        connection_detail.timeout(2000);
        Document document_detail = connection_detail.get();
        JSONObject object = new JSONObject(document_detail.body().text());
        JSONObject json_profile = object.getJSONObject("profile");
        String nick_name = json_profile.getString("nickname");

        return nick_name;
    }

    public String get_avatar_image_url() throws IOException, JSONException {
        Connection connection_detail = Jsoup.connect(completely_url);
        connection_detail.ignoreContentType(true);
        connection_detail.timeout(2000);
        Document document_detail = connection_detail.get();
        JSONObject object = new JSONObject(document_detail.body().text());
        JSONObject json_profile = object.getJSONObject("profile");
        String avatar_url = json_profile.getString("avatarUrl");

        return avatar_url;
    }


}
