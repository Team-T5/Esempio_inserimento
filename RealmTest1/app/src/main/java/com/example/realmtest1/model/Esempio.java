package com.example.realmtest1.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Esempio extends RealmObject {

    @PrimaryKey
    private String Nome;

    public Esempio(){}

    public void setNome(String Nome){
        this.Nome = Nome;
    }
}
