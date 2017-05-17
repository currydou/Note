package com.curry.note.module.news.book.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.Books;
import com.curry.note.util.ScreenUtils;
import com.curry.note.util.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by curry.zhang on 5/15/2017.
 */

// TODO: 5/17/2017  有type，头尾 的区别；用bindview；其他的还写了什么；
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private Context context;
    private List<Books> booksList;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookListAdapter(Context context, List<Books> booksList) {
        this.context = context;
        this.booksList = booksList;
    }

    //创建缓存视图
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据父类的上下文来构建（listview用自己的？  不是）
        LayoutInflater inflater = LayoutInflater.from(context);
        //加载recyclerView的子布局
        View view = inflater.inflate(R.layout.item_book_reading, parent, false);
        return new ViewHolder(view);
    }

    //绑定视图缓存
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Books book = booksList.get(position);
        if (!TextUtils.isEmpty(book.getImages().getLarge())) {
            Utils.init(context);
            int screenWidth = ScreenUtils.getScreenWidth();
            int ivWidth = (screenWidth - ScreenUtils.dp2px(80)) / 3;
            double height = (420.0 / 300.0) * ivWidth;
            ViewGroup.LayoutParams layoutParams = holder.sdvFilm.getLayoutParams();
            layoutParams.width = ivWidth;
            layoutParams.height = (int) height;
            holder.sdvFilm.setLayoutParams(layoutParams);
            holder.sdvFilm.setImageURI(book.getImages().getLarge());
        }
        if (!TextUtils.isEmpty(book.getTitle())) {
            holder.tvFileName.setText(book.getTitle());
        }
        if (!TextUtils.isEmpty(book.getRating().getAverage())) {
            holder.tvFilmGrade.setText("评分:" + book.getRating().getAverage());
        } else {
            holder.tvFilmGrade.setText("暂无评分");
        }

        //监听
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position, booksList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    //用继承的，不直接用是因为在onCreateViewHolder直接返回new RecyclerView.ViewHolder(view)会报错
    class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView sdvFilm;
        private TextView tvFileName;
        private TextView tvFilmGrade;

        public ViewHolder(View itemView) {
            super(itemView);
            sdvFilm = (SimpleDraweeView) itemView.findViewById(R.id.sdvFilm);
            tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
            tvFilmGrade = (TextView) itemView.findViewById(R.id.tvFilmGrade);
        }
    }

    //定义添加item方法
    public void addData(int position) {
//        noteList.add(position, "Insert One");
        notifyItemInserted(position);
    }

    //定义删除的方法
    public void removeData(int position) {
        booksList.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<Books> booksList) {
        this.booksList = booksList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(View view, int position, Books book);
    }
}
