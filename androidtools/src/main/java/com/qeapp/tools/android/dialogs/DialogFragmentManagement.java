package com.qeapp.tools.android.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

/** Интерфейс по работе с Dialog */
public interface DialogFragmentManagement {

    interface Observable {

        void addDialogFragmentObserver(@NonNull Observer observer);
        void removeDialogFragmentObserver(Observer observer);

        class Management implements Observer, Observable {

            private @Nullable Collection<Observer> observers;

            @Override
            public final void addDialogFragmentObserver(@NonNull Observer observer) {
                if (this.observers == null) this.observers = new LinkedHashSet<>();
                this.observers.add(Objects.requireNonNull(observer));
            }
            @Override
            public final void removeDialogFragmentObserver(Observer observer) {
                if (this.observers == null) return;
                this.observers.remove(observer);
            }

            @Override
            public final void onDialogFragmentShow(@NonNull DialogFragment dialog) {
                if (this.observers != null) for (Observer observer : this.observers) observer.onDialogFragmentShow(dialog);
            }
            @Override
            public final void onDialogFragmentClose(@NonNull DialogFragment dialog) {
                if (this.observers != null) for (Observer observer : this.observers) observer.onDialogFragmentClose(dialog);
            }

        }

    }
    interface Observer {
        /** Слушатель открытия диалога */
        void onDialogFragmentShow(@NonNull DialogFragment dialog);
        /** Слушатель закрытия диалога */
        void onDialogFragmentClose(@NonNull DialogFragment dialog);
    }

    /** Показать диалог */
    void showDialog(@NonNull DialogFragment dialog);

}
