/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.IO.download;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Darkp
 */
public class FTPConnection {
    
    private static final FTPClient client = new FTPClient();
    
    private static boolean connected = false;
    
    public static FTPClient getClient() {
        return client;
    }
    
    public static void changeDir(String dir) {
        try {
            client.changeWorkingDirectory(dir);
        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void connect() {
        
        if (connected) {
            return;
        }
        
        try {
            client.connect(FTPSync.getFtpServer());
            client.enterLocalPassiveMode();
            client.login(FTPSync.getFtpUsername(), FTPSync.getFtpPassword());
            client.setFileType(FTP.BINARY_FILE_TYPE);
            
        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        connected = true;
    }
    
    public static void disconnect() {
        
        try {
            client.logout();
            client.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        connected = false;
        
    }
    
    public boolean isConnected() {
        return connected;
    }
    
}
