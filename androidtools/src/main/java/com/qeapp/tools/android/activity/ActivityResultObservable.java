package com.qeapp.tools.android.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

public interface ActivityResultObservable {

    void addActivityResultObserver(@NonNull OnActivityResultObserver observer);
    void removeActivityResultObserver(OnActivityResultObserver observer);

    class Management implements ActivityResultObservable, OnActivityResultObserver {

        private @Nullable Collection<OnActivityResultObserver> observers;

        @Override
        public void addActivityResultObserver(@NonNull OnActivityResultObserver observer) {
            if (this.observers == null) this.observers = new LinkedHashSet<>();
            this.observers.add(Objects.requireNonNull(observer));
        }
        @Override
        public void removeActivityResultObserver(OnActivityResultObserver observer) {
            if (this.observers == null) return;
            this.observers.remove(observer);
        }
        @Override
        public void onResult(int requestCode, int resultCode, Intent data) {
            if (this.observers != null)
                for (OnActivityResultObserver observer : this.observers)
                    observer.onResult(requestCode, resultCode, data);
        }

    }

}
