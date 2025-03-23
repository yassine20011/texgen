package com.texgen.controllers;

import com.texgen.model.User;
import com.texgen.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;


public class SignIn {
    private final UserDAO userDAO = new UserDAO();

    @FXML
    public StackPane rootPane;

    public void login(String username, String password) {
        User user = userDAO.findByIdByUsername(username);
        if (user != null && user.checkPassword(password)) {
            user.setLogged(true);
            userDAO.update(user);
        }
    }

    public void logout(String username) {
        User user = userDAO.findByIdByUsername(username);
        if (user != null) {
            user.setLogged(false);
            userDAO.update(user);
        }
    }
}
