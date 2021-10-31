package ca.cmput301f21t22.nabu.model;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

import ca.cmput301f21t22.nabu.data.Occurrence;

public interface Habit {
    @Nullable
    Boolean getShared();

    void setShared(@Nullable Boolean shared);

    @Nullable
    String getTitle();

    void setTitle(@Nullable String title);

    @Nullable
    String getReason();

    void setReason(@Nullable String reason);

    @Nullable
    Date getStartDate();

    void setStartDate(@Nullable Date startDate);

    @Nullable
    Occurrence getOccurrence();

    void setOccurrence(@Nullable Occurrence occurrence);

    @Nullable
    List<String> getEvents();

    void setEvents(@Nullable List<String> events);
}
