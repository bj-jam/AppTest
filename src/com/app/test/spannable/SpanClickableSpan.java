package com.app.test.spannable;

/**
 * @author lcx
 * Created at 2020.1.20
 * Describe:
 */
public class SpanClickableSpan extends ClickableSpanNoUnderline {

    private String urlString;

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public SpanClickableSpan(int color, OnClickListener onClickListener) {
        super(color, onClickListener);
    }

    public SpanClickableSpan(OnClickListener onClickListener) {
        super(onClickListener);
    }

}
