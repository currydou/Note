package com.curry.note.module.news.movie.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.note.DouBanBaseFragment;
import com.curry.note.R;
import com.curry.note.bean.top250.Root;
import com.curry.note.bean.top250.Subjects;
import com.curry.note.module.news.book.adapter.BookListAdapter;
import com.curry.note.module.news.movie.adapter.MovieListAdapter;
import com.curry.note.module.news.movie.presenter.IMoviePresenter;
import com.curry.note.module.news.movie.presenter.MoviePresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class MovieFragment extends DouBanBaseFragment implements IMovieView {

    @BindView(R.id.recyclerViewMovie)
    RecyclerView recyclerViewMovie;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvLoading)
    TextView tvLoading;
    @BindView(R.id.tvLoadError)
    TextView tvLoadError;

    Unbinder unbinder;
    Unbinder unbinder1;
    private List<Subjects> subjectsList = new ArrayList<>();
    private MovieListAdapter movieListAdapter;

    private int pageCount = 0;
    private String tag = "流行";
    private int PAGE_SIZE = 20;
    private LinearLayoutManager linearLayoutManager;
    private List<Subjects> tempList;
    private IMoviePresenter iMoviePresenter;

    public static MovieFragment getInstance(String name) {
        MovieFragment movieFragment = new MovieFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        movieFragment.setArguments(bundle);
        return movieFragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void lazyLoad() {
        movieListAdapter = new MovieListAdapter(getActivity(), subjectsList);
        recyclerViewMovie.setAdapter(movieListAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMovie.setLayoutManager(linearLayoutManager);
        movieListAdapter.setOnItemClickListener(new MovieListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Subjects subjects) {
                Intent intent = new Intent(getActivity(), FilmDetailActivity.class);
                intent.putExtra("id", subjects.getId());
                startActivity(intent);
            }
        });

        iMoviePresenter = new MoviePresenterImpl(this);
        iMoviePresenter.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                iMoviePresenter.getList(tag, 0, PAGE_SIZE, false);
            }
        });
        recyclerViewMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (linearLayoutManager.getItemCount() == 1) {
                        if (movieListAdapter != null) {
                            movieListAdapter.updateLoadStatus(BookListAdapter.LOAD_NONE);
                        }
                        return;

                    }
                    if (lastVisibleItem + 1 == linearLayoutManager.getItemCount()) {
                        if (movieListAdapter != null) {
                            movieListAdapter.updateLoadStatus(BookListAdapter.LOAD_PULL_TO);
                            // isLoadMore = true;
                            movieListAdapter.updateLoadStatus(BookListAdapter.LOAD_MORE);
                        }
                        //new Handler().postDelayed(() -> getBeforeNews(time), 1000);
                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pageCount++;
                                iMoviePresenter.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, true);
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public void onRequestStart() {
        setLoadingPageVisibility(tvLoading, recyclerViewMovie, tvLoadError);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onSuccess(Object data, boolean isLoadMore) {
        setSuccessPageVisibility(tvLoading, recyclerViewMovie, tvLoadError);
        swipeRefreshLayout.setRefreshing(false);
        tempList = ((Root) data).getSubjects();
        if (tempList != null && tempList.size() != 0) {
            if (isLoadMore) {
                //正常有数据的情况
                subjectsList.addAll(tempList);
            } else {
                subjectsList.clear();
                subjectsList.addAll(tempList);
            }
            movieListAdapter.setData(subjectsList);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        setFailPageVisibility(tvLoading, recyclerViewMovie, tvLoadError);
        swipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.tvLoadError)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLoadError:
                iMoviePresenter.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, false);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
