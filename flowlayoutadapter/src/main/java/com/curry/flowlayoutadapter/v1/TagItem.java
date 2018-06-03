package com.curry.flowlayoutadapter.v1;

/**
 * Created by next on 2017/12/7.
 */

public class TagItem {
    private String text;
    /**
     * 不需要和text对应的并且不需要使用单选模式的，number可以不要
     */
    private String number;

    public TagItem() {
    }

    public TagItem(String text) {
        this.text = text;
    }

    public TagItem(String number, String text) {
        this.text = text;
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
