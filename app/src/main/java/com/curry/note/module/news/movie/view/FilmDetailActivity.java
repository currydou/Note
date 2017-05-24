package com.curry.note.module.news.movie.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.filmdetail.FilmDetail;
import com.curry.note.bean.filmdetail.FilmPeople;
import com.curry.note.bean.top250.Casts;
import com.curry.note.bean.top250.Directors;
import com.curry.note.module.news.book.view.WebviewActivity;
import com.curry.note.module.news.movie.adapter.CastAdapter;
import com.curry.note.module.news.movie.adapter.EasyRecyclerViewAdapter;
import com.curry.note.module.news.movie.adapter.FullyLinearLayoutManager;
import com.curry.note.module.news.net.DouBanService;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by forezp on 16/9/24.
 */
public class FilmDetailActivity extends AppCompatActivity {


    public static String EXTRA_ID = "id";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sdv_film)
    SimpleDraweeView sdvFilm;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_rating_num)
    TextView tvRatingNum;
    @BindView(R.id.tv_date_and_film_time)
    TextView tvDateAndFilmTime;
    @BindView(R.id.tv_film_type)
    TextView tvFilmType;
    @BindView(R.id.tv_film_country)
    TextView tvFilmCountry;
    @BindView(R.id.tv_film_name)
    TextView tvFilmName;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_more_info)
    TextView tvMoreInfo;

    private String id;//
    private Context context;
    private String alt;
    private FilmDetail filmDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_film_detail);
        ButterKnife.bind(this);
        context = this;
//        applyKitKatTranslucency();
        initView();
        initData();
    }

    private void initView() {

//        toolbar.setBackgroundColor(ThemeUtils.getThemeColor());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                backThActivity();
            }
        });


    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra(EXTRA_ID);
        }
        if (!TextUtils.isEmpty(id)) {
            getData(id);
        }
    }

    private void getData(String id) {
        DouBanService douBanService = DouBanService.Buidler.getDouBanService();
        Call<FilmDetail> call = douBanService.getFilmDetail(id);
        call.enqueue(new Callback<FilmDetail>() {
            @Override
            public void onResponse(Call<FilmDetail> call, Response<FilmDetail> response) {
                if (response.body() != null) {
                    filmDetail = response.body();
                    if (filmDetail.getImages() != null && filmDetail.getImages().getLarge() != null) {
                        sdvFilm.setImageURI(filmDetail.getImages().getLarge());
                    }
                    if (!TextUtils.isEmpty(filmDetail.getTitle())) {
                        toolbar.setTitle(filmDetail.getTitle());
                    }
                    if (filmDetail.getRating() != null) {
                        tvRating.setText("评分" + filmDetail.getRating().getAverage());
                    }
                    tvRatingNum.setText(filmDetail.getRatings_count() + "人评分");
                    tvDateAndFilmTime.setText(filmDetail.getYear() + "年  出品");
                    if (filmDetail.getCountries() != null && filmDetail.getCountries().size() > 0) {
                        tvFilmCountry.setText(filmDetail.getCountries().get(0));
                    }
                    if (filmDetail.getGenres() != null && filmDetail.getGenres().size() > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String s : filmDetail.getGenres()) {
                            stringBuilder.append(s + "/");
                        }
                        tvFilmType.setText(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
                    }
                    tvDescription.setText(filmDetail.getSummary());
                    tvFilmName.setText(filmDetail.getOriginal_title() + " [原名]");
                    initFilmData(filmDetail);
                    CastAdapter castAdapter = new CastAdapter(FilmDetailActivity.this);
                    castAdapter.setDatas(list);
                    FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(FilmDetailActivity.this);
                    recyclerview.setLayoutManager(fullyLinearLayoutManager);
                    recyclerview.setAdapter(castAdapter);
                    alt = filmDetail.getAlt();
                    castAdapter.setOnItemClickListener(new EasyRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position, Object data) {
                            Intent intent = new Intent(FilmDetailActivity.this, WebviewActivity.class);
                            String alt = list.get(position).getAlt();
                            intent.putExtra(WebviewActivity.EXTRA_URL, alt);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<FilmDetail> call, Throwable t) {

            }
        });
    }

    private List<FilmPeople> list = new ArrayList<>();

    private void initFilmData(FilmDetail filmDetail) {

        if (filmDetail.getDirectors() != null && filmDetail.getDirectors().size() > 0) {
            for (int i = 0; i < filmDetail.getDirectors().size(); i++) {
                Directors directors = filmDetail.getDirectors().get(i);
                FilmPeople filmPeople = new FilmPeople();

                filmPeople.setAlt(directors.getAlt());
                filmPeople.setAvatars(directors.getAvatars());
                filmPeople.setId(directors.getId());
                filmPeople.setName(directors.getName());
                filmPeople.setType(1);
                list.add(filmPeople);
            }
        }
        if (filmDetail.getCasts() != null && filmDetail.getCasts().size() > 0) {
            for (int i = 0; i < filmDetail.getCasts().size(); i++) {
                Casts casts = filmDetail.getCasts().get(i);
                FilmPeople filmPeople = new FilmPeople();

                filmPeople.setAlt(casts.getAlt());
                filmPeople.setAvatars(casts.getAvatars());
                filmPeople.setId(casts.getId());
                filmPeople.setName(casts.getName());
                filmPeople.setType(2);
                list.add(filmPeople);
            }
        }

    }


    @OnClick({R.id.sdv_film, R.id.tv_more_info})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.sdv_film:
            case R.id.tv_more_info:
                intent = new Intent(this, WebviewActivity.class);
                intent.putExtra(WebviewActivity.EXTRA_URL, alt);
                startActivity(intent);
                break;
        }
    }
}
