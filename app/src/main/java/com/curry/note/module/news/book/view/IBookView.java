package com.curry.note.module.news.book.view;


import com.curry.note.bean.book.BookRoot;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public interface IBookView {
    void showLoadingPage();

    void showSuccessPage(BookRoot bookRoot, boolean isLoadMore);

    void showFailPage();
}
