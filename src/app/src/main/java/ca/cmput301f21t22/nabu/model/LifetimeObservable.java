package ca.cmput301f21t22.nabu.model;

public interface LifetimeObservable {
    void addLifetimeChangeCallback(LifetimeChangeCallback callback);

    void removeLifetimeChangeCallback(LifetimeChangeCallback callback);

    void clearLifetimeChangeCallbacks();
}
