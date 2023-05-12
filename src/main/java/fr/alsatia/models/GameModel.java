package fr.alsatia.models;

public class GameModel {
    private int id;
    private String mode;
    private boolean ranked;
    private int elo, played, loss, won;
    private String playerUuid;

    public GameModel(){}
    public GameModel(String mode, boolean ranked, int elo, int played, int loss, int won, String playerUuid) {
        this.mode = mode;
        this.ranked = ranked;
        this.elo = elo;
        this.played = played;
        this.loss = loss;
        this.won = won;
        this.playerUuid = playerUuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isRanked() {
        return ranked;
    }

    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    @Override
    public String toString() {
        return "GameModel{" +
                "id=" + id +
                ", mode='" + mode + '\'' +
                ", ranked=" + ranked +
                ", elo=" + elo +
                ", played=" + played +
                ", loss=" + loss +
                ", won=" + won +
                ", playerUuid='" + playerUuid + '\'' +
                '}';
    }
}
