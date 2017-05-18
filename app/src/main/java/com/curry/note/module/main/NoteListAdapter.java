package com.curry.note.module.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.bmob.Note;
import com.curry.note.util.TimeUtil;

import java.util.List;

/**
 * Created by curry.zhang on 5/15/2017.
 */


public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private Context context;
    private List<Note> noteList;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NoteListAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    //创建缓存视图
    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据父类的上下文来构建（listview用自己的？  不是）
        LayoutInflater inflater = LayoutInflater.from(context);
        //加载recyclerView的子布局
        View view = inflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(view);
    }

    //绑定视图缓存
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Note note = noteList.get(position);
        holder.tvContent.setText(note.getNoteContent());
        String descriptionTime = TimeUtil.getFormatTimeFromTimestamp(note.getTimestamp());
        holder.tvTimestamp.setText(descriptionTime);
        //监听
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position, noteList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    //用继承的，不直接用是因为在onCreateViewHolder直接返回new RecyclerView.ViewHolder(view)会报错
    class ViewHolder extends RecyclerView.ViewHolder {

        //        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
//            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
        }
    }

    //定义添加item方法
    public void addData(int position) {
//        noteList.add(position, "Insert One");
        notifyItemInserted(position);
    }

    //定义删除的方法
    public void removeData(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(View view, int position, Note note);
    }
}
