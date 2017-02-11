/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.IO.download;

import deincraftlauncher.FXMLSheetController;
import deincraftlauncher.IO.download.Downloader.Function;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;

/**
 *
 * @author Darkp
 */
public class DownloadHandler {
    
    private static final DownloadHandler instance = new DownloadHandler();
    public static int downloaderHeight;
    
    private final ArrayList<Downloader> prepared = new ArrayList<>();
    private final ArrayList<Downloader> done = new ArrayList<>();
    private final ArrayList<FTPDownloader> preparing = new ArrayList<>();
    private final ArrayList<Node> Nodes = new ArrayList<>();
    
    private static String titleName = "Downloading";
    private static int numOfElems = 0;
    private static float totalSize = 0;
    private static float totalDone = 0;
    private static int doneNum = 0;
    private static boolean visible = false;
    private static boolean active = false;
    private static boolean blocked = false;
    private static boolean startBlocked = false;
    
    public static DownloadHandler getInstance() {
        return instance;
    }
    
    private DownloadHandler() {
        
    }
    
    public static void addItem(String targetPath, String Link) {
        instance.addItemP(targetPath, Link);
    }
    
    public static void addItem(String targetPath, String Link, Function func) {
        instance.addItemP(targetPath, Link, func);
    }
    
    public static void addFTPItem(String name, String ftpfolder, String folder, Function func) {
        instance.addFTPItemP(name, ftpfolder, folder, func);
    }
    
    public static void setBlocked(boolean isblocked) {
        System.out.println("downloader blocked=" + isblocked);
        blocked = isblocked;
    }
    
    private void addItemP(String targetPath, String Link) {
        
        if (blocked) {
            System.out.println("blocking download begin");
            return;
        }
        
        
        for (int i = 0; i < preparing.size(); i++) {
            try {
                preparing.get(i).getPrepareThread().join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Downloader elem = new Downloader(Link, targetPath);
        System.out.println("download");
        elem.prepare();
        elem.setOnFinished(this::downloadFinished);
        elem.setOnUpdate(this::onUpdate);
        if (active) {
            addWhileActive(elem);
        }
        instance.prepared.add(elem);
         
    }
    
    private void addItemP(String targetPath, String Link, Function func) {
        
        if (blocked) {
            System.out.println("blocking download begin");
            return;
        }
        
        
        for (int i = 0; i < preparing.size(); i++) {
            try {
                preparing.get(i).getPrepareThread().join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Downloader elem = new Downloader(Link, targetPath);
        //System.out.println("added handled download, link=" + Link);
        elem.prepare();
        elem.setOnFinished(this::downloadFinished);
        elem.setOnFinishedB(func);
        elem.setOnUpdate(this::onUpdate);
        
        if (active) {
            addWhileActive(elem);
        }
        instance.prepared.add(elem);
         
    }
    
    private void addFTPItemP(String name, String ftpfolder, String folder, Function func) {
        
        if (blocked) {
            System.out.println("blocking download begin");
            return;
        }
        
        FTPConnection.connect();
        
        FTPDownloader elem = new FTPDownloader(name, ftpfolder, folder);
        //System.out.println("added handled download, link=" + Link);
        
        for (int i = 0; i < preparing.size(); i++) {
            try {
                preparing.get(i).getPrepareThread().join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        preparing.add(elem);
        
        elem.setOnPrepared(this::FTPPrepared);
        elem.prepare();
        elem.setOnFinished(this::downloadFinished);
        elem.setOnFinishedB(func);
        elem.setOnUpdate(this::onUpdate);
        
        if (active) {
            addWhileActive(elem);
        }
        instance.prepared.add(elem);
         
    }
    
    private void FTPPrepared(Downloader loader) {
        preparing.remove((FTPDownloader) loader);
    }
    
    private void addWhileActive(Downloader elem) {
        numOfElems++;
        elem.setOnPrepared(this::prepareDone);
        
    }
    
    private void prepareDone(Downloader loader) {
        totalSize += loader.getTotalSize();
    }
    
    private void downloadFinished(Downloader loader) {
        
        done.add(loader);
        
        doneNum = done.size();
        System.out.println("download " + doneNum + "/" + numOfElems + " done");
        if (doneNum >= numOfElems) {
            System.out.println("downloading queue done");
            hideDownloader();
            reset();
            return;
        }
        
        prepared.get(doneNum).start();
        
    }
    
    public static void setStartBlocked(boolean blocked) {
        startBlocked = blocked;
    }
    
    public static void start() {
        
        if (active || startBlocked) {
            return;
        }
        
        instance.activate();
        active = true;
    }
    
    private void activate() {
                
        showDownloader();
        
        totalSize = 0;
                
        for (Downloader item : prepared) {
            numOfElems++;
            try {
                item.getPrepareThread().join();    
            } catch (InterruptedException ex) {
                Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            totalSize += item.getTotalSize();
            System.out.println("downloader total size:" + calcToMB(totalSize));
            
        }
        
        
        prepared.get(0).start();
        
        System.out.println("Started Downloading " + numOfElems + " Elements visible; totalSize: " + totalSize);
        
    }
    
    private void showDownloader() {
        
        if (visible) {
            return;
        }
        
        System.out.println("showing downloader");
        visible = true;
        
        //ModpackSelector.getInstance().shortenLabels();
        FXMLSheetController UI = FXMLSheetController.getInstance();
        UI.progressTextLeft.setVisible(true);
        UI.progressTextRight.setVisible(true);
        UI.downloadProgress.setVisible(true);
        
    }
    
    private void hideDownloader() {
        
        System.out.println("Hiding downloader");
        
        FTPConnection.disconnect();
        
        visible = false;
        Platform.runLater(() -> {
            FXMLSheetController UI = FXMLSheetController.getInstance();
            UI.progressTextLeft.setVisible(false);
            UI.progressTextRight.setVisible(false);
            UI.downloadProgress.setVisible(false);
        });
    }
    
    public static void setTitle(String title) {
        titleName = title;
    }
    
    private void onUpdate(float A, float B, Downloader loader) {
                
        System.out.println("Downloader Update: Progress =" + A * 100 + " ges=" + B);
        
        totalDone = A * B;
        for (Downloader down : done) {
            totalDone += down.getTotalSize();
        }
        
        System.out.println(totalDone + " | " + totalSize);
        float downloaded = calcToMB(totalDone);
        float total = calcToMB(totalSize);
        
        FXMLSheetController UI = FXMLSheetController.getInstance();
        
        UI.downloadProgress.setProgress(totalDone / totalSize);
        
        Platform.runLater(() -> {
            UI.progressTextRight.setText("datei " + (doneNum + 1) + "/" + numOfElems + ", " + downloaded + "/" + total + " MB");
            UI.progressTextLeft.setText(loader.getFileName());     
        });
        
    }
    
    private float calcToMB(float num) {
        
        float toReturn = num /  1_048_576;
        
        toReturn = ((float) ((int) (toReturn * 10))) / 10;
        
        return toReturn;
        
    }
    
    private void reset() {
        
        System.out.println("resetting download frame");
        titleName = "Downloading";
        numOfElems = 0;
        totalSize = 0;
        totalDone = 0;
        doneNum = 0;
        visible = false;
        
        prepared.clear();
        done.clear();
        Nodes.clear();
        active = false;
        preparing.clear();
        
    }
    
}
