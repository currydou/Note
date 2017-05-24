package com.curry.note.module.news.music.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.music.Musics;
import com.curry.note.module.news.book.view.WebviewActivity;
import com.curry.note.module.news.net.DouBanService;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by forezp on 16/10/1.
 */
public class MusicDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.sdv_music)
    SimpleDraweeView sdvMusic;
    @BindView(R.id.tv_music_name)
    TextView tvMusicName;
    @BindView(R.id.tv_music_art)
    TextView tvMusicArt;
    @BindView(R.id.tv_music_publishtime)
    TextView tvMusicPublishtime;
    @BindView(R.id.tv_music_grade)
    TextView tvMusicGrade;
    @BindView(R.id.tv_music_grade_num)
    TextView tvMusicGradeNum;
    @BindView(R.id.tv_listen)
    TextView tvListen;
    @BindView(R.id.tv_more_info)
    TextView tvMoreInfo;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_songs)
    TextView tvSongs;
    private Musics music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        ButterKnife.bind(this);
//        applyKitKatTranslucency(getResources().getColor(R.color.black));
        initView();
        initData();
    }


    private void initView() {

        toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                backThActivity();
                onBackPressed();
            }
        });
        toolbar.setTitle("－。－音乐");


    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            if (!TextUtils.isEmpty(id)) {
                getData(id);
            }
        }

    }

    private void getData(String id) {
        DouBanService douBanService = DouBanService.Buidler.getDouBanService();
        Call<Musics> call = douBanService.getMusicDetail(id);
        call.enqueue(new Callback<Musics>() {
            @Override
            public void onResponse(Call<Musics> call, Response<Musics> response) {
                if (response.body() != null) {
                    music = response.body();
                    sdvMusic.setImageURI(music.getImage());
                    tvMusicName.setText(music.getTitle());
                    if (music.getAuthor() != null && music.getAuthor().size() > 0) {
                        tvMusicArt.setText("艺术家：" + music.getAuthor().get(0).getName());
                    }
                    if (music.getAttrs() != null && music.getAttrs().getPubdate() != null && music.getAttrs().getPubdate().size() > 0) {
                        tvMusicPublishtime.setText(music.getAttrs().getPubdate().get(0));
                    }
                    if (music.getRating() != null) {
                        if (!TextUtils.isEmpty(music.getRating().getAverage()))
                            tvMusicGrade.setText(music.getRating().getAverage());
                        if (!TextUtils.isEmpty("" + music.getRating().getNumRaters())) {
                            tvMusicGradeNum.setText(music.getRating().getNumRaters() + "人评");
                        }
                    }
                    if (!TextUtils.isEmpty(music.getSummary())) {
                        tvDescription.setText(music.getSummary());
                    }
                    if (music.getAttrs().getTracks() != null && music.getAttrs().getTracks().size() > 0) {
                        tvSongs.setText(music.getAttrs().getTracks().get(0));
                    }

                }
            }

            @Override
            public void onFailure(Call<Musics> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.tv_listen, R.id.tv_more_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_listen:
            case R.id.tv_more_info:
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra(WebviewActivity.EXTRA_URL, music.getAlt());
                startActivity(intent);
                break;
        }
    }
}
