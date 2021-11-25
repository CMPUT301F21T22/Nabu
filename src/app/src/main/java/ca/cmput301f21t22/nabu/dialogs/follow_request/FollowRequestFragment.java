package ca.cmput301f21t22.nabu.dialogs.follow_request;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.Calendar;
import java.util.Date;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.FragmentEditHabitBinding;
import ca.cmput301f21t22.nabu.databinding.LayoutNewFollowRequestBinding;
import ca.cmput301f21t22.nabu.ui.my_day.MyDayViewModel;
import ca.cmput301f21t22.nabu.ui.social.SocialViewModel;

public class FollowRequestFragment extends DialogFragment  {
/*
    //@SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = this.requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_follow_request, null);

        return new AlertDialog.Builder(this.getActivity()).setView(view).create();
    }
}

public class AddMedicineFragment extends DialogFragment {

 */
    private SocialViewModel viewModel;
    private EditText requestedEmail;
    private LayoutNewFollowRequestBinding binding;

    public FollowRequestFragment(SocialViewModel myDayViewModel) {
        this.viewModel = myDayViewModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //View view = LayoutInflater.from(getActivity())
                //.inflate(R.layout.layout_new_follow_request, null);
        this.binding = FragmentEditHabitBinding.inflate(inflater, container, false);
        requestedEmail = this.binding.emailAddressInput;

        //Create the 'pop-up' window
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(this.binding.getRoot())
                .setNegativeButton("CANCEL", null)

                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String email = requestedEmail.getText().toString();
                        if (validateEmail(email)) {
                            viewModel.onEmailEntered(email);
                        }
                    }
                }).create();
    }

    private boolean validateEmail(String email) {
        if (email == null || email.length() == 0) {
            this.binding.layoutTitle.setErrorEnabled(true);
            this.binding.layoutTitle.setError(this.getString(R.string.error_title_too_long));
        }
    }

}
