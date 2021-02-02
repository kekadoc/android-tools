package com.kekadoc.tools.android.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

public interface ActivityResultObservable {

    void addActivityResultObserver(@NonNull ActivityResultObserver observer);
    void removeActivityResultObserver(ActivityResultObserver observer);

    class Management implements ActivityResultObservable, ActivityResultObserver {

        private @Nullable Collection<ActivityResultObserver> observers;

        @Override
        public void addActivityResultObserver(@NonNull ActivityResultObserver observer) {
            if (this.observers == null) this.observers = new LinkedHashSet<>();
            this.observers.add(Objects.requireNonNull(observer));
        }
        @Override
        public void removeActivityResultObserver(ActivityResultObserver observer) {
            if (this.observers == null) return;
            this.observers.remove(observer);
        }
        @Override
        public void onResult(int requestCode, int resultCode, Intent data) {
            if (this.observers != null)
                for (ActivityResultObserver observer : this.observers)
                    observer.onResult(requestCode, resultCode, data);
        }

    }

}
