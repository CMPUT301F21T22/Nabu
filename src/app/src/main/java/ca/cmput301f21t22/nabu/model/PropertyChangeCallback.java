package ca.cmput301f21t22.nabu.model;

public interface PropertyChangeCallback<T extends Enum<T>> {
    void onPropertyChanged(PropertyObservable<T> sender, T property);
}
