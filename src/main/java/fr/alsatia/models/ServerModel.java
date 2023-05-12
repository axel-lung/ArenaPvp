package fr.alsatia.models;

import java.util.UUID;

public class ServerModel {
    private int id;
    private String servername, mode;
    private Boolean ranked;
    private UUID player_uuid1, player_uuid2;

    public ServerModel() {
    }

    public ServerModel(String servername, String mode, Boolean ranked, UUID player_uuid1, UUID player_uuid2) {
        this.servername = servername;
        this.mode = mode;
        this.ranked = ranked;
        this.player_uuid1 = player_uuid1;
        this.player_uuid2 = player_uuid2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Boolean getRanked() {
        return ranked;
    }

    public void setRanked(Boolean ranked) {
        this.ranked = ranked;
    }

    public UUID getPlayer_uuid1() {
        return player_uuid1;
    }

    public void setPlayer_uuid1(UUID player_uuid1) {
        this.player_uuid1 = player_uuid1;
    }

    public UUID getPlayer_uuid2() {
        return player_uuid2;
    }

    public void setPlayer_uuid2(UUID player_uuid2) {
        this.player_uuid2 = player_uuid2;
    }
}
