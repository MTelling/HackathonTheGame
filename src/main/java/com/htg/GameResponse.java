package com.htg;

public class GameResponse {

    private ChallengeDescription challengeDescription;

    public GameResponse(){};

    public GameResponse(ChallengeDescription status) {
        this.challengeDescription = status;
    }

    public ChallengeDescription getChallengeDescription() {
        return challengeDescription;
    }

    public void setChallengeDescription(ChallengeDescription challengeDescription) {
        this.challengeDescription = challengeDescription;
    }

}
