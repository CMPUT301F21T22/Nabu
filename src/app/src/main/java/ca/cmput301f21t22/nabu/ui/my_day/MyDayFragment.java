package ca.cmput301f21t22.nabu.ui.my_day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ca.cmput301f21t22.nabu.databinding.FragmentMydayBinding;

public class MyDayFragment extends Fragment {

    @Nullable
    private MyDayViewModel viewModel;
    @Nullable
    private FragmentMydayBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(MyDayViewModel.class);
        this.binding = FragmentMydayBinding.inflate(inflater, container, false);

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        this.viewModel = null;
        this.binding = null;
        super.onDestroyView();
    }
}