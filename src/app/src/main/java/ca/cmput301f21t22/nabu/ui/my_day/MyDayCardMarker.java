package ca.cmput301f21t22.nabu.ui.my_day;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ca.cmput301f21t22.nabu.R;

public class MyDayCardMarker {
    @Nullable
    private final String id;
    @Nullable
    private final Boolean complete;
    @NonNull
    private final Icon icon;

    public MyDayCardMarker(@Nullable String id, @Nullable Boolean complete, @NonNull Icon icon) {
        this.id = id;
        this.complete = complete;
        this.icon = icon;
    }

    @Nullable
    public String getId() {
        return this.id;
    }

    @Nullable
    public Boolean isComplete() {
        return this.complete;
    }

    @NonNull
    public Icon getIcon() {
        return this.icon;
    }

    public enum Icon {
        INCOMPLETE(R.drawable.ic_outline_circle_18), COMPLETE(R.drawable.ic_baseline_check_circle_18), FAILED(
                R.drawable.ic_baseline_close_18), NOT_DUE(0);

        private final int res;

        Icon(int res) {
            this.res = res;
        }

        public int getRes() {
            return this.res;
        }
    }
}
