package ca.cmput301f21t22.nabu;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    @NonNull
    public final static String TAG = "MainActivity";

    @Nullable
    private MainViewModel viewModel;
    @Nullable
    private ActivityMainBinding binding;

    @NonNull
    private final List<AuthUI.IdpConfig> signInProviders =
            Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
    @NonNull
    private final ActivityResultLauncher<Intent> signInLauncher =
            this.registerForActivityResult(new FirebaseAuthUIActivityResultContract(),
                                           result -> this.viewModel.onSignIn(result));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.binding.getRoot());

        this.viewModel.getSignedIn().observe(this, signedIn -> {
            if (!signedIn) {
                this.signInLauncher.launch(AuthUI.getInstance()
                                                   .createSignInIntentBuilder()
                                                   .setIsSmartLockEnabled(false)
                                                   .setAvailableProviders(this.signInProviders)
                                                   .build());
            }
        });

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);
        }

        NavHostFragment host = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        NavController controller = Objects.requireNonNull(host).getNavController();
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(this.binding.mainNavView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this, controller, configuration);
        NavigationUI.setupWithNavController(this.binding.mainNavView, controller);
    }
}