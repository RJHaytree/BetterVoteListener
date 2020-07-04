package io.github.rjhaytree.bettervotelistener.managers;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import io.github.rjhaytree.bettervotelistener.managers.model.VoteData;
import org.spongepowered.api.entity.living.player.Player;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class VoteManager {
    private BetterVoteListener plugin;
    private HashMap<UUID, VoteData> voteDataCache;

    public VoteManager(BetterVoteListener plugin) {
        this.plugin = plugin;
        this.voteDataCache = new HashMap<>();
    }

    public void loadPlayerToCache(Player player) {
        try {
            if (plugin.getStorage().checkIfPlayerExists(player).get()) {
                VoteData vd = plugin.getStorage().getPlayer(player).get();
                voteDataCache.put(player.getUniqueId(), vd);
            }
            else {
                plugin.getStorage().addPlayer(player);
                VoteData vd = new VoteData(1, LocalDateTime.now());
                voteDataCache.put(player.getUniqueId(), vd);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public VoteData getVoteData(Player player) {
        return voteDataCache.get(player.getUniqueId());
    }

    public VoteData getVoteData(UUID uuid) {
        return voteDataCache.get(uuid);
    }

    public boolean isPlayerInCache(UUID uuid) {
        return voteDataCache.containsKey(uuid);
    }

    public Integer getVoteCount(Player player) {
        int count = 0;

        if (isPlayerInCache(player.getUniqueId())) {
            count = getVoteData(player).getVoteCount();
        }

        return count;
    }
}
