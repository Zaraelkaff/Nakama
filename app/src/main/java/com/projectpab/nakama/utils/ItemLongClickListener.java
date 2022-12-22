package com.projectpab.nakama.utils;

import android.view.View;

public interface ItemLongClickListener<T> {
    void onItemLongClick(View view, T data, int position);
}
