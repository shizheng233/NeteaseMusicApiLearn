package com.studies.flutters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class NeteaseAdapter extends RecyclerView.Adapter<NeteaseAdapter.VH> {

    private List<NeteaseBean> neteaseBeanList;
    private Context context;
    private String tagName;
    private RecyclerView view;

    public NeteaseAdapter(List<NeteaseBean> list, Context context, String tagNames,
                          RecyclerView v) {
        this.context = context;
        this.neteaseBeanList = list;
        this.tagName = tagNames;
        this.view = v;

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_music_list, parent, false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        final long music_id = neteaseBeanList.get(position).getMusicId();
        final String cover_name = neteaseBeanList.get(position).getCoverName();
        holder.title_list.setText(neteaseBeanList.get(position).getCoverName());
        holder.avtor_name.setText(neteaseBeanList.get(position).getCreator());
        holder.isPrivate.setText(neteaseBeanList.get(position).getIsPribate());
        Glide.with(view).load(neteaseBeanList.get(position).getListImageURL())
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                .into(holder.coverImg);

        holder.layout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.putExtra(StringBean.PUT_EXTAR_NAME_1, Long.toString(music_id));
                intent.putExtra(StringBean.PUT_EXTAR_NAME_2, cover_name);
                intent.putExtra(StringBean.IMAGE_URL,neteaseBeanList.get(position).getListImageURL());
                intent.putExtra(StringBean.COVER_NAME,neteaseBeanList.get(position).getCoverName());
                intent.putExtra(StringBean.AVATER_NAME,neteaseBeanList.get(position).getCreator());
                intent.putExtra(StringBean.DETAILS,neteaseBeanList.get(position).getDetil());
                intent.putExtra("Color",R.color.white);
                intent.setClass(context, MusciListDetailActivity.class);
                jumpIntoAll(holder.coverImg,context,intent, tagName);

            }
        });


    }


    public void jumpIntoAll(View v1 ,Context c, Intent i, String tag) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation((Activity) c, new Pair<>(v1, tag));
        ActivityCompat.startActivity(c, i, optionsCompat.toBundle());
    }


    @Override
    public int getItemCount() {
        return neteaseBeanList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView coverImg;
        TextView title_list;
        TextView avtor_name;
        TextView isPrivate;
        LinearLayout layout_btn;

        public VH(@NonNull View view) {
            super(view);
            coverImg = view.findViewById(R.id.image_cover);
            title_list = view.findViewById(R.id.list_title);
            avtor_name = view.findViewById(R.id.list_user);
            isPrivate = view.findViewById(R.id.is_peivate);
            layout_btn = view.findViewById(R.id.layout_btn);
        }


    }


}
