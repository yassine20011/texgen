package com.texgen.controllers;

import com.texgen.model.User;
import com.texgen.dao.UserDAO;

public class SignUp {

    private final UserDAO userDAO = new UserDAO();

    public void signup(String name, String email, boolean isLogged, String password) {
        User user = new User(name, email, isLogged, password);
        userDAO.save(user);
    }

}
