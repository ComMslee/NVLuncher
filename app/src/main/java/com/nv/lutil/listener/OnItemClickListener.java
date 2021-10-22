package com.nv.lutil.listener;

/*
    Item 클릭
 */
@FunctionalInterface
public interface OnItemClickListener<T> {
    void onItemClick(int position, T model);
}
