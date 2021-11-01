package ca.cmput301f21t22.nabu.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public abstract class ExtendedToolbarFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) this.requireActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        View root = this.getToolbarView();
        if (actionBar != null && root != null) {
            actionBar.setCustomView(root, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                                                     ActionBar.LayoutParams.WRAP_CONTENT));
            Toolbar toolbar = (Toolbar) root.getParent();
            if (toolbar != null) {
                toolbar.setContentInsetsAbsolute(0, 0);
            }
        }
    }

    @Nullable
    public abstract View getToolbarView();
}
