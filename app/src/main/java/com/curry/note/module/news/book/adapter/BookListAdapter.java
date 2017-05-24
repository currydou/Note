package com.curry.note.module.news.book.adapter;

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
import com.curry.note.bean.book.Books;
import com.curry.note.util.ScreenUtils;
import com.curry.note.util.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by curry.zhang on 5/15/2017.
 */

// TODO: 5/17/2017  有type，头尾 的区别；用bindview；其他的还写了什么；
public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Books> booksList;
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

    public BookListAdapter(Context context, List<Books> booksList) {
        this.context = context;
        this.booksList = booksList;
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
            return new FooterViewHolder(view);
        } else {
            //加载recyclerView的子布局
            View view = inflater.inflate(R.layout.item_book_reading, parent, false);
            return new ItemViewHolder(view);
        }
    }

    //绑定视图缓存
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Books book = booksList.get(position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.bindItem(book);
        } else if (holder instanceof BookListAdapter.FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.bindItem();
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
        return booksList.size() + 1;
    }

    //用继承的，不直接用是因为在onCreateViewHolder直接返回new RecyclerView.ViewHolder(view)会报错
    private class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sdvFilm)
        SimpleDraweeView sdvFilm;
        @BindView(R.id.tvFileName)
        TextView tvFileName;
        @BindView(R.id.tvFilmGrade)
        TextView tvFilmGrade;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindItem(Books book) {
            if (!TextUtils.isEmpty(book.getImages().getLarge())) {
                Utils.init(context);
                int screenWidth = ScreenUtils.getScreenWidth();
                int ivWidth = (screenWidth - ScreenUtils.dp2px(80)) / 3;
                double height = (420.0 / 300.0) * ivWidth;
                ViewGroup.LayoutParams layoutParams = sdvFilm.getLayoutParams();
                layoutParams.width = ivWidth;
                layoutParams.height = (int) height;
                sdvFilm.setLayoutParams(layoutParams);
                sdvFilm.setImageURI(book.getImages().getLarge());
            }
            if (!TextUtils.isEmpty(book.getTitle())) {
                tvFileName.setText(book.getTitle());
            }
            if (!TextUtils.isEmpty(book.getRating().getAverage() + "")) {
                tvFilmGrade.setText("评分:" + book.getRating().getAverage());
            } else {
                tvFilmGrade.setText("暂无评分");
            }
        }

    }

    /**
     * footer view
     */
    private class FooterViewHolder extends RecyclerView.ViewHolder {
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
