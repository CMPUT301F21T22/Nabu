package ca.cmput301f21t22.nabu.model;

public interface Observable<TProperties extends Enum<TProperties>> {
    void observe(PropertyChangedCallback<TProperties> callback);

    interface PropertyChangedCallback<TProperties extends Enum<TProperties>> {
        void onPropertyChanged(Observable<TProperties> sender, TProperties property);
    }
}
