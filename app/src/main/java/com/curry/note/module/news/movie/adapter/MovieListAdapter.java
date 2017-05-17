package com.curry.note.module.news.movie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.top250.Subjects;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by curry.zhang on 5/17/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private Context context;
    private List<Subjects> subjectsList;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MovieListAdapter(Context context, List<Subjects> subjectsList) {
        this.context = context;
        this.subjectsList = subjectsList;
    }

    //创建缓存视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据父类的上下文来构建（listview用自己的？  不是）
        LayoutInflater inflater = LayoutInflater.from(context);
        //加载recyclerView的子布局
        View view = inflater.inflate(R.layout.item_top250, parent, false);
        return new ViewHolder(view);
    }

    //绑定视图缓存
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Subjects subjects = subjectsList.get(position);
        if (!TextUtils.isEmpty(subjects.getImages().getLarge())) {
            holder.itemSdvFilm.setImageURI(subjects.getImages().getLarge());
        }
        if (!TextUtils.isEmpty(subjects.getTitle())) {
            holder.itemTvFilm.setText(subjects.getTitle());
        }
        if (!TextUtils.isEmpty(subjects.getRating().getStars())) {
            holder.itemTvFilmGrade.setText("评分:" + subjects.getRating().getStars());
        }
        if (!TextUtils.isEmpty(subjects.getOriginal_title())) {
            holder.itemTvFilmEnglish.setText(subjects.getOriginal_title());
        }
        if (position < 9) {
            holder.itemTvRank.setText("0" + (position + 1));
        } else {
            holder.itemTvRank.setText("" + (position + 1));
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
        return subjectsList.size();
    }

    //用继承的，不直接用是因为在onCreateViewHolder直接返回new RecyclerView.ViewHolder(view)会报错
    class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView itemSdvFilm;
        private TextView itemTvFilm;
        private TextView itemTvFilmEnglish;
        private TextView itemTvFilmGrade;
        private TextView itemTvRank;

        public ViewHolder(View itemView) {
            super(itemView);
            itemSdvFilm = (SimpleDraweeView) itemView.findViewById(R.id.itemSdvFilm);
            itemTvFilm = (TextView) itemView.findViewById(R.id.itemTvFilm);
            itemTvFilmEnglish = (TextView) itemView.findViewById(R.id.itemTvFilmEnglish);
            itemTvFilmGrade = (TextView) itemView.findViewById(R.id.itemTvFilmGrade);
            itemTvRank = (TextView) itemView.findViewById(R.id.itemTvRank);
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
}
