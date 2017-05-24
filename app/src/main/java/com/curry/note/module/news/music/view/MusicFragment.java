package com.curry.note.module.news.music.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curry.note.R;
import com.curry.note.base.LazyBaseFragment2;
import com.curry.note.bean.music.MusicRoot;
import com.curry.note.bean.music.Musics;
import com.curry.note.module.news.music.adapter.MusicListAdapter;
import com.curry.note.module.news.music.presenter.MusicInfo;
import com.curry.note.module.news.music.presenter.MusicInfoImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class MusicFragment extends LazyBaseFragment2 implements IMusicView<MusicRoot> {

    @BindView(R.id.recyclerViewMusic)
    RecyclerView recyclerViewMusic;
    Unbinder unbinder;
    private List<Musics> musicsList = new ArrayList<>();
    private MusicListAdapter musicListAdapter;

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
        MusicInfo musicInfo = new MusicInfoImpl(this);

        musicListAdapter = new MusicListAdapter(getActivity(), musicsList);
        recyclerViewMusic.setAdapter(musicListAdapter);
        recyclerViewMusic.setLayoutManager(new LinearLayoutManager(getActivity()));
        musicListAdapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Musics musics) {
                Intent intent = new Intent(getActivity(), MusicDetailActivity.class);
                intent.putExtra("id", musics.getId());
                startActivity(intent);
            }
        });

        musicInfo.getList("流行", 0, 20);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onSuccess(MusicRoot data) {
        musicListAdapter.setData(data.getMusics());
    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
