package ca.cmput301f21t22.nabu.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Objects;
import java.util.WeakHashMap;

import ca.cmput301f21t22.nabu.model.Observable;

public class DateChangedReceiver extends BroadcastReceiver implements Observable<DateChangedReceiver.Properties> {
    @NonNull
    public final static String TAG = "DateChangedReceiver";

    @NonNull
    private final WeakHashMap<PropertyChangedCallback<DateChangedReceiver.Properties>, Boolean> propertyCallbacks;

    @NonNull
    private LocalDate now;

    public DateChangedReceiver() {
        this.propertyCallbacks = new WeakHashMap<>();
        this.now = LocalDate.now();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Objects.equals(action, Intent.ACTION_DATE_CHANGED) || Objects.equals(action, Intent.ACTION_TIME_CHANGED) ||
            Objects.equals(action, Intent.ACTION_TIMEZONE_CHANGED)) {
            this.now = LocalDate.now();
            this.notifyPropertyChanged();
        }
    }

    @Override
    public void observe(PropertyChangedCallback<DateChangedReceiver.Properties> callback) {
        this.propertyCallbacks.put(callback, true);
        this.notifyPropertyChanged();
    }

    @NonNull
    public LocalDate getNow() {
        return this.now;
    }

    protected void notifyPropertyChanged() {
        for (PropertyChangedCallback<DateChangedReceiver.Properties> callback : this.propertyCallbacks.keySet()) {
            callback.onPropertyChanged(this, Properties.NOW);
        }
    }

    public enum Properties {
        NOW
    }
}
