package io.github.rjhaytree.bettervotelistener.api;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface VoteService {
    /**
     * Get a player's total vote count.
     * @param uuid UUID of the player.
     * @return Integer. Number of votes.
     */
    Integer getPlayerVotes(UUID uuid) throws ExecutionException, InterruptedException;

    /**
     * Add a vote to a user's vote count. Will trigger vote rewards.
     * @param uuid UUID of the player.
     * @param votes Number of votes to add.
     * @return Boolean. Whether the task was successful.
     */
    boolean addVotes(UUID uuid, int votes);

    /**
     * Get the total number of stored votes across all players.
     * @return Integer. The number of total votes.
     */
    Integer getTotalStoredVotes();
}
