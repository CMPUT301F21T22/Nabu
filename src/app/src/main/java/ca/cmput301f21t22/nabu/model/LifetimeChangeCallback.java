package ca.cmput301f21t22.nabu.model;

public interface LifetimeChangeCallback {
    void onLifetimeChanged(LifetimeObservable sender, boolean alive);
}
