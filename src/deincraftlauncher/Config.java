/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher;

import deincraftlauncher.IO.ZIPExtractor;
import deincraftlauncher.IO.download.DownloadHandler;
import deincraftlauncher.IO.download.Downloader;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darkp
 */
public class Config {
    
    private static final String libLink = "https://www.dropbox.com/s/b2cbdpgo4249vq4/General-Cache.zip?dl=1";
    
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
        return instance.DCFolder + "Launcher" + File.separator;
    }
    
    public static String getImagesFolder() {
        return Config.getLauncherFolder() + "Images" + File.separator;
    }
    
    public static String getGameFolder() {
        return instance.DCFolder + "Games" + File.separator;
    }
    
    public static String getCacheFolder() {
        
        return getGameFolder() + "Cache" + File.separator;
    }
    
    public void updateLib() {
        
        System.out.println("checking lib update!");
        
        File test = new File(getCacheFolder());
        if (!test.exists()) {
            System.out.println("Libs not downloaded!");
            test.mkdirs();
            DownloadHandler.addItem(getCacheFolder(), libLink, this::onFinished);
            DownloadHandler.start();
            DownloadHandler.setTitle("Downloading Assets");
        } else {
            System.out.print("cache folder already existing");
        }
        
    }
    
    private void onFinished(Downloader loader) {
        try {
            ZIPExtractor.extractArchive(loader.getTargetFile(), getCacheFolder());
        } catch (Exception ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
