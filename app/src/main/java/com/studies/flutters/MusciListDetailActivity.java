package com.studies.flutters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusciListDetailActivity extends AppCompatActivity {

    private RecyclerView music_recycler;
    private Handler handler;
    private List<MusicListBean> listBeans;
    private MusicListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private AppBarLayout appBarLayout;

    @SuppressLint({"HandlerLeak", "ResourceAsColor"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list_detail);

        music_recycler = findViewById(R.id.music_list_recyclerView);
        appBarLayout = findViewById(R.id.appBarLayout);
        Toolbar toolbar_music_list = findViewById(R.id.music_list_toolbar);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorError,
                R.color.black,R.color.colorPrimary);
        refreshLayout.setRefreshing(true);
        ImageView ivTop = findViewById(R.id.iv_top);
        TextView tv_avater_name = findViewById(R.id.tvAvaterName);
        final TextView tv_coverName = findViewById(R.id.tvCoverName);
        TextView tv_description = findViewById(R.id.tvDescription);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapseActionView_music);
        setSupportActionBar(toolbar_music_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MusciListDetailActivity.this);
        music_recycler.setLayoutManager(linearLayoutManager);

        Intent in_url = getIntent();
        String url_image = in_url.getStringExtra(StringBean.IMAGE_URL);
        final String cover_name = in_url.getStringExtra(StringBean.COVER_NAME);
        String avater_name = in_url.getStringExtra(StringBean.AVATER_NAME);
        String descriptons = in_url.getStringExtra(StringBean.DETAILS);

        Glide.with(this).load(url_image).into(ivTop);
        tv_coverName.setText(cover_name);
        tv_avater_name.setText(avater_name);
        tv_description.setText(descriptons);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i==0){
                    collapsingToolbarLayout.setTitle(cover_name);
                    isShow = true;
                }else if (isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        final Runnable runnable_load = new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = getIntent();

                    String id = intent.getStringExtra(StringBean.PUT_EXTAR_NAME_1);
                    String url = "http://musicapi.leanapp.cn/playlist/detail?id=" + id;

                    Connection connection1 = Jsoup.connect(url);
                    connection1.ignoreContentType(true);
                    connection1.timeout(2000);

                    String json_music = connection1.get().body().text();
                    JSONObject jsonObject1 = new JSONObject(json_music);
                    JSONObject new_json_object = jsonObject1.getJSONObject("playlist");
                    final JSONArray jsonArray1 = new_json_object.getJSONArray("trackIds");

                    listBeans = new ArrayList<>();
                    adapter = new MusicListAdapter(listBeans, MusciListDetailActivity.this);
                    for (int p = 0; p < jsonArray1.length(); p++) {
                        try {
                            MusicListBean musicListBean = new MusicListBean();
                            JSONObject object = jsonArray1.getJSONObject(p);
                            long l = object.getLong("id");
                            final String ids = Long.toString(l);
                            String name = musicUnit.getMusicName(ids);
                            String ar_name = musicUnit.getArtistName(ids);
                            String al_url = musicUnit.getAlumsUrl(ids);
                            musicListBean.setMusic_artist(ar_name);
                            musicListBean.setMusic_tile(name);
                            musicListBean.setMusic_url(al_url);
                            listBeans.add(musicListBean);

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }


                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            music_recycler.setItemAnimator(new DefaultItemAnimator());
                            music_recycler.setAdapter(adapter);
                        }

                    });

                    Message message = new Message();
                    message.what = 1;
                    message.obj = getString(R.string.compeled);
                    handler.sendMessage(message);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        };

        final Thread[] thread = {
                new Thread(runnable_load)
        };
        thread[0].start();


        handler = new Handler() {

            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull final Message msg) {
                super.handleMessage(msg);
                Intent intent = getIntent();
                String title = intent.getStringExtra(StringBean.PUT_EXTAR_NAME_2);

                if (msg.what == 1) {
                    String isCompleted = (String) msg.obj;
                    if (isCompleted.equals(getString(R.string.compeled))) {
                        refreshLayout.setRefreshing(false);
                        thread[0].interrupt();
                        thread[0] = null;
                    }

                }
            }

        };


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                thread[0] = new Thread(runnable_load);
                thread[0].start();
            }
        });

    }
}
