package ca.cmput301f21t22.nabu.model;

public interface PropertyObservable<TProperties extends Enum<TProperties>> {
    void addPropertyChangeCallback(PropertyChangeCallback<TProperties> callback);

    void removePropertyChangeCallback(PropertyChangeCallback<TProperties> callback);

    void clearPropertyChangeCallbacks();
}
