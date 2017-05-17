package com.curry.note.module.news.music.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curry.note.R;
import com.curry.note.bean.MusicRoot;
import com.curry.note.bean.Musics;
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

public class MusicFragment extends Fragment implements IMusicView<MusicRoot> {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MusicInfo musicInfo = new MusicInfoImpl(this);

        musicListAdapter = new MusicListAdapter(getActivity(), musicsList);
        recyclerViewMusic.setAdapter(musicListAdapter);
        recyclerViewMusic.setLayoutManager(new LinearLayoutManager(getActivity()));

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
