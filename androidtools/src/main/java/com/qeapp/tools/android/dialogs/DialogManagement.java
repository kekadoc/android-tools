package com.qeapp.tools.android.dialogs;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

public interface DialogManagement {

    interface Observable {

        void addDialogObserver(@NonNull Observer observer);
        void removeDialogObserver(Observer observer);

        class Management implements Observer, Observable {

            private @Nullable Collection<Observer> observers;

            @Override
            public final void addDialogObserver(@NonNull Observer observer) {
                if (this.observers == null) this.observers = new LinkedHashSet<>();
                this.observers.add(Objects.requireNonNull(observer));
            }
            @Override
            public final void removeDialogObserver(Observer observer) {
                if (this.observers == null) return;
                this.observers.remove(observer);
            }

            @Override
            public final void onDialogClose(@NonNull Dialog dialog) {
                if (this.observers != null) for (Observer observer : this.observers) observer.onDialogClose(dialog);
            }
            @Override
            public final void onDialogShow(@NonNull Dialog dialog) {
                if (this.observers != null) for (Observer observer : this.observers) observer.onDialogShow(dialog);
            }

        }

    }
    interface Observer {
        /** Слушатель открытия диалога */
        void onDialogShow(@NonNull Dialog dialog);
        /** Слушатель закрытия диалога */
        void onDialogClose(@NonNull Dialog dialog);
    }

    void showDialog(@NonNull Dialog dialog);

}
