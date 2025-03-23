package com.texgen.dao;


import com.texgen.model.User;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO() {
        super(User.class);
    }
}