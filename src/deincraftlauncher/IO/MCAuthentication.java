/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.IO;

import net.chris54721.openmcauthenticator.OpenMCAuthenticator;
import net.chris54721.openmcauthenticator.exceptions.AuthenticationUnavailableException;
import net.chris54721.openmcauthenticator.exceptions.InvalidCredentialsException;
import net.chris54721.openmcauthenticator.exceptions.RequestException;
import net.chris54721.openmcauthenticator.responses.AuthenticationResponse;

/**
 *
 * @author Darkp
 */
public class MCAuthentication {
    
    public static boolean isValidLogin(String Username, String Password) {
        try {
            AuthenticationResponse authResponse = OpenMCAuthenticator.authenticate(Username, Password);
            String authToken = authResponse.getAccessToken();
        } catch (RequestException | AuthenticationUnavailableException e) {
            if (e instanceof AuthenticationUnavailableException) {
                System.err.println("Minecraft servers unavaible");
                return false;
            }
            if (e instanceof InvalidCredentialsException) {
                System.err.println("invalid username or password");
                return false;
              
            }
        }
        
        return true;
    }
    
}
