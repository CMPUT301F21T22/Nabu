package ca.cmput301f21t22.nabu;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @NonNull
    private final static String TAG = "ca.cmput301f21t22.nabu.MainActivity";
    @Nullable
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.binding.getRoot());

        NavHostFragment host = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        if (host == null) {
            Log.e(TAG, "Could not retrieve NavHostFragment");
            return;
        }

        NavController controller = host.getNavController();
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(this.binding.mainNavView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this, controller, configuration);
        NavigationUI.setupWithNavController(this.binding.mainNavView, controller);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
}