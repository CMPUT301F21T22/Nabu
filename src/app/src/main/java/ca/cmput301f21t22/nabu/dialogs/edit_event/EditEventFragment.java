package ca.cmput301f21t22.nabu.dialogs.edit_event;

import static android.app.Activity.RESULT_OK;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

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

    private ImageView event;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;


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


        event = this.binding.event;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        event.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                choosePicture();
            }

            private void choosePicture() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }

            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
                EditEventFragment.super.onActivityResult(requestCode, resultCode, data);
                if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                    imageUri = data.getData();
                    event.setImageURI(imageUri);
                    uploadPicture();
                }
            }

            private void uploadPicture() {

                final String randomKey = UUID.randomUUID().toString();
                StorageReference riversRef = storageReference.child("images/");

                riversRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //Snackbar.make(EditEventFragment.this.binding., "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(EditEventFragment.this.requireContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


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


