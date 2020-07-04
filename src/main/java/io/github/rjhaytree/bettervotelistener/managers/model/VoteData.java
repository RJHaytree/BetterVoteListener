package io.github.rjhaytree.bettervotelistener.managers.model;

import java.time.LocalDateTime;

public class VoteData {
    private int voteCount;
    private LocalDateTime lastVote;

    public VoteData(int voteCount, LocalDateTime lastVote) {
        this.voteCount = voteCount;
        this.lastVote = lastVote;
    }

    public int getVoteCount() {
        return this.voteCount;
    }

    public LocalDateTime getLastVoteDate() {
        return this.lastVote;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setLastVoteDate(LocalDateTime lastVote) {
        this.lastVote = lastVote;
    }

    public void addVote() {
        this.voteCount++;
    }
}
