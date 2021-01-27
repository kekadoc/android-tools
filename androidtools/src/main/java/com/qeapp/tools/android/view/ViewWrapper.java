package com.qeapp.tools.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.Objects;

public class ViewWrapper implements ViewHolder {

    private View view;

    public ViewWrapper(@NonNull View view) {
        setView(view);
    }
    public ViewWrapper(@NonNull Context context, @LayoutRes int res) {
        setView(onViewInflate(context, res));
    }

    public void setView(@NonNull View view) {
        this.view = Objects.requireNonNull(view);
        onViewAttached(view);
    }

    protected View onViewInflate(@NonNull Context context, @LayoutRes int res) {
        return LayoutInflater.from(context).inflate(res, null, false);
    }

    protected void onViewAttached(@NonNull View view) {

    }

    protected @NonNull Context getContext() {
        return view.getContext();
    }

    @Override
    public @NonNull View getView() {
        return view;
    }

}
