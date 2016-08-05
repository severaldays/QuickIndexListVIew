package com.severaldays.quickindexlistview.index;

import android.support.annotation.NonNull;

/**
 *
 * @author LingJian•He
 * created at 16/8/5
 *
 */
public class QuickIndexItem implements Comparable {

    private String index;
    private String name;

    public QuickIndexItem(String index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NonNull Object another) {
        if (index != null) {
            // # always rank last
            if (index.equals("#")) {
                return index.equals(((QuickIndexItem) another).index) ? 0 : 1;
            } else if ("#".equals(((QuickIndexItem) another).index)) {
                return index.equals(((QuickIndexItem) another).index) ? 0 : -1;
            } else {
                return index.compareTo(((QuickIndexItem) another).index);
            }
        }
        return 0;
    }
}