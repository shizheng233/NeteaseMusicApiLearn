package com.studies.flutters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
    private List<MusicListBean> listBeans = new ArrayList<>();
    private MusicListAdapter adapter = new MusicListAdapter(listBeans, MusciListDetailActivity.this);
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private MusicListBean musicListBean;

    @SuppressLint({"HandlerLeak", "ResourceAsColor"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list_detail);

        music_recycler = findViewById(R.id.music_list_recyclerView);
        final AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        Toolbar toolbar_music_list = findViewById(R.id.music_list_toolbar);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorError,
                R.color.black, R.color.colorPrimary);
        refreshLayout.setRefreshing(true);

        ImageView ivTop = findViewById(R.id.iv_top);
        TextView tv_avatar_name = findViewById(R.id.tvAvaterName);
        final TextView tv_coverName = findViewById(R.id.tvCoverName);
        TextView tv_description = findViewById(R.id.tvDescription);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapseActionView_music);
        setSupportActionBar(toolbar_music_list);

        linearLayoutManager = new LinearLayoutManager(MusciListDetailActivity.this);
        music_recycler.setLayoutManager(linearLayoutManager);

        final Intent in_url = getIntent();
        String url_image = in_url.getStringExtra(StringBean.IMAGE_URL);
        final String cover_name = in_url.getStringExtra(StringBean.COVER_NAME);
        String avatar_name = in_url.getStringExtra(StringBean.AVATER_NAME);
        String descriptions = in_url.getStringExtra(StringBean.DETAILS);

        Glide.with(this).load(url_image).into(ivTop);
        tv_coverName.setText(cover_name);
        tv_avatar_name.setText(avatar_name);
        tv_description.setText(descriptions);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle(cover_name);
                    isShow = true;
                } else if (isShow) {
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

                    int arraySize = jsonArray1.length();

                    try {
                        for (int p = 0; p < 15; p++) {

                            musicListBean = new MusicListBean();
                            JSONObject object = jsonArray1.getJSONObject(p);
                            long l = object.getLong("id");
                            final String ids = Long.toString(l);
                            String name = musicUnit.getMusicName(ids);
                            String ar_name = musicUnit.getArtistName(ids);
                            String al_url = musicUnit.getAlumsUrl(ids);

                            musicListBean.setMusic_artist(ar_name);
                            musicListBean.setMusic_tile(name);
                            musicListBean.setMusic_url(al_url);

                            if (arraySize < 15) {
                                musicListBean.setLoading(StringBean.NO_MORE);
                            } else {
                                musicListBean.setLoading(StringBean.CLICK_FOR_MORE);
                            }

                            listBeans.add(musicListBean);
                        }


                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            music_recycler.setItemAnimator(new DefaultItemAnimator());
                            music_recycler.setAdapter(adapter);
                        }

                    });


                    Message successful = new Message();
                    successful.what = StringBean.SEND_FEEDBACK_NUM_SUCCESSFUL;
                    successful.obj = StringBean.SUCCESSFUL;
                    handler.sendMessage(successful);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        };

        final Runnable runnable_load_more = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("THREAD_START", "Thread is started");
                    Intent intent = getIntent();
                    String url_ids = intent.getStringExtra(StringBean.PUT_EXTAR_NAME_1);
                    String url = "http://musicapi.leanapp.cn/playlist/detail?id=" + url_ids;

                    Connection connection1 = Jsoup.connect(url);
                    connection1.ignoreContentType(true);
                    connection1.timeout(2000);

                    String json_music = connection1.get().body().text();
                    JSONObject jsonObject1 = new JSONObject(json_music);
                    JSONObject new_json_object = jsonObject1.getJSONObject("playlist");
                    JSONArray jsonArray1 = new_json_object.getJSONArray("trackIds");
                    int arraySize = jsonArray1.length();
                    int start = 15;
                    int least = arraySize - 15;

                    Log.i("ALL_NUM",">>>>>>>>>>"+arraySize);

                    if (least <= 15) {
                        for (int l = start; l < arraySize; l++) {
                            Log.i("FOR", "NUM:" + l);
                            Log.i("WHO","LESS THAN 15");
                            MusicListBean beans = new MusicListBean();
                            JSONObject object = jsonArray1.getJSONObject(l);
                            long ls = object.getLong("id");
                            final String ids = Long.toString(ls);
                            String name = musicUnit.getMusicName(ids);
                            String ar_name = musicUnit.getArtistName(ids);
                            String al_url = musicUnit.getAlumsUrl(ids);

                            beans.setMusic_artist(ar_name);
                            beans.setMusic_tile(name);
                            beans.setMusic_url(al_url);
                            beans.setLoading(StringBean.NO_MORE);
                            listBeans.add(beans);


                        }
                        Log.i("LIST_NUM",":"+start);
                    } else {
                        int fiftyPass = start + 15;
                        int startLoadingPosition = linearLayoutManager.findLastVisibleItemPosition() +1;
                        Log.i("START LOADING POSITION",">>>>>>>>>"+startLoadingPosition);
                        Log.i("WHO","MORE THAN 15");
                        for (int l = startLoadingPosition; l < startLoadingPosition + 15; l++) {

                            Log.i("FOR", "NUM:" + l);
                            MusicListBean beans = new MusicListBean();
                            JSONObject object = jsonArray1.getJSONObject(l);
                            long ls = object.getLong("id");
                            final String ids = Long.toString(ls);
                            String name = musicUnit.getMusicName(ids);
                            String ar_name = musicUnit.getArtistName(ids);
                            String al_url = musicUnit.getAlumsUrl(ids);

                            beans.setMusic_artist(ar_name);
                            beans.setMusic_tile(name);
                            beans.setMusic_url(al_url);
                            if (arraySize <= start + 15) {
                                beans.setLoading(StringBean.NO_MORE);
                            } else {
                                beans.setLoading(StringBean.CLICK_FOR_MORE);
                            }

                            listBeans.add(beans);
                        }

                    }




                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });


                    Message message8 = new Message();
                    message8.obj = StringBean.SUCCESSFUL;
                    message8.what = 2;
                    handler.sendMessage(message8);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        };


        final Thread[] thread = {
                new Thread(runnable_load),
                new Thread(runnable_load_more)
        };
        thread[0].start();

        adapter.setOnItemClickListener(new MusicListAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view) {

                thread[1] = new Thread(runnable_load_more);
                thread[1].start();

            }
        });


        handler = new Handler() {

            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull final Message msg) {
                super.handleMessage(msg);
                Intent intent = getIntent();
                String title = intent.getStringExtra(StringBean.PUT_EXTAR_NAME_2);

                if (msg.what == 1) {
                    if (msg.obj == StringBean.SUCCESSFUL) {
                        refreshLayout.setRefreshing(false);
                        thread[0].interrupt();
                        thread[0] = null;
                    }
                }
                if (msg.what == 2){
                    thread[1].interrupt();
                    thread[1] = null;
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
