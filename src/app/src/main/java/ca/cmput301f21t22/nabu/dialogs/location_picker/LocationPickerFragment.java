package ca.cmput301f21t22.nabu.dialogs.location_picker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.cmput301f21t22.nabu.R;

public class LocationPickerFragment extends DialogFragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    @Nullable
    private SupportMapFragment fragment;
    @NonNull
    private final Callback callback;
    @NonNull
    private final ActivityResultLauncher<String> launcher;
    @Nullable
    private LatLng location;
    @Nullable
    private GoogleMap map;

    public LocationPickerFragment(@NonNull Callback callback) {
        this.callback = callback;
        this.launcher = this.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (!result) {
                this.dismiss();
            }
        });
    }

    public LocationPickerFragment(@NonNull LatLng initial, @NonNull Callback callback) {
        this.location = initial;
        this.callback = callback;
        this.launcher = this.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (!result) {
                this.dismiss();
            }
        });
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = this.requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_location_picker, null);

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.requireActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return new AlertDialog.Builder(this.getActivity()).setView(view).create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.requireActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            this.requireActivity().getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        this.map.setOnMarkerDragListener(this);
        this.updateMap();

        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            this.launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        FusedLocationProviderClient locationClient =
                LocationServices.getFusedLocationProviderClient(this.requireContext());
        locationClient.getLastLocation().addOnSuccessListener(location -> {
            if (this.location == null) {
                this.location = new LatLng(location.getLatitude(), location.getLongitude());
                this.updateMap();
            }
        });
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        this.callback.onLocationSelected(this, marker.getPosition());
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
    }

    private void updateMap() {
        if (this.map == null || this.location == null) {
            return;
        }

        LatLng position = new LatLng(this.location.latitude, this.location.longitude);
        this.map.clear();
        this.map.moveCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(position).zoom(14).build()));
        this.map.addMarker(new MarkerOptions().position(position).draggable(true));
        this.callback.onLocationSelected(this, position);
    }

    public interface Callback {
        void onLocationSelected(LocationPickerFragment dialog, LatLng location);
    }
}
