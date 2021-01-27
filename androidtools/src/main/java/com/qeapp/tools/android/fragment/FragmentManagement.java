package com.qeapp.tools.android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

/** Интерфейс для работы с Fragment's */
public interface FragmentManagement {

    interface Observable {

        void addFragmentObserver(@NonNull Observer observer);
        void removeFragmentObserver(Observer observer);

        class Management implements Observer, Observable {

            private @Nullable Collection<Observer> observers;

            @Override
            public void addFragmentObserver(@NonNull Observer observer) {
                if (this.observers == null) this.observers = new LinkedHashSet<>();
                this.observers.add(Objects.requireNonNull(observer));
            }
            @Override
            public void removeFragmentObserver(Observer observer) {
                if (this.observers == null) this.observers = new LinkedHashSet<>();
                this.observers.remove(observer);
            }

            @Override
            public void onFragmentAttached(@NonNull Fragment fragment) {
                if (this.observers != null) for (Observer listener : observers)
                    listener.onFragmentAttached(fragment);
            }
            @Override
            public void onFragmentDetached(@NonNull Fragment fragment) {
                if (this.observers != null) for (Observer listener : observers)
                    listener.onFragmentDetached(fragment);
            }
        }

    }

    /** События от фрагментов */
    interface Observer {
        /**
         *
         * */
        default void onFragmentAttached(@NonNull Fragment fragment) {}
        /**
         *
         * */
        default void onFragmentDetached(@NonNull Fragment fragment) {}
    }

    /** Запрос фрагмента о смене Фрагмента */
    void switchFragment(@NonNull Fragment fragment);
    /** Предыдущий фрагмента */
    void previousFragment();

    boolean hasBackFragment();

}
