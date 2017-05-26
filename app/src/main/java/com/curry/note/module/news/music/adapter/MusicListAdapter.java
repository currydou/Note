package com.curry.note.module.news.music.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.music.Musics;
import com.curry.note.util.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by curry.zhang on 5/15/2017.
 */

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Musics> musicsList;
    private OnItemClickListener listener;

    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    public static final int LOAD_END = 3;
    private static final int TYPE_TOP = -1;
    private static final int TYPE_FOOTER = -2;
    private int status = 1;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MusicListAdapter(Context context, List<Musics> musicsList) {
        this.context = context;
        this.musicsList = musicsList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return position;
        }
    }

    //创建缓存视图
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据父类的上下文来构建（listview用自己的？  不是）
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_FOOTER) {
            View view = View.inflate(parent.getContext(), R.layout.activity_view_footer, null);
            return new MusicListAdapter.FooterViewHolder(view);
        } else {
            //加载recyclerView的子布局
            View view = inflater.inflate(R.layout.item_music, parent, false);
            return new MusicListAdapter.ItemViewHolder(view);
        }
    }

    //绑定视图缓存
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MusicListAdapter.ItemViewHolder) {
            Musics musics = musicsList.get(position);
            MusicListAdapter.ItemViewHolder itemViewHolder = (MusicListAdapter.ItemViewHolder) holder;
            itemViewHolder.bindItem(musics, position);
        } else if (holder instanceof MusicListAdapter.FooterViewHolder) {
            MusicListAdapter.FooterViewHolder footerViewHolder = (MusicListAdapter.FooterViewHolder) holder;
            footerViewHolder.bindItem();
        }

    }

    @Override
    public int getItemCount() {
        return musicsList.size() + 1;
    }

    //用继承的，不直接用是因为在onCreateViewHolder直接返回new RecyclerView.ViewHolder(view)会报错
    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemSdvMusic)
        SimpleDraweeView itemSdvMusic;
        @BindView(R.id.itemTvMusicName)
        TextView itemTvMusicName;
        @BindView(R.id.itemTvMusicGrade)
        TextView itemTvMusicGrade;
        @BindView(R.id.itemTvMusicArt)
        TextView itemTvMusicArt;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindItem(Musics musics, final int position) {
            if (!TextUtils.isEmpty(musics.getImage())) {
                itemSdvMusic.setImageURI(musics.getImage());
            }
            if (!TextUtils.isEmpty(musics.getTitle())) {
                itemTvMusicName.setText(musics.getTitle());
            }
            if (musics.getAuthor() != null) {
                itemTvMusicGrade.setText(musics.getAuthor().get(0).getName());
            }
            if (!TextUtils.isEmpty(musics.getRating().getAverage())) {
                itemTvMusicArt.setText("评分:" + musics.getRating().getAverage());
            }
            //监听
            if (listener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(view, position, musicsList.get(position));
                    }
                });
            }
        }
    }

    /**
     * footer view
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_load_prompt)
        TextView tv_load_prompt;
        @BindView(R.id.progress)
        ProgressBar progress;

        FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(40));
            itemView.setLayoutParams(params);
        }

        private void bindItem() {
            switch (status) {
                case LOAD_MORE:
                    progress.setVisibility(View.VISIBLE);
                    tv_load_prompt.setText("正在加载...");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText("上拉加载更多");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    System.out.println("LOAD_NONE----");
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText("已无更多加载");
                    break;
                case LOAD_END:
                    itemView.setVisibility(View.GONE);
                default:
                    break;
            }
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

    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }
}
