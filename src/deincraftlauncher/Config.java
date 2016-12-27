/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher;

import java.io.File;

/**
 *
 * @author Darkp
 */
public class Config {
    
    public String DCFolder;
    public String Username;
    public String Password;
    
    private Config() {};
    
    private static Config instance = new Config();
    
    public static Config config() {
        return instance;
    }

    @Override
    public String toString() {
        return "Config{" + "DCFolder=" + DCFolder + '}';
    }
    
    public static String getLauncherFolder() {
        return instance.DCFolder + File.separator + "Launcher" + File.separator;
    }
    
    public static String getImagesFolder() {
        return Config.getLauncherFolder() + File.separator + "Images" + File.separator;
    }
    
    public static String getGameFolder() {
        return instance.DCFolder + File.separator + "Games" + File.separator;
    }
    
}
