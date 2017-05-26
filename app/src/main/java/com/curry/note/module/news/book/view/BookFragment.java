package com.curry.note.module.news.book.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.note.DouBanBaseFragment;
import com.curry.note.R;
import com.curry.note.bean.book.BookRoot;
import com.curry.note.bean.book.Books;
import com.curry.note.module.news.book.adapter.BookListAdapter;
import com.curry.note.module.news.book.presenter.BookInfo;
import com.curry.note.module.news.book.presenter.BookInfoImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class BookFragment extends DouBanBaseFragment implements IBookView {


    Unbinder unbinder;
    @BindView(R.id.recyclerViewBook)
    RecyclerView recyclerViewBook;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvLoading)
    TextView tvLoading;
    @BindView(R.id.tvLoadError)
    TextView tvLoadError;

    private BookInfo bookInfo;
    private List<Books> booksList = new ArrayList<>();
    private BookListAdapter bookListAdapter;
    private GridLayoutManager gridLayoutManager;
    private int pageCount = 0;
    private String tag = "文学";
    private int PAGE_SIZE = 20;
    private List<Books> tempList;

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
        bookInfo = new BookInfoImpl(this);
        bookInfo.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, false);
        bookListAdapter = new BookListAdapter(getActivity(), booksList);
        // TODO: 5/24/2017  下拉加载显示不对
        recyclerViewBook.setAdapter(bookListAdapter);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewBook.setLayoutManager(gridLayoutManager);
        bookListAdapter.setOnItemClickListener(new BookListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Books book) {
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("id", book.getId());
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bookInfo.getList(tag, 0, PAGE_SIZE, false);
            }
        });
        recyclerViewBook.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                    if (gridLayoutManager.getItemCount() == 1) {
                        if (bookListAdapter != null) {
                            bookListAdapter.updateLoadStatus(BookListAdapter.LOAD_NONE);
                        }
                        return;

                    }
                    if (lastVisibleItem + 1 == gridLayoutManager.getItemCount()) {
                        if (bookListAdapter != null) {
                            bookListAdapter.updateLoadStatus(BookListAdapter.LOAD_PULL_TO);
                            // isLoadMore = true;
                            bookListAdapter.updateLoadStatus(BookListAdapter.LOAD_MORE);
                        }
                        //new Handler().postDelayed(() -> getBeforeNews(time), 1000);
                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pageCount++;
                                bookInfo.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, true);
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
            }
        });

        //当时footer时，单独占一行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return bookListAdapter.getItemViewType(position) == BookListAdapter.TYPE_FOOTER ? gridLayoutManager.getSpanCount() : 1;
            }
        });

    }

    @Override
    public void showLoadingPage() {
        setLoadingPageVisibility(tvLoading, recyclerViewBook, tvLoadError);
    }

    @Override
    public void showSuccessPage(BookRoot bookRoot, boolean isLoadMore) {
        // TODO: 5/26/2017  在原来有这个页面，重新安装，下面空指针
        swipeRefreshLayout.setRefreshing(false);
        setSuccessPageVisibility(tvLoading, recyclerViewBook, tvLoadError);
        tempList = bookRoot.getBooks();
        if (tempList != null && tempList.size() != 0) {
            if (isLoadMore) {
                //正常有数据的情况
                booksList.addAll(tempList);
            } else {
                booksList.clear();
                booksList.addAll(tempList);
            }
            bookListAdapter.setData(booksList);
        }
    }

    @Override
    public void showFailPage() {
        swipeRefreshLayout.setRefreshing(false);
        setFailPageVisibility(tvLoading, recyclerViewBook, tvLoadError);
    }

    @OnClick(R.id.tvLoadError)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLoadError:
                bookInfo.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, false);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
