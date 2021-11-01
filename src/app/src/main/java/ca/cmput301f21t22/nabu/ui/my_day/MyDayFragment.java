package ca.cmput301f21t22.nabu.ui.my_day;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.FragmentMydayBinding;
import ca.cmput301f21t22.nabu.databinding.HeaderCalendarBinding;
import ca.cmput301f21t22.nabu.ui.ExtendedToolbarFragment;

public class MyDayFragment extends ExtendedToolbarFragment {
    @NonNull
    public final static String TAG = "MyDayFragment";

    @Nullable
    private MyDayViewModel viewModel;
    @Nullable
    private FragmentMydayBinding binding;
    @Nullable
    private HeaderCalendarBinding toolbar;
    @Nullable
    private MyDayCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(MyDayViewModel.class);
        this.binding = FragmentMydayBinding.inflate(inflater, container, false);
        this.toolbar = HeaderCalendarBinding.inflate(LayoutInflater.from(this.getContext()));
        this.adapter = new MyDayCardAdapter(this.viewModel.getCards());

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        this.requireContext().registerReceiver(this.viewModel.getDateReceiver(), filter);

        this.viewModel.getNow().observe(this.getViewLifecycleOwner(), (now) -> {
            TextView[] daysOfWeek = {
                    this.toolbar.labelDayOfWeek0,
                    this.toolbar.labelDayOfWeek1,
                    this.toolbar.labelDayOfWeek2,
                    this.toolbar.labelDayOfWeek3,
                    this.toolbar.labelDayOfWeek4,
                    this.toolbar.labelDayOfWeek5,
                    this.toolbar.labelDayOfWeek6,
                    };
            TextView[] dates = {
                    this.toolbar.labelDate0,
                    this.toolbar.labelDate1,
                    this.toolbar.labelDate2,
                    this.toolbar.labelDate3,
                    this.toolbar.labelDate4,
                    this.toolbar.labelDate5,
                    this.toolbar.labelDate6,
                    };

            LocalDate day = now;
            for (int i = 0; i < 7; i++) {
                daysOfWeek[i].setText(day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
                dates[i].setText(String.format(Locale.getDefault(), "%d", day.getDayOfMonth()));
                day = day.minusDays(1);
            }
        });

        this.binding.listIncomplete.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        this.binding.listIncomplete.setAdapter(this.adapter);

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        if (this.viewModel != null) {
            this.requireContext().unregisterReceiver(this.viewModel.getDateReceiver());
        }

        super.onDestroyView();
    }

    @Nullable
    @Override
    public View getToolbarView() {
        if (this.toolbar != null) {
            this.toolbar.title.setText(R.string.fragment_myday_name);
            return this.toolbar.getRoot();
        }
        return null;
    }
}