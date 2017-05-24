package com.curry.note.module.news.book.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.book.Books;
import com.curry.note.module.news.net.DouBanService;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.sdv_music)
    SimpleDraweeView sdvMusic;
    @BindView(R.id.tv_book_name)
    TextView tvBookName;
    @BindView(R.id.tv_book_grade)
    TextView tvBookGrade;
    @BindView(R.id.tv_book_art)
    TextView tvBookArt;
    @BindView(R.id.tv_book_publishtime)
    TextView tvBookPublishtime;
    @BindView(R.id.tv_book_publish_address)
    TextView tvBookPublishAddress;
    @BindView(R.id.tv_book_grade_num)
    TextView tvBookGradeNum;
    @BindView(R.id.tv_want_read)
    TextView tvWantRead;
    @BindView(R.id.tv_more_info)
    TextView tvMoreInfo;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_author_description)
    TextView tvAuthorDescription;
    @BindView(R.id.rl_author)
    RelativeLayout rlAuthor;
    @BindView(R.id.tv_chapters)
    TextView tvChapters;
    private Books books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {

        toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                backThActivity();
                onBackPressed();
            }
        });
        toolbar.setTitle("－。－看书");
        toolbar.setTitleTextColor(Color.WHITE);

        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            if (!TextUtils.isEmpty(id)) {
                getData(id);
            }
        }
    }

    private void getData(String id) {
        Call<Books> call = DouBanService.Buidler.getDouBanService().getBookDetail(id);
        call.enqueue(new Callback<Books>() {
            @Override
            public void onResponse(Call<Books> call, Response<Books> response) {
                if (response.body() != null) {
                    books = response.body();
                    initViews();
                }
            }

            @Override
            public void onFailure(Call<Books> call, Throwable t) {

            }
        });

    }

    private void initViews() {

        if (books == null) {
            return;
        }
        if (books.getImages() != null) {
            sdvMusic.setImageURI(books.getImages().getLarge());
        }
        if (!TextUtils.isEmpty(books.getTitle())) {
            tvBookName.setText(books.getTitle());
        }
        if (books.getAuthor() != null && books.getAuthor().size() > 0) {
            tvBookArt.setText(books.getAuthor().get(0));
        }
        if (!TextUtils.isEmpty(books.getPublisher())) {
            tvBookPublishAddress.setText(books.getPublisher());
        }
        if (!TextUtils.isEmpty(books.getPubdate())) {
            tvBookPublishtime.setText("出版时间" + books.getPubdate());
        }
        if (!TextUtils.isEmpty(books.getPublisher())) {
            tvBookPublishAddress.setText(books.getPublisher());
        }

        if (!TextUtils.isEmpty(books.getSummary())) {
            tvDescription.setText(books.getSummary());
        }
        if (!TextUtils.isEmpty(books.getAuthor_intro())) {
            tvAuthorDescription.setText(books.getAuthor_intro());
        }
        if (!TextUtils.isEmpty(books.getCatalog())) {
            tvChapters.setText(books.getCatalog());
        }
        if (!TextUtils.isEmpty(books.getRating().getAverage()+"")) {
            tvBookGrade.setText(books.getRating().getAverage() + "分");
        }
        if (!TextUtils.isEmpty("" + books.getRating().getNumRaters())) {
            tvBookGradeNum.setText(books.getRating().getNumRaters() + "人评");
        }

    }


    @OnClick({R.id.tv_want_read, R.id.tv_more_info, R.id.rl_author})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_want_read:
            case R.id.tv_more_info:
                intent = new Intent(this, WebviewActivity.class);
                intent.putExtra(WebviewActivity.EXTRA_URL, books.getAlt());
                startActivity(intent);
                break;
            case R.id.rl_author:
                intent = new Intent(this, WebviewActivity.class);
                intent.putExtra(WebviewActivity.EXTRA_URL, books.getAlt());
                startActivity(intent);
                break;
        }
    }

}
