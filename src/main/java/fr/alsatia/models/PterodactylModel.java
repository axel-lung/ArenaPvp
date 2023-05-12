package fr.alsatia.models;

import java.util.List;

public class PterodactylModel {
    private String object;
    private List<Server> data;


    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Server> getData() {
        return data;
    }

    public void setData(List<Server> data) {
        this.data = data;
    }
}

