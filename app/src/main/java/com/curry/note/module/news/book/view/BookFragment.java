package com.curry.note.module.news.book.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curry.note.R;
import com.curry.note.bean.BookRoot;
import com.curry.note.bean.Books;
import com.curry.note.module.news.book.adapter.BookListAdapter;
import com.curry.note.module.news.book.presenter.BookInfo;
import com.curry.note.module.news.book.presenter.BookInfoImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class BookFragment extends Fragment implements IBookView {


    Unbinder unbinder;
    @BindView(R.id.recyclerViewBook)
    RecyclerView recyclerViewBook;
    private BookInfo bookInfo;
    private List<Books> booksList=new ArrayList<>();
    private BookListAdapter bookListAdapter;

    public static BookFragment getInstance(String name) {
        BookFragment bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bookFragment.setArguments(bundle);
        return bookFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bookInfo = new BookInfoImpl(this);
        bookInfo.getList("文学", 0, 20);
        bookListAdapter = new BookListAdapter(getActivity(), booksList);
        recyclerViewBook.setAdapter(bookListAdapter);
        recyclerViewBook.setLayoutManager(new GridLayoutManager(getActivity(), 3));

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
