package com.studies.flutters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.raphets.roundimageview.RoundImageView;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.VH> {

    private static final int LIST_NORMAL = 0;
    private static final int LIST_END_ITEM = 1;
    private static final int LOAD_MORE = 2;
    private static final int NO_MORE = 3;
    private List<MusicListBean> list;
    private Context context;
    private onItemClickListener onItemClickListener;

    public MusicListAdapter(List<MusicListBean> musicListBeans, Context context) {
        this.context = context;
        this.list = musicListBeans;
    }

    public void setOnItemClickListener(MusicListAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view_in_loading_more = LayoutInflater.from(context).inflate(R.layout.in_loading_layout,
                parent, false);
        View view_music_list = LayoutInflater.from(context).inflate(R.layout.music_list_item,
                parent, false);

        if (viewType == LIST_END_ITEM) {
            return new VH(view_in_loading_more);
        } else {
            return new VH(view_music_list);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, int position) {

        int loading = list.get(position).getLoading();

        if (getItemViewType(position) == LIST_NORMAL) {
            holder.text_music_title.setText(list.get(position).getMusic_tile());
            holder.text_music_artist.setText(list.get(position).getMusic_artist());
            Glide.with(context).load(list.get(position).getMusic_url()).into(holder.iv_alums);

        } else if (getItemViewType(position) == LIST_END_ITEM) {


            if (loading == StringBean.LOAD_MORE) {
                holder.layout_load.setVisibility(View.VISIBLE);
                holder.layout_no_more.setVisibility(View.GONE);
                holder.layout_click_more.setVisibility(View.GONE);
            }
            if (loading == StringBean.NO_MORE) {
                holder.layout_load.setVisibility(View.GONE);
                holder.layout_no_more.setVisibility(View.VISIBLE);
                holder.layout_click_more.setVisibility(View.GONE);
            }
            if (loading == StringBean.CLICK_FOR_MORE){
                holder.layout_load.setVisibility(View.GONE);
                holder.layout_no_more.setVisibility(View.GONE);
                holder.layout_click_more.setVisibility(View.VISIBLE);
                holder.layout_click_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.layout_load.setVisibility(View.VISIBLE);
                        holder.layout_click_more.setVisibility(View.GONE);
                        onItemClickListener.OnItemClick(view);
                    }
                });
            }
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return LIST_END_ITEM;
        }

        return LIST_NORMAL;
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView text_music_title;
        TextView text_music_artist;
        LinearLayout layout_loading, layout_load, layout_no_more,layout_click_more;
        RoundImageView iv_alums;


        public VH(@NonNull View itemView) {
            super(itemView);
            text_music_artist = itemView.findViewById(R.id.artist_name);
            text_music_title = itemView.findViewById(R.id.music_title);
            iv_alums = itemView.findViewById(R.id.iv_alums);
            layout_loading = itemView.findViewById(R.id.loading_more);
            layout_load = itemView.findViewById(R.id.load_more);
            layout_no_more = itemView.findViewById(R.id.no_more);
            layout_click_more = itemView.findViewById(R.id.click_more);
        }


    }

    interface onItemClickListener{
        void OnItemClick(View view);
    }


}
