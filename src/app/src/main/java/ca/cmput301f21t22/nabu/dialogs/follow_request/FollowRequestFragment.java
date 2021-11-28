package ca.cmput301f21t22.nabu.dialogs.follow_request;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.DialogNewFollowRequestBinding;

public class FollowRequestFragment extends DialogFragment {
    @NonNull
    private final Callback callback;
    @Nullable
    private DialogNewFollowRequestBinding binding;

    public FollowRequestFragment(@NonNull Callback callback) {
        this.callback = callback;
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
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(this, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = this.requireActivity().getLayoutInflater();
        this.binding = DialogNewFollowRequestBinding.inflate(inflater);

        AlertDialog dialog = new AlertDialog.Builder(this.getActivity()).setTitle(R.string.title_send_follow_request)
                .setView(this.binding.getRoot())
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.button_add, null)
                .show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String email = retrieveText(this.binding.editEmail);
            if (this.validateEmail(email)) {
                this.callback.onTargetEmail(email);
                this.dismiss();
            }
        });
        return dialog;
    }

    private boolean validateEmail(String email) {
        assert this.binding != null;

        if (email == null || email.equals("")) {
            this.binding.layoutEmail.setErrorEnabled(true);
            this.binding.layoutEmail.setError(this.getString(R.string.error_email_empty));
            return false;
        } else {
            this.binding.layoutEmail.setErrorEnabled(false);
            this.binding.layoutEmail.setError(null);
            return true;
        }
    }

    public interface Callback {
        void onTargetEmail(String email);
    }
}
