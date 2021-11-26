package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A data object, holding the data needed to render a card of another user's habits in the My Day view.
 */
public class MyDayUserCard {
    private final User user;
    private List<MyDayCard> userHabits;

    /**
     * Create an instance of MyDayUserCard.
     *
     * @param user       The user associated with the card view.
     * @param userHabits The habits of that user to be displayed, stored in MyDayCards.
     */
    public MyDayUserCard(@NonNull User user, @NonNull ArrayList<MyDayCard> userHabits) {
        this.user = user;
        this.userHabits = userHabits;
    }

    /**
     * @return Whether two objects are structurally equivalent.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        MyDayUserCard that = (MyDayUserCard) o;
        return Objects.equals(this.user, that.user) && Objects.equals(this.userHabits, that.userHabits);
    }

    /**
     * @return The hash code of the object's fields.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.user, this.userHabits);
    }

    /**
     * Return the user associated with the card.
     *
     * @return The User associated with the card.
     */
    public User getUser() {
        return this.user;
    }

    public void setHabits(ArrayList<MyDayCard> userHabits) {
        this.userHabits = userHabits;
    }

    /**
     * Return the email of the user associated with the card.
     *
     * @return The String email of the user associated with the card.
     */
    public String getEmail() {
        return this.user.getEmail();
    }

    /**
     * Return the Id of the user associated with the card.
     *
     * @return The String Id of the user associated with the card.
     */
    public String getUserId() {
        return this.user.getId();
    }

    /**
     * Return the habits associated with the user associated with the card.
     *
     * @return List of the user's habits represented by MyDayCards.
     */
    public List<MyDayCard> getUserHabits() {
        return this.userHabits;
    }
}
