package com.devinhouse.m03w04.library.service;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<String> users;

    public UserService(){
        this.users = new ArrayList<>();
    }

    public String add(String user) throws Exception{
        if(users.contains(user)){
            throw new Exception("User already exists");
        }
        this.users.add(user);
        return user;
    }

    public List<String> getUsers(){
        return users;
    }

}