package com.curry.note.module.news.movie.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curry.note.R;
import com.curry.note.base.LazyBaseFragment2;
import com.curry.note.bean.top250.Root;
import com.curry.note.bean.top250.Subjects;
import com.curry.note.module.news.movie.adapter.MovieListAdapter;
import com.curry.note.module.news.movie.presenter.IMoviePresenter;
import com.curry.note.module.news.movie.presenter.MoviePresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class MovieFragment extends LazyBaseFragment2 implements IMovieView {

    @BindView(R.id.recyclerViewMovie)
    RecyclerView recyclerViewMovie;
    Unbinder unbinder;
    private List<Subjects> subjectsList = new ArrayList<>();
    private MovieListAdapter movieListAdapter;

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
        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieListAdapter.setOnItemClickListener(new MovieListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Subjects subjects) {
                Intent intent = new Intent(getActivity(), FilmDetailActivity.class);
                intent.putExtra("id", subjects.getId());
                startActivity(intent);
            }
        });

        IMoviePresenter iMoviePresenter = new MoviePresenterImpl(this);
        iMoviePresenter.getList("d", 0, 20);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onFinish() {

    }

    @Override
    public void onSuccess(Object data) {
        movieListAdapter.setData(((Root) data).getSubjects());
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
