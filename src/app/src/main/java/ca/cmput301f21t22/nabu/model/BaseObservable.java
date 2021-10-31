package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.WeakHashMap;

public abstract class BaseObservable<TProperties extends Enum<TProperties>> implements Observable<TProperties> {
    @NonNull
    private final Class<TProperties> cls;
    @NonNull
    private final WeakHashMap<PropertyChangedCallback<TProperties>, Boolean> propertyCallbacks;

    public BaseObservable(@NonNull Class<TProperties> cls) {
        this.cls = cls;
        this.propertyCallbacks = new WeakHashMap<>();
    }

    @Override
    public void observe(PropertyChangedCallback<TProperties> callback) {
        this.propertyCallbacks.put(callback, true);
        for (TProperties property : Objects.requireNonNull(this.cls.getEnumConstants())) {
            this.notifyPropertyChanged(property);
        }
    }

    protected void notifyPropertyChanged(TProperties property) {
        for (PropertyChangedCallback<TProperties> callback : this.propertyCallbacks.keySet()) {
            callback.onPropertyChanged(this, property);
        }
    }
}
