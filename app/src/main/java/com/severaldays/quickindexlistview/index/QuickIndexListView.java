package com.severaldays.quickindexlistview.index;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 * @author LingJian•He
 * created at 16/8/5
 *
 */
public class QuickIndexListView extends ListView implements AbsListView.OnScrollListener {
    private QuickIndexAdapter quickIndexAdapter;
    private TextView headerView;
    private final int HEAD_VISIBLE = 0;
    private final int HEAD_PUSH_UP = 1;
    private final int HEAD_GONE = 2;

    public QuickIndexListView(Context context) {
        this(context, null);
    }

    public QuickIndexListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        registerListener();
    }

    private void registerListener() {
        setOnScrollListener(this);
    }

    public void setHeaderView(TextView view) {
        headerView = view;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void configureHeaderView(View firstChild, int firstVisibleItem) {
        QuickIndexItem quickIndexItem = (QuickIndexItem) quickIndexAdapter.getItem(firstVisibleItem);
        headerView.setText(quickIndexItem.getIndex());
        int state = getHeadViewState(firstChild, firstVisibleItem);

        switch (state) {
            case HEAD_VISIBLE:
                headerView.setVisibility(VISIBLE);
                headerView.setTranslationY(0);
                break;
            case HEAD_PUSH_UP:
                int childBottom = firstChild.getBottom();
                int headerHeight = headerView.getHeight();
                int yOffset;
                if (childBottom < headerHeight) {
                    yOffset = (childBottom - headerHeight);
                } else {
                    yOffset = 0;
                }
                headerView.setTranslationY(yOffset);
                break;
            case HEAD_GONE:
                headerView.setVisibility(GONE);
                break;
            default:
                break;
        }
    }

    public int getHeadViewState(View view, int firstVisibleItem) {
        int headerHeight = headerView.getMeasuredHeight();
        View nextView = getChildAt(1);
        int nextViewTop = nextView.getTop();
        boolean isFirstVisibleViewHaveSectionBar = ((ViewGroup) view).getChildAt(0).getVisibility() == VISIBLE;
        boolean isNextViewHaveSectionBar = ((ViewGroup) nextView).getChildAt(0).getVisibility() == VISIBLE;
        if (isNextViewHaveSectionBar && nextViewTop < headerHeight) {
            return HEAD_PUSH_UP;
        } else if (!isFirstVisibleViewHaveSectionBar || isFirstVisibleViewHaveSectionBar && view.getTop() < 0) {
            return HEAD_VISIBLE;
        } else if (firstVisibleItem == 0 && view.getTop() == 0) {
            return HEAD_GONE;
        }
        return HEAD_GONE;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        quickIndexAdapter = (QuickIndexAdapter) adapter;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getChildCount() >= 2) {
            configureHeaderView(getChildAt(0), firstVisibleItem);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}
