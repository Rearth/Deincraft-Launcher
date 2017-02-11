/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.modPacks;

import deincraftlauncher.Config;
import deincraftlauncher.IO.FileUtils;
import deincraftlauncher.IO.ZIPExtractor;
import deincraftlauncher.IO.download.DownloadHandler;
import deincraftlauncher.IO.download.Downloader;
import deincraftlauncher.IO.download.FTPSync;
import deincraftlauncher.designElements.ModpackView;
import deincraftlauncher.designElements.PackViewHandler;
import deincraftlauncher.start.StartMinecraft;
import static deincraftlauncher.start.StartMinecraft.hasCaches;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import query.MCQuery;
import query.QueryResponse;

/**
 *
 * @author Darkp
 */
public class Modpack {
    
    //works only for 1.7.10
    private static final String CacheLink = "https://onedrive.live.com/download?cid=829AE01C48100392&resid=829AE01C48100392%21121&authkey=AKFExpNZ54gYQLc";

    private final String Name;
    private final Image image;
    private final String path;
    
    private int playersOnline = 0;
    private int maxPlayers = 0;
    private boolean installed = false;
    private boolean updated = false;
    private boolean serverOnline = false;
    private boolean WIP = false;
    private String forgeVersion;
    private MCQuery mcQuery;
    private String serverIP;
    private int serverPort;
    
    //Server loaded things
    private String InfoFileLink;
    private String ModsVersion = "0.0.0";
    private String ModsVersionInstalled = "0.0.0";
    private String ModsLink;
    private String ConfigVersion = "0.0.0";
    private String ConfigVersionInstalled = "0.0.0";
    private String ConfigLink;
    private String MainVersion = "0.0.0";
    private String MainVersionInstalled = "0.0.0";
    private String MainLink;
    private String News = "Nicht verfügbar";
    private String MCVersion = "";
    
    public Modpack(String Name, Image image, Color color) {
        this.Name = Name;
        this.image = image;
        path = Config.getGameFolder() + this.Name + File.separator;
        
        
        loadFromConfig();
        
        //view = new ModpackView(this);
        
        
    }
    
    public void select() {
        ModpackSelector.getInstance().selectedPack = this;
        System.out.println("Selected Modpack: " + this.Name);
        showUp();
    }
    
    private void showUp() {
        PackViewHandler.showPack(this);
        
        if (WIP) {
            PackViewHandler.setStartLocked("Coming Soon");
        } else {
            PackViewHandler.setStartUnLocked("Spielen");
        }
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateServerChecker();
            }
        }, 100);
        
    }
    
    public String getState() {
        if (updated) {
            return "Aktuelle Version installiert";
        } else if (installed){
            return "Update Verfügbar!";
        } else {
            return "Nicht Installiert";
        }
    }
    
    public String getServerState() {
        if (serverOnline) {
            return "Online";
        } else {
            return "Offline";
        }
    }
    
    public boolean updateAvaible() {
        
        System.out.println(MainVersion);
        System.out.println(ModsVersion);
        System.out.println(ConfigVersion);
        System.out.println(ConfigVersionInstalled);
        System.out.println(MainVersionInstalled);
        System.out.println(ModsVersionInstalled);
        
        return !(MainVersion.equals(MainVersionInstalled) && ModsVersion.equals(ModsVersionInstalled) && ConfigVersion.equals(ConfigVersionInstalled));
    }
    
    private void loadFromConfig() {
        
        File folder = new File(Config.getLauncherFolder() + this.Name + File.separator);
        File configFile = new File(Config.getLauncherFolder() + this.Name + File.separator + "config");
        if (!folder.exists()) {
            System.out.println("first time opening modpack config!");
            try {
                folder.mkdirs();
                configFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Modpack.class.getName()).log(Level.SEVERE, null, ex);
            }
            saveConfig();
            return;
        }
        
        ArrayList<String> fileText = FileUtils.readFile(configFile);
        MainVersionInstalled = fileText.get(0);
        ModsVersionInstalled = fileText.get(1);
        ConfigVersionInstalled = fileText.get(2);
        
    }
    
    public void saveConfig() {
        
        if (MainVersionInstalled == null) {
            MainVersionInstalled = MainVersion;
            ModsVersionInstalled = ModsVersion;
            ConfigVersionInstalled = ConfigVersion;
        }
        
        ArrayList<String> write = new ArrayList<>();
        write.add(MainVersionInstalled);
        write.add(ModsVersionInstalled);
        write.add(ConfigVersionInstalled);
        FileUtils.WriteFile(new File(Config.getLauncherFolder() + this.Name + File.separator + "config"), write);
        
    }
    
    public void handleStart() {
        System.out.println("Handling Start action for pack " + Name + " Update: " + updateAvaible());
        
        if (updateAvaible()) {
            PackViewHandler.setStartLocked("Downloade");
            System.out.println("Downloading update");
            if (!ModsVersion.equals(ModsVersionInstalled)) {
                updateMods();
                //DownloadHandler.addItem(path, ModsLink, this::onDownloaderFinish);
            }
            if (!MainVersion.equals(MainVersionInstalled)) {
                DownloadHandler.addItem(path, MainLink, this::onDownloaderFinish);
                DownloadHandler.setTitle("Downloade " + Name);
                DownloadHandler.start();
            }
            if (!ConfigVersion.equals(ConfigVersionInstalled)) {
                DownloadHandler.addItem(path, ConfigLink, this::onDownloaderFinish);
                DownloadHandler.setTitle("Downloade " + Name);
                DownloadHandler.start();
            }
            if (!hasCaches(this)) {
                DownloadHandler.addItem(this.getCaches(), CacheLink, this::onCachesFinish);
                DownloadHandler.setTitle("Downloade " + Name);
                DownloadHandler.start();
            }
        } else {
            startPack();
        }
        
    }
    
    public void onModsFinished() {
        
        ModsVersionInstalled = ModsVersion;
        saveConfig();
        
        Platform.runLater(() -> {
            PackViewHandler.setStartLoading(true);
        });
                
        if (!updateAvaible()) {
            System.out.println("Finished all files for this pack, enabling start button");
            Platform.runLater(() -> {
                PackViewHandler.setStartLoading(false);
                PackViewHandler.setStartUnLocked("Start");      
            });
        }
    }
    
    private void onCachesFinish(Downloader loader) {
        System.out.println("Finished Downloading Caches");
        
        try {
            ZIPExtractor.extractArchive(loader.getTargetFile(), this.getCaches());
        } catch (Exception ex) {
            Logger.getLogger(Modpack.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!updateAvaible()) {
            System.out.println("Finished all files for this pack, enabling start button");
            Platform.runLater(() -> {
                PackViewHandler.setStartUnLocked("Start");      
            });
        }
    }
    
    private void onDownloaderFinish(Downloader loader) {
        
        System.out.println("Finished Downloading game data file: " + loader.getTargetFile());
        
        try {
            ZIPExtractor.extractArchive(loader.getTargetFile(), path);
        } catch (Exception ex) {
            Logger.getLogger(Modpack.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (loader.getTargetFile().toLowerCase().contains("main")) {
            MainVersionInstalled = MainVersion;
        }
        if (loader.getTargetFile().toLowerCase().contains("mods")) {
            ModsVersionInstalled = ModsVersion;
        }
        if (loader.getTargetFile().toLowerCase().contains("config")) {
            ConfigVersionInstalled = ConfigVersion;
        }
        
        saveConfig();
        
        if (!updateAvaible()) {
            System.out.println("Finished all files for this pack, enabling start button");
            Platform.runLater(() -> {
                PackViewHandler.setStartUnLocked("Start");      
            });
        }
        
    }
    
    private void startPack() {
        
        final Modpack thispack = this;
        
        PackViewHandler.setStartLoading(true);
        
        Thread thread = new Thread(){
            @Override
            public void run(){
                System.out.println("Minecraft start thread running");
                StartMinecraft.start(thispack);
            }
        };

        thread.start();
    }
    
    private void updateMods() {
        
        System.out.println("updating mods from ftp server");
        
        final String FTPDir = "/upload/" + this.getName() + "/mods";
        
        FTPSync synctask = new FTPSync(this.getPath() + "mods" + File.separator, FTPDir, this);
        
    }
    
    public boolean isWIP() {
        System.out.println("WIP state for " + Name + "=" + WIP);
        return WIP;
    }
    
    public void setWIP(boolean WIP) {
        this.WIP = WIP;
    }
    
    public String getInfoFileLink() {
        return InfoFileLink;
    }
    
    public void setInfoFileLink(String InfoFileLink) {
        this.InfoFileLink = InfoFileLink;
    }
    
    public String getPath() {
        
        File pathF = new File(path);
        if (!pathF.exists()) {
            pathF.mkdirs();
        }
        
        return path;
    }
    
    public void setServer(String IP, int port) {
        this.serverIP = IP;
        this.serverPort = port;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter/setter">
    
    public String getModsVersion() {
        return ModsVersion;
    }
    
    public void setModsVersion(String ModsVersion) {
        this.ModsVersion = ModsVersion;
    }
    
    public String getModsLink() {
        return ModsLink;
    }
    
    public void setModsLink(String ModsLink) {
        this.ModsLink = ModsLink;
    }
    
    public String getConfigVersion() {
        return ConfigVersion;
    }
    
    public void setConfigVersion(String ConfigVersion) {
        this.ConfigVersion = ConfigVersion;
    }
    
    public String getConfigLink() {
        return ConfigLink;
    }
    
    public void setConfigLink(String ConfigLink) {
        this.ConfigLink = ConfigLink;
    }
    
    public String getMainVersion() {
        return MainVersion;
    }
    
    public void setMainVersion(String MainVersion) {
        this.MainVersion = MainVersion;
    }
    
    public String getMainLink() {
        return MainLink;
    }
    
    public void setMainLink(String MainLink) {
        this.MainLink = MainLink;
    }
    
    public int getPlayersOnline() {
        return playersOnline;
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public boolean isInstalled() {
        return installed;
    }
    
    public boolean isUpdated() {
        return updated;
    }
    
    public String getNews() {
        return News;
    }
    
    public void setNews(String News) {
        this.News = News;
    }
    
    public String getName() {
        return Name;
    }
    
    public String getVersion() {
        return MainVersionInstalled;
    }
    
    public Image getImage() {
        return image;
    }
    
    public String getModsVersionInstalled() {
        return ModsVersionInstalled;
    }
    
    public void setModsVersionInstalled(String ModsVersionInstalled) {
        this.ModsVersionInstalled = ModsVersionInstalled;
    }
    
    public String getConfigVersionInstalled() {
        return ConfigVersionInstalled;
    }
    
    public void setConfigVersionInstalled(String ConfigVersionInstalled) {
        this.ConfigVersionInstalled = ConfigVersionInstalled;
    }
    
    public String getMainVersionInstalled() {
        return MainVersionInstalled;
    }
    
    public void setMainVersionInstalled(String MainVersionInstalled) {
        this.MainVersionInstalled = MainVersionInstalled;
    }
    
    public String getForgeVersion() {
        return forgeVersion;
    }
    
    public void setForgeVersion(String forgeVersion) {
        this.forgeVersion = forgeVersion;
    }
    
//</editor-fold>
    
    private void updateServerChecker() {
        
        if (serverIP == null || serverPort == 0) {
            PackViewHandler.updateStats(this);
            return;
        }
        
        System.out.println("Starting server query");
        try {
            mcQuery = new MCQuery(serverIP, serverPort); //"46.4.75.39"; 25575
            QueryResponse response = mcQuery.basicStat();
            playersOnline = response.getOnlinePlayers();
            maxPlayers = response.getMaxPlayers();
            serverOnline = true;
            System.out.println(response);
        } catch (Exception ex) {
            System.err.println("error contacting server");
            playersOnline = 0;
            maxPlayers = 0;
            serverOnline = false;
        }
        
        Platform.runLater(() -> {
                PackViewHandler.updateStats(this);
        });
        
        Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateServerChecker();
                }
            }, 15000);
        
    }
    
    public String getCaches() {
        return this.getPath() + "Cache" + File.separator;
    }

    public String getMCVersion() {
        return MCVersion;
    }

    public void setMCVersion(String MCVersion) {
        this.MCVersion = MCVersion;
    }
    
    public GameType getGameType() {
        
        if (MCVersion.contains("1.7.10")) {
            return GameType.V1_7_10;
        }
        
        return GameType.V1_8_HIGHER;
    }

    public void updateInfo() {
        if (ModpackSelector.getInstance().selectedPack.equals(this)) {
            PackViewHandler.setPatchNotes(News);
            if (updateAvaible()) {
                PackViewHandler.setStartText("Aktualisieren");
            }
        }
    }
}
