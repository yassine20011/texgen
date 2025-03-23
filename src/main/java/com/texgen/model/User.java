package com.texgen.model;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private final String name;

    @Column(unique = true)
    private final String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(name = "is_logged", nullable = false)
    private boolean isLogged;

    public User(String name, String email, boolean isLogged, String password) {
        this.name = name;
        this.email = email;
        this.isLogged = isLogged;
        this.passwordHash = hashPassword(password);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
    }

    public void setPassword(String rawPassword) {
        this.passwordHash = hashPassword(rawPassword);
    }

    public boolean checkPassword(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.passwordHash);
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

}
