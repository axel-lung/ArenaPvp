package fr.alsatia.models;

import java.util.UUID;

public class WaitingModel {
    private Integer id;
    private String mode;
    private Boolean ranked;
    private UUID player_uuid;

    public WaitingModel() {
    }
    public WaitingModel(String mode, Boolean ranked, UUID player_uuid) {
        this.mode = mode;
        this.ranked = ranked;
        this.player_uuid = player_uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UUID getPlayer_uuid() {
        return player_uuid;
    }

    public void setPlayer_uuid(UUID player_uuid) {
        this.player_uuid = player_uuid;
    }
}
