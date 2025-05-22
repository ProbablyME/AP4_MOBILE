package com.example.ap4.Model;

public class PlayerModel {
    private int    id;
    private String name;
    private int    avatarResId;  // référence au drawable

    public PlayerModel(int id, String name, int avatarResId) {
        this.id          = id;
        this.name        = name;
        this.avatarResId = avatarResId;
    }

    // getters
    public int getId()           { return id; }
    public String getName()      { return name; }
    public int getAvatarResId()  { return avatarResId; }
}
