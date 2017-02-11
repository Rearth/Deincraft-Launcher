/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.IO;

import deincraftlauncher.modPacks.settings;
import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public static void setLegacyName(String Username, String PW) {
        try {
            Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
            AuthResponse rep = authenticator.authenticate(AuthAgent.MINECRAFT, Username, PW, "");
            AuthInfos authInfos = new AuthInfos(rep.getSelectedProfile().getName(), rep.getAccessToken(), rep.getSelectedProfile().getId());
            System.out.println("legacyName=" + authInfos.getUsername());
            
            settings.getInstance().NickName = authInfos.getUsername();
        } catch (AuthenticationException ex) {
            Logger.getLogger(MCAuthentication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
