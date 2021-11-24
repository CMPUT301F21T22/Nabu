package ca.cmput301f21t22.nabu.model.commands;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.model.Command;

/**
 * Command providing uploading for a local photo to Firebase storage.
 */
public class UploadLocalPhotoCommand implements Command<CompletableFuture<String>> {
    @NonNull
    public final static String TAG = "UploadLocalPhotoCommand";

    @NonNull
    private final FirebaseStorage storage;

    @NonNull
    private final Uri localPhotoPath;

    /**
     * Create an instance of UploadLocalPhotoCommand.
     *
     * @param localPhotoPath The local URI of the photo to upload.
     */
    public UploadLocalPhotoCommand(@NonNull Uri localPhotoPath) {
        this.storage = FirebaseStorage.getInstance();

        this.localPhotoPath = localPhotoPath;
    }

    /**
     * Execute the command.
     *
     * @return A string containing the URL the newly-uploaded file may be downloaded from, or null if the upload failed.
     */
    @Override
    public CompletableFuture<String> execute() {
        StorageReference fileRef = this.storage.getReference().child(this.localPhotoPath.getLastPathSegment());
        CompletableFuture<String> future = new CompletableFuture<>();
        fileRef.putFile(this.localPhotoPath).continueWithTask(task -> {
            if (!task.isSuccessful() && task.getException() != null) {
                Log.e(TAG, "Failed to upload file with name: " + this.localPhotoPath.getLastPathSegment());
                future.complete(null);
            }

            return fileRef.getDownloadUrl();
        }).addOnSuccessListener(uri -> {
            future.complete(uri.toString());
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to retrieve download URL for file");
            future.complete(null);
        });
        return future;
    }
}
