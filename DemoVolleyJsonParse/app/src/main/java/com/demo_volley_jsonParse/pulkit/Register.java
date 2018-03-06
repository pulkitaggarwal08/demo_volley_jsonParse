package com.demo_volley_jsonParse.pulkit;

/**
 * Created by agicon06 on 26/7/17.
 */

public class Register {

    private String id;
    private String name;


    public Register(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
