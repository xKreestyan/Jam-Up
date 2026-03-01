package org.jamup.bean;

import org.jamup.exception.InvalidFieldException;

public class LoginUserBean {

    private String email;
    private String password;

    public void checkAndSetEmail(String email) throws InvalidFieldException {
        if (email == null || !email.contains("@")) {
            throw new InvalidFieldException("email", "must be a valid email address");
        }
        this.email = email.trim();
    }

    public void checkAndSetPassword(String password) throws InvalidFieldException {
        if (password == null || password.isEmpty()) {
            throw new InvalidFieldException("password", "cannot be empty");
        }
        this.password = password;
    }

    public LoginUserBean(String email, String password) throws InvalidFieldException {
        checkAndSetEmail(email);
        checkAndSetPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
