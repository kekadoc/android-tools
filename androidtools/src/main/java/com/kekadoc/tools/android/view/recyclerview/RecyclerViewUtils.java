package com.kekadoc.tools.android.view.recyclerview;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kekadoc.tools.data.ListDataProvider;
import com.kekadoc.tools.android.AndroidUtils;

import java.util.List;

public class RecyclerViewUtils {

    public static <T, VH extends RecyclerView.ViewHolder> void notifyData(@Nullable RecyclerView recyclerView,
                                                                          @Nullable ListAdapter<T, VH> adapter,
                                                                          @Nullable List<T> data, @Nullable Runnable callback) {
        if (adapter == null) {
            if (callback != null) callback.run();
            return;
        }
        if (AndroidUtils.isUiThread()) adapter.submitList(data, callback);
        else {
            Runnable submit = () -> adapter.submitList(data);
            if (recyclerView != null) recyclerView.post(submit);
            else AndroidUtils.runInMainThread(submit);
        }
    }
    public static <T, VH extends RecyclerView.ViewHolder> void notifyData(@Nullable RecyclerView recyclerView,
                                                                          @Nullable ListAdapter<T, VH> adapter,
                                                                          @Nullable ListDataProvider<T> data, @Nullable Runnable callback) {
        List<T> d = data == null? null : data.getListData();
        notifyData(recyclerView, adapter, d, callback);
    }

}
