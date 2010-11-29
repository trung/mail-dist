/**
 * 
 */
package org.mdkt.library.security.users;

import java.util.ArrayList;
import java.util.List;


/**
 * @author trung
 *
 */
public interface UserRegistry {

    GaeUser findUser(String userId);

    void registerUser(GaeUser newUser);

    void removeUser(String userId);
    
    void removeUsers(ArrayList<String> userIds);
    
    List<GaeUser> getAllGaeUsers();
    
    /**
     * Update lastSignedIn field
     */
    void updateLastSignedIn(String userId);
    
    /**
     * Return google user id corresponding to the email. User must accept to use the app before.
     * @see acceptUser
     * @param email
     * @return google user id
     */
    String findAcceptedUser(String email);
    
    /**
     * Happens when user accept to user the app and therefore be stored in the temporarily table. 
     * @param userId google userid
     * @param email
     */
    void acceptUser(String userId, String email);
}
