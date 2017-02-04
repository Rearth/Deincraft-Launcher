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
import net.chris54721.openmcauthenticator.responses.RefreshResponse;

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
    
    public static String getToken(String Username, String Password) {
        
        try {
            AuthenticationResponse authResponse = OpenMCAuthenticator.authenticate(Username, Password);
            RefreshResponse refreshResponse = OpenMCAuthenticator.refresh(authResponse.getAccessToken(), authResponse.getClientToken());
            String authToken = refreshResponse.getAccessToken();
            
            System.out.println("Mc Authentication done! + token=" + authToken);
            System.out.println("is valid Token:" + OpenMCAuthenticator.validate(authToken));
            return authToken;
            
        } catch (RequestException | AuthenticationUnavailableException e) {
            if (e instanceof AuthenticationUnavailableException) {
                System.err.println("Minecraft servers unavaible");
                return "";
            }
            if (e instanceof InvalidCredentialsException) {
                System.err.println("invalid username or password");
                return "";
              
            }
        }
        return "";
    }
    
    public static String getUUID(String Username, String Password) {
        
        try {
            AuthenticationResponse authResponse = OpenMCAuthenticator.authenticate(Username, Password);
            RefreshResponse refreshResponse = OpenMCAuthenticator.refresh(authResponse.getAccessToken(), authResponse.getClientToken());
            String UUID = authResponse.getSelectedProfile().getUUID().toString();
            
            System.out.println("Mc Authentication done! + UUID=" + UUID);
            System.out.println("is valid Token:" + OpenMCAuthenticator.validate(UUID));
            return UUID;
            
        } catch (RequestException | AuthenticationUnavailableException e) {
            if (e instanceof AuthenticationUnavailableException) {
                System.err.println("Minecraft servers unavaible");
                return "";
            }
            if (e instanceof InvalidCredentialsException) {
                System.err.println("invalid username or password");
                return "";
              
            }
        }
        return "";
    }
    
}
