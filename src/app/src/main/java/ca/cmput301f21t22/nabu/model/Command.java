package ca.cmput301f21t22.nabu.model;

/**
 * An executable command that has side-effects on database data, and may provide an arbitrary return value.
 *
 * @param <TResult>
 */
public interface Command<TResult> {
    /**
     * Execute the command.
     *
     * @return Relevant return information.
     */
    TResult execute();
}
