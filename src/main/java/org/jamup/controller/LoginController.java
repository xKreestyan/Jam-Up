package org.jamup.controller;

import org.jamup.bean.LoginUserBean;
import org.jamup.dao.interfaces.UserDAO;
import org.jamup.exception.InvalidCredentialsException;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.model.Artist;
import org.jamup.model.VenueManager;
import org.jamup.util.Encryptor;
import org.jamup.util.SessionManager;

public class LoginController {

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param bean the bean containing the user's email and password
     * @throws InvalidCredentialsException if no matching user is found or the password is incorrect
     */
    public void login(LoginUserBean bean) throws InvalidCredentialsException {
        UserDAO userDAO = DAOFactory.getInstance().createUserDAO();
        String hashedPassword = Encryptor.hash(bean.getPassword());

        //search among artists
        Artist artist = userDAO.findArtistByEmail(bean.getEmail());
        if (artist != null && artist.getPassword().equals(hashedPassword)) {
            SessionManager.getInstance().login(artist);
            return;
        }

        //search among managers
        VenueManager manager = userDAO.findManagerByEmail(bean.getEmail());
        if (manager != null && manager.getPassword().equals(hashedPassword)) {
            SessionManager.getInstance().login(manager);
            return;
        }

        //no match found
        throw new InvalidCredentialsException();
    }

}