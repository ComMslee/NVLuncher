package com.nv.lutil.listener;

/*
    Item 클릭
 */
public interface OnItemClickListener<T> {
    void onItemClick(int position, T model);
    boolean onItemLongClick(int position, T model);
}
