package ca.cmput301f21t22.nabu.dialogs.edit_habit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.databinding.FragmentEditHabitBinding;
import ca.cmput301f21t22.nabu.dialogs.date_picker.DatePickerFragment;
import ca.cmput301f21t22.nabu.ui.SimpleTextWatcher;

public class EditHabitFragment extends DialogFragment {
    @NonNull
    public final static String TAG = "EditHabitFragment";

    public final static String ARG_USER = "User";
    public final static String ARG_HABIT = "Habit";

    @NonNull
    private final DateFormat dateFormat;
    @Nullable
    private EditHabitViewModel viewModel;
    @Nullable
    private FragmentEditHabitBinding binding;

    private EditHabitFragment() {
        this.dateFormat = DateFormat.getDateInstance();
    }

    public static EditHabitFragment newInstance(@NonNull User user) {
        EditHabitFragment fragment = new EditHabitFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditHabitFragment newInstance(@NonNull Habit habit) {
        EditHabitFragment fragment = new EditHabitFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_HABIT, habit);
        fragment.setArguments(args);
        return fragment;
    }

    private static String retrieveText(TextView textView) {
        CharSequence t = textView.getText();
        if (t != null) {
            return t.toString();
        } else {
            return "";
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(STYLE_NORMAL, R.style.Theme_MaterialComponents_DayNight_DialogWhenLarge);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(EditHabitViewModel.class);
        this.binding = FragmentEditHabitBinding.inflate(inflater, container, false);

        this.binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);
        this.binding.toolbar.setNavigationOnClickListener(view -> this.dismiss());
        this.binding.toolbar.setTitle(R.string.title_edit_habit);

        this.viewModel.isSaved().observe(this.getViewLifecycleOwner(), saved -> {
            if (saved) {
                this.binding.buttonSave.hide();
            } else {
                this.binding.buttonSave.show();
            }
        });
        this.viewModel.getTitle().observe(this.getViewLifecycleOwner(), title -> {
            EditText edit = this.binding.editTitle;
            if (!Objects.equals(title, edit.getText().toString())) {
                edit.setText(title);
            }
        });
        this.viewModel.getReason().observe(this.getViewLifecycleOwner(), reason -> {
            EditText edit = this.binding.editReason;
            if (!Objects.equals(reason, edit.getText().toString())) {
                edit.setText(reason);
            }
        });
        this.viewModel.getStartDate().observe(this.getViewLifecycleOwner(), date -> {
            String dateString = this.dateFormat.format(date);
            this.binding.textStartDate.setText(dateString);
        });
        this.viewModel.isOnSunday().observe(this.getViewLifecycleOwner(), value -> {
            ToggleButton button = this.binding.buttonOnSunday;
            if (!Objects.equals(value, button.isChecked())) {
                button.setChecked(value);
            }
        });
        this.viewModel.isOnMonday().observe(this.getViewLifecycleOwner(), value -> {
            ToggleButton button = this.binding.buttonOnMonday;
            if (!Objects.equals(value, button.isChecked())) {
                button.setChecked(value);
            }
        });
        this.viewModel.isOnTuesday().observe(this.getViewLifecycleOwner(), value -> {
            ToggleButton button = this.binding.buttonOnTuesday;
            if (!Objects.equals(value, button.isChecked())) {
                button.setChecked(value);
            }
        });
        this.viewModel.isOnWednesday().observe(this.getViewLifecycleOwner(), value -> {
            ToggleButton button = this.binding.buttonOnWednesday;
            if (!Objects.equals(value, button.isChecked())) {
                button.setChecked(value);
            }
        });
        this.viewModel.isOnThursday().observe(this.getViewLifecycleOwner(), value -> {
            ToggleButton button = this.binding.buttonOnThursday;
            if (!Objects.equals(value, button.isChecked())) {
                button.setChecked(value);
            }
        });
        this.viewModel.isOnFriday().observe(this.getViewLifecycleOwner(), value -> {
            ToggleButton button = this.binding.buttonOnFriday;
            if (!Objects.equals(value, button.isChecked())) {
                button.setChecked(value);
            }
        });
        this.viewModel.isOnSaturday().observe(this.getViewLifecycleOwner(), value -> {
            ToggleButton button = this.binding.buttonOnSaturday;
            if (!Objects.equals(value, button.isChecked())) {
                button.setChecked(value);
            }
        });
        this.viewModel.isShared()
                .observe(this.getViewLifecycleOwner(), shared -> this.binding.switchShared.setChecked(shared));

        this.viewModel.setCurrentUser((User) this.requireArguments().getSerializable(ARG_USER));
        this.viewModel.loadHabit((Habit) this.requireArguments().getSerializable(ARG_HABIT));

        this.binding.editTitle.addTextChangedListener(new SimpleTextWatcher(editable -> {
            String newTitle = editable.toString();
            if (this.validateTitle(newTitle)) {
                this.viewModel.setTitle(newTitle);
            }
        }));
        this.binding.editReason.addTextChangedListener(new SimpleTextWatcher(editable -> {
            String newReason = editable.toString();
            if (this.validateReason(newReason)) {
                this.viewModel.setReason(newReason);
            }
        }));
        this.binding.textStartDate.setOnClickListener(this::onEditStartDateClicked);
        this.binding.layoutStartDate.setEndIconOnClickListener(
                (view) -> this.onEditStartDateClicked(this.binding.textStartDate));
        this.binding.buttonOnSunday.setOnCheckedChangeListener(
                (button, checked) -> this.viewModel.setOnSunday(checked));
        this.binding.buttonOnMonday.setOnCheckedChangeListener(
                (button, checked) -> this.viewModel.setOnMonday(checked));
        this.binding.buttonOnTuesday.setOnCheckedChangeListener(
                (button, checked) -> this.viewModel.setOnTuesday(checked));
        this.binding.buttonOnWednesday.setOnCheckedChangeListener(
                (button, checked) -> this.viewModel.setOnWednesday(checked));
        this.binding.buttonOnThursday.setOnCheckedChangeListener(
                (button, checked) -> this.viewModel.setOnThursday(checked));
        this.binding.buttonOnFriday.setOnCheckedChangeListener(
                (button, checked) -> this.viewModel.setOnFriday(checked));
        this.binding.buttonOnSaturday.setOnCheckedChangeListener(
                (button, checked) -> this.viewModel.setOnSaturday(checked));
        this.binding.switchShared.setOnCheckedChangeListener((view, checked) -> this.viewModel.setShared(checked));
        this.binding.buttonSave.setOnClickListener(view -> {
            if (this.validateTitle(retrieveText(this.binding.editTitle)) &&
                this.validateReason(retrieveText(this.binding.editReason)) &&
                this.validateStartDate(retrieveText(this.binding.textStartDate))) {
                this.viewModel.saveHabit();
            }
        });

        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.requireDialog().getWindow().setWindowAnimations(R.style.Animation_MaterialComponents_BottomSheetDialog);
        super.onViewCreated(view, savedInstanceState);
    }

    private void onEditStartDateClicked(View view) {
        assert this.viewModel != null;

        AutoCompleteTextView edit = (AutoCompleteTextView) view;
        Date currentDate = new Date();
        try {
            Date parse = this.dateFormat.parse(edit.getText().toString());
            if (parse != null) {
                currentDate = parse;
            }
        } catch (ParseException ignored) {
        }

        new DatePickerFragment(currentDate, (fragment, date) -> this.viewModel.setStartDate(date)).show(
                this.getChildFragmentManager(), "DatePicker");

        edit.dismissDropDown();
    }

    private boolean validateTitle(String title) {
        assert this.binding != null;
        assert this.viewModel != null;

        if (title == null || title.length() == 0) {
            this.binding.layoutTitle.setErrorEnabled(true);
            this.binding.layoutTitle.setError(this.getString(R.string.error_title_empty));
            return false;
        } else if (title.length() > 20) {
            this.binding.layoutTitle.setErrorEnabled(true);
            this.binding.layoutTitle.setError(this.getString(R.string.error_title_too_long));
            return false;
        } else {
            this.binding.layoutTitle.setErrorEnabled(false);
            this.binding.layoutTitle.setError(null);
            return true;
        }
    }

    private boolean validateReason(String reason) {
        assert this.binding != null;
        assert this.viewModel != null;

        if (reason == null || reason.length() == 0) {
            this.binding.layoutReason.setErrorEnabled(true);
            this.binding.layoutReason.setError(this.getString(R.string.error_reason_empty));
            return false;
        } else if (reason.length() > 30) {
            this.binding.layoutReason.setErrorEnabled(true);
            this.binding.layoutReason.setError(this.getString(R.string.error_reason_too_long));
            return false;
        } else {
            this.binding.layoutReason.setErrorEnabled(false);
            this.binding.layoutReason.setError(null);
            return true;
        }
    }

    private boolean validateStartDate(String date) {
        assert this.binding != null;
        assert this.viewModel != null;

        if (date == null || date.length() == 0) {
            this.binding.layoutStartDate.setErrorEnabled(true);
            this.binding.layoutStartDate.setError(this.getString(R.string.error_start_date_empty));
            return false;
        } else {
            this.binding.layoutStartDate.setErrorEnabled(false);
            this.binding.layoutStartDate.setError(null);
            return true;
        }
    }
}