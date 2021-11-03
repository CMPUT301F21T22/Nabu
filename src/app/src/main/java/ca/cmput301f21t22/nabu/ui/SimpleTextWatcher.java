package ca.cmput301f21t22.nabu.ui;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

public class SimpleTextWatcher implements TextWatcher {
    @NonNull
    private final TextChangeListener listener;

    public SimpleTextWatcher(@NonNull TextChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        this.listener.onTextChanged(editable);
    }
}
