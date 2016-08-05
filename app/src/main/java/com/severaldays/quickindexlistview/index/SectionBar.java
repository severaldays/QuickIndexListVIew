package com.severaldays.quickindexlistview.index;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 * @author LingJian•He
 * created at 16/8/5
 *
 */
public class SectionBar extends View {

    private static final char[] WORDS = new char[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '#'
    };

    private int wordSize = 13;
    private int preHeight = wordSize + 2;
    private int heightGap = 2;
    private int topAndBottomPadding = 10;
    private TextView textDialog;

    public void setTextView(TextView dialog) {
        this.textDialog = dialog;
    }

    private ListView listView;
    private Indexer indexer;
    private int heightCenter;
    private Paint paint;

    public SectionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        wordSize = convertDpToPixel(wordSize, getResources());
        preHeight = convertDpToPixel(preHeight, getResources());
        heightGap = convertDpToPixel(heightGap, getResources());
        topAndBottomPadding = convertDpToPixel(topAndBottomPadding, getResources());
        paint = new Paint();
        paint.setColor(0xFF000000);
        paint.setTextSize(preHeight);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
    }

    public void setListView(ListView listView) {
        this.listView = listView;
        Adapter adapter = this.listView.getAdapter();
        if (adapter == null || !(adapter instanceof Indexer)) {
            throw new RuntimeException("ListView must set Adapter or Adapter must implements Indexer interface");
        }
        indexer = (Indexer) adapter;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int startY = (int) event.getY() - heightCenter;
        int index = startY / preHeight;
        if (index >= WORDS.length) {
            index = WORDS.length - 1;
        } else if (index < 0) {
            index = 0;
        }
        int position;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                position = indexer.getStartPositionOfSection(String.valueOf(WORDS[index]));
                if (position == -1) {
                    return true;
                }
                listView.setSelection(position);
                break;
            case MotionEvent.ACTION_MOVE:
                position = indexer.getStartPositionOfSection(String.valueOf(WORDS[index]));
                if (position == -1) {
                    return true;
                }
                if (textDialog != null) {
                    textDialog.setText(String.valueOf(WORDS[index]));
                    textDialog.setVisibility(View.VISIBLE);
                }
                listView.setSelection(position);
                break;
            case MotionEvent.ACTION_UP:
                if (textDialog != null) {
                    textDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                topAndBottomPadding + (preHeight + heightGap) * WORDS.length);
        heightCenter = (getMeasuredHeight() - (preHeight + heightGap) * WORDS.length) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < WORDS.length; i++) {
            canvas.drawText(String.valueOf(WORDS[i]), getMeasuredWidth() / 2,
                    preHeight + (i * preHeight) + i * heightGap + heightCenter, paint);
        }
        super.onDraw(canvas);
    }

    public int convertDpToPixel(float dp, Resources res) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                res.getDisplayMetrics());
    }

}
