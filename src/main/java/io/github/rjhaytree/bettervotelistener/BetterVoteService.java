package io.github.rjhaytree.bettervotelistener;

import io.github.rjhaytree.bettervotelistener.api.VoteService;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class BetterVoteService implements VoteService {
    private BetterVoteListener plugin;

    public BetterVoteService(BetterVoteListener plugin) {
        this.plugin = plugin;
    }

    /**
     * Get a player's total number of votes.
     *
     * @param uuid UUID of the player.
     * @return Number of votes.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public Integer getPlayerVotes(UUID uuid) throws ExecutionException, InterruptedException {
        int votes = 0;

        if (plugin.getVoteManager().isPlayerInCache(uuid)) {
            votes = plugin.getVoteManager().getVoteData(uuid).getVoteCount();
        }
        else if (plugin.getStorage().checkIfPlayerExists(uuid).get()) {
            votes = plugin.getStorage().getOfflinePlayerVotes(uuid).get();
        }

        return votes;
    }

    @Override
    public boolean addVotes(UUID uuid, int votes) {
        return false;
    }

    @Override
    public Integer getTotalStoredVotes() {
        return null;
    }
}
