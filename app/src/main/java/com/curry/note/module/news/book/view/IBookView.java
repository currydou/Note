package com.curry.note.module.news.book.view;

import com.curry.note.bean.BookRoot;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public interface IBookView {
    void showSuccessPage(BookRoot bookRoot);

    void showFailPage();
}
