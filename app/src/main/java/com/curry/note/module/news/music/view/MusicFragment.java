package com.curry.note.module.news.music.view;

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
import com.curry.note.bean.music.MusicRoot;
import com.curry.note.bean.music.Musics;
import com.curry.note.module.news.book.adapter.BookListAdapter;
import com.curry.note.module.news.music.adapter.MusicListAdapter;
import com.curry.note.module.news.music.presenter.MusicInfo;
import com.curry.note.module.news.music.presenter.MusicInfoImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class MusicFragment extends DouBanBaseFragment implements IMusicView<MusicRoot> {

    @BindView(R.id.recyclerViewMusic)
    RecyclerView recyclerViewMusic;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvLoading)
    TextView tvLoading;
    @BindView(R.id.tvLoadError)
    TextView tvLoadError;

    Unbinder unbinder;
    private List<Musics> musicsList = new ArrayList<>();
    private MusicListAdapter musicListAdapter;

    private int pageCount = 0;
    private String tag = "流行";
    private int PAGE_SIZE = 20;
    private LinearLayoutManager linearLayoutManager;
    private List<Musics> tempList;
    private MusicInfo musicInfo;

    public static MusicFragment getInstance(String name) {
        MusicFragment musicFragment = new MusicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        musicFragment.setArguments(bundle);
        return musicFragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void lazyLoad() {
        musicInfo = new MusicInfoImpl(this);

        musicListAdapter = new MusicListAdapter(getActivity(), musicsList);
        recyclerViewMusic.setAdapter(musicListAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMusic.setLayoutManager(linearLayoutManager);
        musicListAdapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Musics musics) {
                Intent intent = new Intent(getActivity(), MusicDetailActivity.class);
                intent.putExtra("id", musics.getId());
                startActivity(intent);
            }
        });

        musicInfo.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                musicInfo.getList(tag, 0, PAGE_SIZE, false);
            }
        });
        recyclerViewMusic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (linearLayoutManager.getItemCount() == 1) {
                        if (musicListAdapter != null) {
                            musicListAdapter.updateLoadStatus(BookListAdapter.LOAD_NONE);
                        }
                        return;

                    }
                    if (lastVisibleItem + 1 == linearLayoutManager.getItemCount()) {
                        if (musicListAdapter != null) {
                            musicListAdapter.updateLoadStatus(BookListAdapter.LOAD_PULL_TO);
                            // isLoadMore = true;
                            musicListAdapter.updateLoadStatus(BookListAdapter.LOAD_MORE);
                        }
                        //new Handler().postDelayed(() -> getBeforeNews(time), 1000);
                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pageCount++;
                                musicInfo.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, true);
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
        setLoadingPageVisibility(tvLoading, recyclerViewMusic, tvLoadError);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onSuccess(MusicRoot data, boolean isLoadMore) {
        setSuccessPageVisibility(tvLoading, recyclerViewMusic, tvLoadError);
        swipeRefreshLayout.setRefreshing(false);
        tempList = data.getMusics();
        if (tempList != null && tempList.size() != 0) {
            if (isLoadMore) {
                //正常有数据的情况
                musicsList.addAll(tempList);
            } else {
                musicsList.clear();
                musicsList.addAll(tempList);
            }
            musicListAdapter.setData(musicsList);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        swipeRefreshLayout.setRefreshing(false);
        setFailPageVisibility(tvLoading, recyclerViewMusic, tvLoadError);
    }

    @OnClick(R.id.tvLoadError)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLoadError:
                musicInfo.getList(tag, pageCount * PAGE_SIZE, PAGE_SIZE, false);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
