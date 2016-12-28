/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher;

import deincraftlauncher.IO.FileUtils;
import deincraftlauncher.IO.download.DownloadHandler;
import deincraftlauncher.modPacks.ModpackSelector;
import java.io.File;

/**
 *
 * @author Darkp
 */
public class Config {
    
    private static final String libLink = "";
    
    public String DCFolder;
    public String Username;
    public String Password;
    
    private String libVersion = "0.0.0";
    private String libVersionInstalled = "0.0.0";
    
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
        return instance.DCFolder + "Launcher" + File.separator;
    }
    
    public static String getImagesFolder() {
        return Config.getLauncherFolder() + "Images" + File.separator;
    }
    
    public static String getGameFolder() {
        return instance.DCFolder + "Games" + File.separator;
    }
    
    public static String getCacheFolder() {
        File test = new File(getGameFolder() + "Cache" + File.separator);
        if (!test.exists()) {
            test.mkdirs();
        }
        return getGameFolder() + "Cache" + File.separator;
    }
    
    private String getLibFile() {
        return getLauncherFolder() + "libData";
    }

    public String getLibVersion() {
        return libVersion;
    }

    public void setLibVersion(String libVersion) {
        this.libVersion = libVersion;
    }

    public String getLibVersionInstalled() {
        return libVersionInstalled;
    }

    public void setLibVersionInstalled(String libVersionInstalled) {
        this.libVersionInstalled = libVersionInstalled;
    }
    
    public void updateLib() {
        
        libVersionInstalled = readInstalledVersion();
        if (libVersionInstalled == null || libVersionInstalled.equals("")) {
            FileUtils.WriteFile(new File(getLibFile()), "0.0.0");
            updateLibNow();
        }
        if (!libVersionInstalled.equals(libVersion)) {
            updateLibNow();
        }
        
    }
    
    private void updateLibNow() {
        
        DownloadHandler.addItem(getCacheFolder(), libLink);
        
    }
    
    private String readInstalledVersion() {
        return FileUtils.readFile(new File(getLibFile())).get(0);
    }
    
}
