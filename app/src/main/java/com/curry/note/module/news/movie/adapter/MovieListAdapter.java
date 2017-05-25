package com.curry.note.module.news.movie.adapter;

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
import com.curry.note.bean.top250.Subjects;
import com.curry.note.util.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by curry.zhang on 5/17/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Subjects> subjectsList;
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

    public MovieListAdapter(Context context, List<Subjects> subjectsList) {
        this.context = context;
        this.subjectsList = subjectsList;
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
            return new MovieListAdapter.FooterViewHolder(view);
        } else {
            //加载recyclerView的子布局
            View view = inflater.inflate(R.layout.item_top250, parent, false);
            return new MovieListAdapter.ItemViewHolder(view);
        }
    }

    //绑定视图缓存
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MovieListAdapter.ItemViewHolder) {
            Subjects subjects = subjectsList.get(position);
            MovieListAdapter.ItemViewHolder itemViewHolder = (MovieListAdapter.ItemViewHolder) holder;
            itemViewHolder.bindItem(subjects, position);
        } else if (holder instanceof MovieListAdapter.FooterViewHolder) {
            MovieListAdapter.FooterViewHolder footerViewHolder = (MovieListAdapter.FooterViewHolder) holder;
            footerViewHolder.bindItem();
        }

        //监听
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position, subjectsList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return subjectsList.size() + 1;
    }

    //用继承的，不直接用是因为在onCreateViewHolder直接返回new RecyclerView.ViewHolder(view)会报错
    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemSdvFilm)
        SimpleDraweeView itemSdvFilm;
        @BindView(R.id.itemTvFilm)
        TextView itemTvFilm;
        @BindView(R.id.itemTvFilmEnglish)
        TextView itemTvFilmEnglish;
        @BindView(R.id.itemTvFilmGrade)
        TextView itemTvFilmGrade;
        @BindView(R.id.itemTvRank)
        TextView itemTvRank;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindItem(Subjects subjects, int position) {
            if (!TextUtils.isEmpty(subjects.getImages().getLarge())) {
                itemSdvFilm.setImageURI(subjects.getImages().getLarge());
            }
            if (!TextUtils.isEmpty(subjects.getTitle())) {
                itemTvFilm.setText(subjects.getTitle());
            }
            if (!TextUtils.isEmpty(subjects.getRating().getStars())) {
                itemTvFilmGrade.setText("评分:" + subjects.getRating().getStars());
            }
            if (!TextUtils.isEmpty(subjects.getOriginal_title())) {
                itemTvFilmEnglish.setText(subjects.getOriginal_title());
            }
            if (position < 9) {
                itemTvRank.setText("0" + (position + 1));
            } else {
                itemTvRank.setText("" + (position + 1));
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

    public void setData(List<Subjects> subjectsList) {
        this.subjectsList = subjectsList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(View view, int position, Subjects subjects);
    }

    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }
}
