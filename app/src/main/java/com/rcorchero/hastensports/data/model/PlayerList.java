package com.rcorchero.hastensports.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerList {

    @JsonProperty(value = "type")
    private String mType;

    @JsonProperty(value = "title")
    private String mTitle;

    @JsonProperty(value = "players")
    private List<Player> mPlayers;

    public PlayerList() {}

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public void setPlayers(List<Player> mPlayers) {
        this.mPlayers = mPlayers;
    }

}
