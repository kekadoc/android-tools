package com.kekadoc.tools.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public interface ViewHolder {

    class Instance implements ViewHolder {

        private final View view;

        public Instance(View view) {
            this.view = view;
        }
        public Instance(@NonNull Context context, @LayoutRes int res) {
            this(LayoutInflater.from(context).inflate(res, null));
        }
        public Instance(@NonNull LayoutInflater inflater, @LayoutRes int res) {
            this(inflater.inflate(res, null));
        }
        public Instance(@NonNull Context context, @LayoutRes int res, ViewGroup root, boolean attach) {
            this(LayoutInflater.from(context).inflate(res, root, attach));
        }
        public Instance(@NonNull LayoutInflater inflater, @LayoutRes int res, ViewGroup root, boolean attach) {
            this(inflater.inflate(res, root, attach));
        }

        @Nullable
        @Override
        public View getView() {
            return view;
        }

    }

    @Nullable
    View getView();

    default @NonNull View requireView() {
        return Objects.requireNonNull(getView());
    }

    default boolean hasView() {
        return getView() != null;
    }

}
