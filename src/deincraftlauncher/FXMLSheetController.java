/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher;

import deincraftlauncher.IO.FileUtils;
import deincraftlauncher.IO.download.Downloader;
import deincraftlauncher.designElements.DesignHelpers;
import deincraftlauncher.designElements.PackViewHandler;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.ModpackSelector;
import deincraftlauncher.modPacks.settings;
import deincraftlauncher.news.NewsHandler;
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
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Darkp
 */
public class FXMLSheetController implements Initializable {
    
    //google direct link: https://drive.google.com/uc?export=download&id=FILE_ID
    
    private static FXMLSheetController instance = null;
    private static final ModpackSelector PackSelecter = ModpackSelector.getInstance();
    private static boolean newsSelected = false;
    
    
    public static FXMLSheetController getInstance() {
        return instance;
    }
    
    
    
    @FXML
    public AnchorPane mainPanel;
    @FXML
    public Label labelModpacks;
    @FXML
    public Label labelNews;
    @FXML
    public Rectangle packSelectorPane;
    @FXML
    public Label PatchNotesTitle;
    @FXML
    public Label PatchNotesContent;
    @FXML
    public ImageView packImage;
    @FXML
    public Label StatusTitle;
    @FXML
    public Label StatusServer;
    @FXML
    public Label StatusPlayers;
    @FXML
    public Label closeLabel;
    @FXML
    public Label minimizeLabel;
    @FXML
    public Label playerLabel;
    @FXML
    public Label playerWelcome;
    @FXML
    public Rectangle startRect;
    @FXML
    public Label startButton;
    @FXML
    public Label progressTextRight;
    @FXML
    public Label progressTextLeft;
    @FXML
    public ProgressBar downloadProgress;
    @FXML
    public Pane ModpackPane;
    @FXML
    public ScrollPane NewsPane;
    @FXML
    public Pane ScrollPaneContent;
    @FXML
    public Rectangle newsSeparator;
    @FXML
    public Rectangle menuRect;
    @FXML
    public Label NewsContentTitle;
    @FXML
    public Label NewsContentText;
    @FXML
    public Label unreadNotes;
    @FXML
    public Label newsDate;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        
        loadConfig();
        settings.loadFromFile();
        
        
        PackSelecter.init();
        Image infinityImage = new Image(getClass().getResource("/deincraftlauncher/Images/logo_FTBInfinity.jpg").toString());
        Image skyImage = new Image(getClass().getResource("/deincraftlauncher/Images/logo_Skyfactory.png").toString());
        Image vanillaImage = new Image(getClass().getResource("/deincraftlauncher/Images/logo_Vanilla.png").toString());
        Image DCImage = new Image(getClass().getResource("/deincraftlauncher/Images/logo_Minefactory.png").toString());
        
        //DCTile test = new DCTile(50, 50, file, "Text", mainPanel);
        //test.setOnClick(this::onTestClick);
        //System.out.println(test.toString());
        
        Color packColor = DesignHelpers.foreGround;
        Modpack FTBInfinity = new Modpack("FTBInfinity", infinityImage, packColor);
        FTBInfinity.setWIP(true);
        Modpack Skyfactory = new Modpack("Skyfactory", skyImage, packColor);
        Skyfactory.setWIP(true);
        Modpack Vanilla = new Modpack("Vanilla", vanillaImage, packColor);
        Vanilla.setWIP(true);
        Modpack Deincraft = new Modpack("Minefactory", DCImage, packColor);
        Deincraft.setInfoFileLink("https://onedrive.live.com/download?cid=829AE01C48100392&resid=829AE01C48100392%21115&authkey=AO1QdK9f_lrHj5c");
        Deincraft.setForgeVersion("1.7.10-10.13.4.1614-1.7.10");
        Deincraft.setServer("46.4.75.39", 25565);
        Deincraft.setMCVersion("1.7.10");
        PackSelecter.registerModpack(Deincraft);
        PackSelecter.registerModpack(Skyfactory);
        PackSelecter.registerModpack(Vanilla);
        PackSelecter.registerModpack(FTBInfinity);
        
        //PackSelecter.addEnds();
        
        Deincraft.select();
        
        initFonts();
        initStartButton();
        initWindowDrag();
        initAnims();
        playerLabel.setText(settings.getInstance().NickName);
        PackViewHandler.initialSelect();
        
        checkForUpdates();
        NewsHandler.init();
        
        System.out.println("starting done");
        
    }

    private void loadConfig() {
        
        File config = new File(System.getProperty("user.home") + File.separator + "Minefactory" + File.separator + "config.txt");
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
    
    @FXML
    public void exit() {
        System.exit(0);
    }
    
    private void checkForUpdates() {
        
        for (Modpack pack : ModpackSelector.getInstance().getPacks()) {
            if (!pack.isWIP()) {
                System.out.println("Downloading info file for " + pack.getName());
                String saveTo = Config.getLauncherFolder() + pack.getName() + ".txt";
                Downloader.direectDownload(pack.getInfoFileLink(), saveTo);
                infoDownloaded(new File(saveTo), pack);
            }
            
        }
        
    }
    
    private void infoDownloaded(File info, final Modpack pack) {
        
        System.out.println("Downloaded info for pack " + pack.getName());
        
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
        
        /*String News = new String();
        for (int i = 1; i < total.size(); i++) {
            News += total.get(i) + System.getProperty("line.separator");
        }
        
        final String NewsB = News;
        Platform.runLater(() -> {
            pack.setNews(NewsB);
            pack.updateInfo();
        });*/
        
    }

    private void initFonts() {
        setFont(labelModpacks);
        setFont(labelNews);
        setFont(PatchNotesTitle);
        setFont(PatchNotesContent);
        setFont(StatusTitle);
        setFont(StatusServer);
        setFont(StatusPlayers);
        setFont(closeLabel);
        setFont(minimizeLabel);
        setFont(playerLabel);
        setFont(playerWelcome);
        setFont(startButton);
        setFont(progressTextRight);
        setFont(progressTextLeft);
        setFont(NewsContentTitle);
        setFont(NewsContentText);
        setFont(unreadNotes);
        setFont(newsDate);
        packImage.setEffect(DesignHelpers.getShadowEffect());  
        startRect.setEffect(DesignHelpers.getShadowEffect());  
        
    }
    
    private void setFont(Labeled node) {
        node.setFont(DesignHelpers.getTextFont((int) node.getFont().getSize()));
    }
    
    @FXML
    public void minimize() {
        ((Stage) mainPanel.getScene().getWindow()).setIconified(true);
    }

    private void initStartButton() {
        
        startButton.setOnMouseEntered((MouseEvent e) -> {
            handleHover(true, e);
        });
        
        startButton.setOnMouseExited((MouseEvent e) -> {
            handleHover(false, e);
        });
        
        startButton.setOnMouseClicked((MouseEvent e) -> {
            ModpackSelector.getInstance().selectedPack.handleStart();
        });
        
    }
    
    public void handleHover(boolean entered, MouseEvent e) {
        
        double scaleTo = 1.1;
        
        if (entered) {
            focusAnim(1.0, scaleTo, 120);
        } else {
            focusAnim(scaleTo, 1.0, 120);
            
        }
        
    }
    
    public void handleHover(boolean entered, MouseEvent e, Node node) {
        
        double scaleTo = 1.1;
        
        if (entered) {
            focusAnim(1.0, scaleTo, 120, node);
        } else {
            focusAnim(scaleTo, 1.0, 120, node);
            
        }
        
    }
    
    private void focusAnim(double from, double to, int time, Node node) {
        
        ScaleTransition scaleanim = new ScaleTransition(Duration.millis(time), node);
        scaleanim.setFromX(from);
        scaleanim.setToX(to);
        scaleanim.setFromY(from);
        scaleanim.setToY(to);
        scaleanim.setCycleCount(1);
        scaleanim.setAutoReverse(true);
        scaleanim.play();
        
    }
    
    private void focusAnim(double from, double to, int time) {
        
        
        ArrayList<Node> node = new ArrayList<>();
        node.add(startButton);
        node.add(startRect);
        
        for (Node Elem : node) {
            ScaleTransition scaleanim = new ScaleTransition(Duration.millis(time), Elem);
            scaleanim.setFromX(from);
            scaleanim.setToX(to);
            scaleanim.setFromY(from);
            scaleanim.setToY(to);
            scaleanim.setCycleCount(1);
            scaleanim.setAutoReverse(true);
            scaleanim.play();
        }
        
    }
    
    @FXML
    private void clickModpacks() {
        System.out.println("clicked on modpacks");
        if (!newsSelected) {
            return;
        }
        
        PackViewHandler.show();
        NewsHandler.hide();
        newsSelected = false;
        System.out.println("hiding modpack views");
        
        //doStuff
    }
    
    @FXML
    private void clickNews() {
        System.out.println("clicked on News");
        if (newsSelected) {
            return;
        }
        
        PackViewHandler.hide();
        NewsHandler.show();
        newsSelected = true;
        System.out.println("showing modpack views");
        
    }
    
    private static double xOffset = 0;
    private static double yOffset = 0;

    private void initWindowDrag() {
        menuRect.setOnMousePressed((MouseEvent event) -> {
            xOffset = DeincraftLauncherUI.window.getX() - event.getScreenX();
            yOffset = DeincraftLauncherUI.window.getY() - event.getScreenY();
        });
        
        menuRect.setOnMouseDragged((MouseEvent event) -> {
            DeincraftLauncherUI.window.setX(event.getScreenX() + xOffset);
            DeincraftLauncherUI.window.setY(event.getScreenY() + yOffset);
        });
    }
    
    private void initAnims() {
        //noStuff
    }
    
}
