package fr.alsatia.models;

public class Opponents {
    private WaitingModel opponent1, opponent2;
    private GameModel gm;

    public Opponents(WaitingModel opponent1, WaitingModel opponent2, GameModel gm) {
        this.opponent1 = opponent1;
        this.opponent2 = opponent2;
        this.gm = gm;
    }

    public WaitingModel getOpponent1() {
        return opponent1;
    }

    public void setOpponent1(WaitingModel opponent1) {
        this.opponent1 = opponent1;
    }

    public WaitingModel getOpponent2() {
        return opponent2;
    }

    public void setOpponent2(WaitingModel opponent2) {
        this.opponent2 = opponent2;
    }

    public GameModel getGm() {
        return gm;
    }

    public void setGm(GameModel gm) {
        this.gm = gm;
    }
}
