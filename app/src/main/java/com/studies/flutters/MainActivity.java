package com.studies.flutters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle barDrawerToggle;
    private MaterialEditText materialEditText;
    private TextView nike_name_text_view;
    private ImageView avatar_img;
    private NavigationView navigationView;
    private FloatingActionButton fab_agree_id;
    private String URL = "http://musicapi.leanapp.cn/user/playlist?uid=";
    private String UID;
    private ProgressDialog progressDialog;
    private Handler handler;
    private RecyclerView recyclerViewList;
    private List<NeteaseBean> list_L;
    private GridLayoutManager layoutManager;
    private NeteaseAdapter neteaseAdapter;
    private AlertDialog dialog;
    private Thread thread;
    private Bitmap bitmap;
    private String str;

    @SuppressLint({"HandlerLeak", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_main);
        recyclerViewList = findViewById(R.id.mian_rec);
        navigationView = findViewById(R.id.navigation);
        View header_layout = navigationView.getHeaderView(0);
        nike_name_text_view = header_layout.findViewById(R.id.text_nike_name);
        avatar_img = header_layout.findViewById(R.id.image_icon);
        layoutManager = new GridLayoutManager(this, 2);
        toolbar = findViewById(R.id.toolbar_main);
        drawerLayout = findViewById(R.id.drawer_mian);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        barDrawerToggle.syncState();
        drawerLayout.setDrawerListener(barDrawerToggle);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog_inputo, null);
        materialEditText = view.findViewById(R.id.text_input_id);
        fab_agree_id = view.findViewById(R.id.fab_next_step);

        progressDialog = new ProgressDialog(MainActivity.this);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                UID = materialEditText.getText().toString();
                try {
                    Message message = new Message();
                    Message message_error = new Message();
                    Connection connection = Jsoup.connect(URL + UID);
                    Log.i("FINAL_URL", URL + UID);
                    connection.timeout(2000);
                    connection.ignoreContentType(true);

                    Document document = connection.get();
                    String resJson = document.body().text();
                    JSONObject mainJSON = new JSONObject(resJson);
                    message.obj = mainJSON;
                    message.what = 1;
                    handler.sendMessage(message);
                    Message message1 = new Message();
                    getUserInfo userInfo = new getUserInfo(UID);
                    String nick_name = "";
                    String avatar_url = "";
                    if (userInfo.isUser() == StringBean.USER_IS_FOUND) {
                        nick_name = userInfo.get_nickName();
                        avatar_url = userInfo.get_avatar_image_url();
                    } else if (userInfo.isUser() == StringBean.USER_IS_NOT_FOUND) {
                        message_error.what = 0;
                        message_error.obj = getString(R.string.usrNotFound);
                        handler.sendMessage(message_error);
                        return;
                    }
                    message1.obj = nick_name;
                    message1.what = 2;
                    Message message2 = new Message();
                    message2.obj = avatar_url;
                    message2.what = 3;

                    handler.sendMessage(message1);
                    handler.sendMessage(message2);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.dialog_title));
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.dialog_btn_neutral_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        fab_agree_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(materialEditText.getText()).toString().isEmpty()) {
                    materialEditText.setError(getString(R.string.input_your_id));

                } else {
                    thread = new Thread(runnable);
                    progressDialog.setMessage(getString(R.string.progressing));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    thread.start();
                }

            }
        });
        dialog.show();

        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {

                    if (msg.what == 0) {
                        String error_text = (String) msg.obj;
                        materialEditText.setError(error_text);
                        progressDialog.dismiss();
                    } else {
                        switch (msg.what) {
                            case 1:
                                JSONObject root_json = (JSONObject) msg.obj;
                                JSONArray listPlay = root_json.getJSONArray("playlist");
                                if (listPlay.toString().equals("[]")) {
                                    thread.interrupt();
                                    materialEditText.setError(getString(R.string.something_error));
                                    thread = null;
                                    progressDialog.dismiss();
                                    return;
                                }
                                list_L = new ArrayList<NeteaseBean>();

                                for (int i = 0; i < listPlay.length(); i++) {
                                    NeteaseBean bean = new NeteaseBean();
                                    JSONObject object = listPlay.getJSONObject(i);
                                    JSONObject creatorOb = object.getJSONObject("creator");
                                    String description;
                                    long music_id = object.getLong("id");
                                    bean.setMusicId(music_id);
//                                    String url = "http://musicapi.leanapp.cn/playlist/detail?id=" + music_id;
                                    str = object.getString("coverImgUrl");

                                    bean.setListImageURL(str);

                                    String str_creator_name = creatorOb.getString("nickname");
                                    bean.setCreator(str_creator_name);
                                    String str_name = object.getString("name");
                                    bean.setCoverName(str_name);
                                    String avatarUrl_c = creatorOb.getString("avatarUrl");
                                    long updateTime = object.getLong("updateTime");
                                    if (object.isNull("description")) {
                                        description = getString(R.string.no_description);
                                        bean.setDetil(description);
                                    } else {
                                        description = object.getString("description");
                                        bean.setDetil(description);
                                    }

                                    int id = object.getInt("userId");
                                    if (id == Integer.parseInt(UID)) {
                                        bean.setIsPribate(getString(R.string._private));
                                    } else {
                                        bean.setIsPribate(getString(R.string.collection));
                                    }

                                    list_L.add(bean);
                                }

                                progressDialog.dismiss();
                                dialog.dismiss();
                                neteaseAdapter = new NeteaseAdapter(list_L, MainActivity.this,
                                        getString(R.string.my), recyclerViewList);
                                recyclerViewList.setLayoutManager(layoutManager);
                                recyclerViewList.setAdapter(neteaseAdapter);
                                break;
                            case 2:
                                String nick_name_str = (String) msg.obj;
                                nike_name_text_view.setText(nick_name_str);
                                break;
                            case 3:
                                String avatar_url_str = (String) msg.obj;
                                Glide.with(MainActivity.this).load(avatar_url_str).into(avatar_img);
                                break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


    }


}