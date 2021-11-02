package ca.cmput301f21t22.nabu.model;

public interface Command<TResult> {
    TResult execute();
}
