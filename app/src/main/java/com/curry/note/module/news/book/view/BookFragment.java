package com.curry.note.module.news.book.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curry.note.R;
import com.curry.note.base.LazyBaseFragment2;
import com.curry.note.bean.book.BookRoot;
import com.curry.note.bean.book.Books;
import com.curry.note.module.news.book.adapter.BookListAdapter;
import com.curry.note.module.news.book.presenter.BookInfo;
import com.curry.note.module.news.book.presenter.BookInfoImpl;
import com.curry.note.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class BookFragment extends LazyBaseFragment2 implements IBookView {


    Unbinder unbinder;
    @BindView(R.id.recyclerViewBook)
    RecyclerView recyclerViewBook;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private BookInfo bookInfo;
    private List<Books> booksList = new ArrayList<>();
    private BookListAdapter bookListAdapter;

    public static BookFragment getInstance(String name) {
        BookFragment bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bookFragment.setArguments(bundle);
        return bookFragment;
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void lazyLoad() {
        ToastUtils.showShortToast("加载");
        bookInfo = new BookInfoImpl(this);
        bookInfo.getList("文学", 0, 20);
        bookListAdapter = new BookListAdapter(getActivity(), booksList);
        // TODO: 5/24/2017  快速切换的时候下面报空指针
        recyclerViewBook.setAdapter(bookListAdapter);
        recyclerViewBook.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        bookListAdapter.setOnItemClickListener(new BookListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Books book) {
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("id", book.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void showSuccessPage(BookRoot bookRoot) {
        booksList = bookRoot.getBooks();
        if (booksList != null && booksList.size() != 0) {
            //正常有数据的情况
            bookListAdapter.setData(booksList);
        }
    }

    @Override
    public void showFailPage() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
