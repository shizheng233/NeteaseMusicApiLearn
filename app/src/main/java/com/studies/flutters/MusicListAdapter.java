package com.studies.flutters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.raphets.roundimageview.RoundImageView;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.VH> {

    private List<MusicListBean> list;
    private Context context;

    public MusicListAdapter(List<MusicListBean> musicListBeans, Context context) {
        this.context = context;
        this.list = musicListBeans;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view_music_list = LayoutInflater.from(context).inflate(R.layout.music_list_item,
                parent, false);
        return new VH(view_music_list);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        holder.text_music_title.setText(list.get(position).getMusic_tile());
        holder.text_music_artist.setText(list.get(position).getMusic_artist());
        Glide.with(context).load(list.get(position).getMusic_url()).into(holder.iv_alums);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView text_music_title;
        TextView text_music_artist;
        RoundImageView iv_alums;


        public VH(@NonNull View itemView) {
            super(itemView);
            text_music_artist = itemView.findViewById(R.id.artist_name);
            text_music_title = itemView.findViewById(R.id.music_title);
            iv_alums = itemView.findViewById(R.id.iv_alums);
        }
    }
}
