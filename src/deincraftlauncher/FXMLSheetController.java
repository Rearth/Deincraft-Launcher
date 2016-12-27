/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher;

import deincraftlauncher.IO.FileUtils;
import deincraftlauncher.IO.ZIPExtractor;
import deincraftlauncher.IO.download.DownloadHandler;
import static deincraftlauncher.IO.download.DownloadHandler.downloaderHeight;
import deincraftlauncher.IO.download.Downloader;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.ModpackSelector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Darkp
 */
public class FXMLSheetController implements Initializable {
    
    private static FXMLSheetController instance = null;
    private static final ModpackSelector PackSelecter = ModpackSelector.getInstance();
    
    public static FXMLSheetController getInstance() {
        return instance;
    }
    
    
    @FXML
    public AnchorPane mainPanel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        
        loadConfig();
        
        PackSelecter.init();
        Image infinityImage = new Image(getClass().getResource("/deincraftlauncher/Images/logo_FTBInfinity.jpg").toString());
        Image skyImage = new Image(getClass().getResource("/deincraftlauncher/Images/logo_Skyfactory.png").toString());
        Image vanillaImage = new Image(getClass().getResource("/deincraftlauncher/Images/logo_Vanilla.png").toString());
        Image DCImage = new Image(getClass().getResource("/deincraftlauncher/Images/logo_Deincraft.png").toString());
        
        //DCTile test = new DCTile(50, 50, file, "Text", mainPanel);
        //test.setOnClick(this::onTestClick);
        //System.out.println(test.toString());
        Modpack FTBInfinity = new Modpack("FTBInfinity", infinityImage, Color.GRAY);
        FTBInfinity.setWIP(true);
        Modpack Skyfactory = new Modpack("Skyfactory", skyImage, Color.LIGHTBLUE);
        Skyfactory.setWIP(true);
        Modpack Vanilla = new Modpack("Vanilla", vanillaImage, Color.AQUAMARINE);
        Vanilla.setWIP(true);
        Modpack Deincraft = new Modpack("Deincraft - Tekkit", DCImage, Color.LIGHTGOLDENRODYELLOW);
        Deincraft.setInfoFileLink("https://www.dropbox.com/s/cjb72vmu7xm1f93/deincraft-tekkit.txt?dl=1");
        PackSelecter.registerModpack(Deincraft);
        PackSelecter.registerModpack(Skyfactory);
        PackSelecter.registerModpack(Vanilla);
        PackSelecter.registerModpack(FTBInfinity);
        System.out.println("selector bottom end: " + PackSelecter.getBottomEnd());
        
        PackSelecter.addEnds();
        
        Deincraft.select();
        
        downloadScreens();
        
        checkForUpdates();
    }   
    
    public void onTestClick() {
        System.out.println("klicked testTile!");
    }

    private void loadConfig() {
        
        File config = new File(System.getProperty("user.home") + File.separator + "Deincraft" + File.separator + "config.txt");
        System.out.println("loading config...");
        
        ArrayList<String> configText = readFile(config);
        Config.config().DCFolder = configText.get(1);
        System.out.println(Config.config().toString());
    }
    
    private ArrayList<String> readFile(File file) {
        
        BufferedReader br;
        ArrayList<String> Text = new ArrayList<>();
        String curText;
        
        try {
            br = new BufferedReader(new FileReader(file));
            
            while ((curText = br.readLine()) != null) {
                Text.add(curText);
            }
            
            br.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLSheetController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLSheetController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return Text;
    }
    
    public void exit() {
        System.exit(0);
    }
    
    private void downloadScreens() {
        
        if (new File(Config.getImagesFolder()).exists()) {
            return;
        } else {
            new File(Config.getImagesFolder()).mkdirs();
        }
        
        System.out.println("downloading Screenshots");
        
        String URL = "https://www.dropbox.com/s/wttaovp3pt4clkn/LauncherImages.zip?dl=1";
        String targetPath = Config.getLauncherFolder();
        DownloadHandler.addItem(targetPath, URL, this::onImgDone);
        DownloadHandler.setTitle("Downloade Screenshots");
        DownloadHandler.start();
        ModpackSelector.getInstance().selectedPack.getView().enableLoading();
    }
    
    
    private void onImgDone(Downloader loader) {
        System.out.println("Screenshots downloaded");
        
        try {
            ZIPExtractor.extractArchive(loader.getTargetFile(), Config.getLauncherFolder());
        } catch (Exception ex) {
            System.out.println("cant extract Images");
        }
        Platform.runLater(() -> {
            
            ModpackSelector.getInstance().reloadScreenshots();
            ModpackSelector.getInstance().selectedPack.getView().disableLoading();
        });
    }
    
    private void checkForUpdates() {
        
        ModpackSelector.getInstance().setStartLoading(true);
        
        for (Modpack pack : ModpackSelector.getInstance().getPacks()) {
            if (!pack.isWIP()) {
                System.out.println("Downloading info file for " + pack.getName());
                Downloader infoFile = new Downloader(pack.getInfoFileLink(), Config.getLauncherFolder());
                infoFile.start();
                infoFile.setOnFinished((Downloader loader) -> {
                    infoDownloaded(loader, pack);
                });
            }
            
        }
        
    }
    
    private void infoDownloaded(Downloader loader, final Modpack pack) {
        
        System.out.println("Downloaded info for pack " + pack.getName());
        
        File info = new File(loader.getTargetFile());
        
        ArrayList<String> total = FileUtils.readFile(info);
        String text = total.get(0);
        System.out.println("Update Text for " + pack.getName() + ": " + text);
        String[] texts = text.split(" ");
        
        if (texts.length != 6) {
            System.err.println("Error getting update data for Modpack " + pack.getName() + " length=" + texts.length);
            return;
        }
        
        pack.setMainVersion(texts[0]);
        pack.setMainLink(texts[1]);
        pack.setModsVersion(texts[2]);
        pack.setModsLink(texts[3]);
        pack.setConfigVersion(texts[4]);
        pack.setConfigLink(texts[5]);
        
        String News = "Patchnotes: " + System.getProperty("line.separator");
        for (int i = 1; i < total.size(); i++) {
            News += total.get(i) + System.getProperty("line.separator");
        }
        
        final String NewsB = News;
        Platform.runLater(() -> {
            pack.setNews(NewsB);
            pack.getView().updateInfo();
            ModpackSelector.getInstance().setStartLoading(false);
        });
        
    }
    
}
