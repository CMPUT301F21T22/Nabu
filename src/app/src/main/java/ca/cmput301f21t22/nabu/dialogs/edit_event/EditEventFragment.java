package ca.cmput301f21t22.nabu.dialogs.edit_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.databinding.FragmentEditEventBinding;
import ca.cmput301f21t22.nabu.dialogs.date_picker.DatePickerFragment;
import ca.cmput301f21t22.nabu.ui.SimpleTextWatcher;

public class EditEventFragment extends DialogFragment {
    @NonNull
    public final static String TAG = "EditEventFragment";

    public final static String ARG_EVENT = "Event";

    @NonNull
    private final DateFormat dateFormat;
    @Nullable
    private EditEventViewModel viewModel;
    @Nullable
    private FragmentEditEventBinding binding;

    private EditEventFragment() {
        this.dateFormat = DateFormat.getDateInstance();
    }

    public static EditEventFragment newInstance(@NonNull Event event) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
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
        this.viewModel = new ViewModelProvider(this).get(EditEventViewModel.class);
        this.binding = FragmentEditEventBinding.inflate(inflater, container, false);

        this.binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);
        this.binding.toolbar.setNavigationOnClickListener(view -> this.dismiss());
        this.binding.toolbar.setTitle(R.string.title_edit_event);

        this.viewModel.isSaved().observe(this.getViewLifecycleOwner(), saved -> {
            if (saved) {
                this.binding.buttonSave.hide();
            } else {
                this.binding.buttonSave.show();
            }
        });

        this.viewModel.getDate().observe(this.getViewLifecycleOwner(), date -> {
            String dateString = this.dateFormat.format(date);
            this.binding.textDate.setText(dateString);
        });

        this.viewModel.getComment().observe(this.getViewLifecycleOwner(), comment -> {
            EditText edit = this.binding.editTextComment;
            if (!Objects.equals(comment, edit.getText().toString())) {
                edit.setText(comment);
            }
        });

        this.viewModel.getPhotoPath().observe(this.getViewLifecycleOwner(), photoPath -> {
            // TODO: Display image from path.
        });

        this.viewModel.getLocation().observe(this.getViewLifecycleOwner(), location -> {
            // TODO: Display location.
        });

        this.viewModel.loadEvent((Event) this.requireArguments().getSerializable(ARG_EVENT));

        this.binding.textDate.setOnClickListener(this::onEditDateClicked);
        this.binding.layoutDate.setEndIconOnClickListener((view) -> this.onEditDateClicked(this.binding.textDate));

        this.binding.editTextComment.addTextChangedListener(new SimpleTextWatcher(editable -> {
            String newComment = editable.toString();
            if (newComment.length() > 20) {
                this.binding.layoutComment.setErrorEnabled(true);
                this.binding.layoutComment.setError(this.getString(R.string.error_comment_too_long));
                this.viewModel.setSaveable(false);
            } else {
                this.binding.layoutComment.setErrorEnabled(false);
                this.binding.layoutComment.setError(null);
                this.viewModel.setComment(newComment);
                this.viewModel.setSaveable(true);
            }
        }));

        this.binding.buttonSave.setOnClickListener(view -> this.viewModel.saveEvent());

        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.requireDialog().getWindow().setWindowAnimations(R.style.Animation_MaterialComponents_BottomSheetDialog);
        super.onViewCreated(view, savedInstanceState);
    }

    private void onEditDateClicked(View view) {
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

        new DatePickerFragment(currentDate, (fragment, date) -> this.viewModel.setDate(date)).show(
                this.getChildFragmentManager(), "DatePicker");

        edit.dismissDropDown();
    }
}
