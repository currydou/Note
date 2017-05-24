package com.curry.note.module.news.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.music.Musics;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by curry.zhang on 5/15/2017.
 */

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    private Context context;
    private List<Musics> musicsList;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MusicListAdapter(Context context, List<Musics> musicsList) {
        this.context = context;
        this.musicsList = musicsList;
    }

    //创建缓存视图
    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据父类的上下文来构建（listview用自己的？  不是）
        LayoutInflater inflater = LayoutInflater.from(context);
        //加载recyclerView的子布局
        View view = inflater.inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view);
    }

    //绑定视图缓存
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Musics musics = musicsList.get(position);
        if (!TextUtils.isEmpty(musics.getImage())) {
            holder.itemSdvMusic.setImageURI(musics.getImage());
        }
        if (!TextUtils.isEmpty(musics.getTitle())) {
            holder.itemTvMusicName.setText(musics.getTitle());
        }
        if (musics.getAuthor() != null) {
            holder.itemTvMusicGrade.setText(musics.getAuthor().get(0).getName());
        }
        if (!TextUtils.isEmpty(musics.getRating().getAverage())) {
            holder.itemTvMusicArt.setText("评分:" + musics.getRating().getAverage());
        }


        //监听
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position, musicsList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musicsList.size();
    }

    //用继承的，不直接用是因为在onCreateViewHolder直接返回new RecyclerView.ViewHolder(view)会报错
    class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView itemSdvMusic;
        private TextView itemTvMusicName;
        private TextView itemTvMusicGrade;
        private TextView itemTvMusicArt;

        public ViewHolder(View itemView) {
            super(itemView);
            itemSdvMusic = (SimpleDraweeView) itemView.findViewById(R.id.itemSdvMusic);
            itemTvMusicName = (TextView) itemView.findViewById(R.id.itemTvMusicName);
            itemTvMusicGrade = (TextView) itemView.findViewById(R.id.itemTvMusicGrade);
            itemTvMusicArt = (TextView) itemView.findViewById(R.id.itemTvMusicArt);
        }
    }

    //定义添加item方法
    public void addData(int position) {
//        noteList.add(position, "Insert One");
        notifyItemInserted(position);
    }

    //定义删除的方法
    public void removeData(int position) {
//        booksList.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<Musics> musicsList) {
        this.musicsList = musicsList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(View view, int position, Musics musics);
    }
}
