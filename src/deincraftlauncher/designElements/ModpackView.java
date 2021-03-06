/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import deincraftlauncher.DeincraftLauncherUI;
import deincraftlauncher.IO.download.DownloadHandler;
import static deincraftlauncher.designElements.DesignHelpers.*;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.ModpackSelector;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Darkp
 */
public final class ModpackView {
    
    private static final int galleryX = defaultgap * 3;
    private static final int galleryY = 110;
    private static final int gallerySizeX = 350 - galleryX;
    private static final int gallerySizeY = 190;
    private static final int infoX = galleryX + gallerySizeX + (int) (defaultgap * 2.1);
    private static final int stateY = galleryY + (int) (defaultgap * 0.6);
    private static final int playersY = stateY + 25;
    public static final int notesX = defaultgap;
    public static final int notesY = galleryY + gallerySizeY + defaultgap;
    private static final int versionY = playersY + 25;
    private static final int startSizeX = ModpackSelector.posX - galleryX - gallerySizeX - defaultgap * 6;
    private static final int startSizeY = 75;
    private static final int startX = infoX;
    private static final int startY = galleryY + gallerySizeY - startSizeY - defaultgap;
    public static final int notesSizeX = ModpackSelector.posX - defaultgap * 3;
    private static final int radius = 0;
    private static final Insets insets =  new Insets(0, 0, 0, 0);
    private static final CornerRadii radii = new CornerRadii(
            radius, radius, radius, radius, radius, radius, radius, radius,
            false,  false,  false,  false,  false,  false,  false,  false
    );
    
    private static final int arc = 15;
    private static final int sizeX = 600;
    private static final int sizeY = 430;
    private static final Color backgroundColor = Color.rgb(50, 50, 60);
    private static final Color blueColor = Color.DARKBLUE;
    private static final int separatorY = 60;
    private static final int separatorSizeY = 4;

    private final Modpack pack;
    private final ArrayList<Node> Nodes = new ArrayList<>();
    private Gallery gallery;
    private final DCTile StartTile;
    private final Label StartText;
    private final Label title;
    private final Label playersInfo;
    private final Label version;
    private final Label serverState;
    private final Label playersInfoR;
    private final Label versionR;
    private final Label serverStateR;
    private final Label patchNotes;
    private final Rectangle patchNotesBack;
    private final Rectangle background;
    private final int notesSizeY;
    
    private ImageView loading;
    private ImageView loadingb;
    private boolean WIP = false;
    private Rectangle gray;
    private Label titleWIP;
    
    private static double xOffset = 0;
    private static double yOffset = 0;
    
    private boolean Visible = false;
    private boolean Loading = false;
    
    public ModpackView(Modpack pack) {
        System.out.println("creating view for: " + pack.getName());
        this.pack = pack;
        gallery = new Gallery(galleryX, galleryY, gallerySizeX, gallerySizeY);
        //gallery.addImage(pack.getScreenshots());
        gallery.show();
        Nodes.addAll(gallery.getNodes());
        
        title = new Label();
        title.setText(pack.getName());
        title.setLayoutX(defaultgap);
        title.setLayoutY(defaultgap);
        title.setPrefHeight(galleryY - defaultgap * 2);
        title.setPrefWidth(ModpackSelector.posX - defaultgap * 3);
        title.setAlignment(Pos.CENTER);
        title.setFont(getTitleFont());
        title.setEffect(DesignHelpers.getShadowEffect(50));
        //title.setBackground(new Background(new BackgroundFill(pack.getTitleColor(), radii, insets)));
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(title);
        Nodes.add(title);
        
        title.setOnMousePressed((MouseEvent event) -> {
            xOffset = DeincraftLauncherUI.window.getX() - event.getScreenX();
            yOffset = DeincraftLauncherUI.window.getY() - event.getScreenY();
        });
        
        title.setOnMouseDragged((MouseEvent event) -> {
            DeincraftLauncherUI.window.setX(event.getScreenX() + xOffset);
            DeincraftLauncherUI.window.setY(event.getScreenY() + yOffset);
        });
        
        
        //right rectangle
        background = new Rectangle();
        //background.setFill(pack.getTitleColor());
        background.setLayoutX(galleryX + gallerySizeX + defaultgap * 1.5);
        background.setLayoutY(galleryY);
        background.setWidth(ModpackSelector.posX - galleryX - gallerySizeX - defaultgap * 5);
        background.setHeight(gallerySizeY);
        background.setEffect(getSmallShadow());
        Nodes.add(background);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(background);
        
        playersInfo = new Label();
        playersInfo.setLayoutX(infoX);
        playersInfo.setLayoutY(playersY);
        playersInfo.setText("Spieler: ");
        playersInfo.setFont(getLabelFont());
        Nodes.add(playersInfo);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(playersInfo);
        
        notesSizeY = (int) deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getPrefHeight() - notesY - defaultgap;
        
        patchNotesBack = new Rectangle();
        //patchNotesBack.setFill(pack.getTitleColor());
        patchNotesBack.setLayoutX(notesX);
        patchNotesBack.setLayoutY(notesY);
        patchNotesBack.setWidth(notesSizeX);
        patchNotesBack.setHeight(notesSizeY);
        patchNotesBack.setEffect(getSmallShadow());
        Nodes.add(patchNotesBack);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(patchNotesBack);
        
        version = new Label();
        version.setWrapText(true);
        version.setLayoutX(infoX);
        version.setLayoutY(versionY);
        version.setText("Version: ");
        version.setFont(getLabelFont());
        Nodes.add(version);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(version);
        
        serverState = new Label();
        serverState.setLayoutX(infoX);
        serverState.setLayoutY(stateY);
        serverState.setText("Server: ");
        serverState.setFont(getLabelFont());
        Nodes.add(serverState);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(serverState);
                
        playersInfoR = new Label();
        playersInfoR.setWrapText(true);
        playersInfoR.setLayoutX(infoX);
        playersInfoR.setLayoutY(playersY);
        playersInfoR.setPrefWidth(startSizeX);
        playersInfoR.setTextAlignment(TextAlignment.RIGHT);
        playersInfoR.setAlignment(Pos.CENTER_RIGHT);
        playersInfoR.setText(pack.getPlayersOnline() + "/" + pack.getMaxPlayers());
        playersInfoR.setFont(getLabelFont());
        Nodes.add(playersInfoR);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(playersInfoR);
        
        versionR = new Label();
        versionR.setWrapText(true);
        versionR.setLayoutX(infoX);
        versionR.setLayoutY(versionY);
        versionR.setPrefWidth(startSizeX);
        versionR.setTextAlignment(TextAlignment.RIGHT);
        versionR.setAlignment(Pos.CENTER_RIGHT);
        versionR.setText(pack.getVersion());
        versionR.setFont(getLabelFont());
        Nodes.add(versionR);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(versionR);
        
        serverStateR = new Label();
        serverStateR.setLayoutX(infoX);
        serverStateR.setLayoutY(stateY);
        serverStateR.setPrefWidth(startSizeX);
        serverStateR.setTextAlignment(TextAlignment.RIGHT);
        serverStateR.setAlignment(Pos.CENTER_RIGHT);
        serverStateR.setFont(getLabelFont());
        serverStateR.setText(pack.getServerState());
        Nodes.add(serverStateR);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(serverStateR);
        
        patchNotes = new Label();
        patchNotes.setWrapText(true);
        patchNotes.setLayoutX(notesX + defaultgap);
        patchNotes.setLayoutY(notesY + defaultgap);
        patchNotes.setPrefSize(notesSizeX - 2 * defaultgap, notesSizeY - 2 * defaultgap);
        patchNotes.setAlignment(Pos.TOP_LEFT);
        patchNotes.setText(pack.getNews());
        Nodes.add(patchNotes);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(patchNotes);
        patchNotes.setOnMouseClicked((MouseEvent e) -> {
                showPatchNotes();
            });
        patchNotes.setFont(DesignHelpers.getNotesFont());
        
        StartText = new Label();
        StartText.setLayoutX(infoX);
        StartText.setLayoutY(startY);
        StartText.setPrefWidth(startSizeX);
        StartText.setPrefHeight(startSizeY);
        StartText.setTextAlignment(TextAlignment.CENTER);
        StartText.setAlignment(Pos.CENTER);
        StartText.setFont(getFocusFont());
        StartText.setText("Start");
        Nodes.add(StartText);
        if (pack.updateAvaible()) {
            StartText.setText("Update");
        }
        
        Image imgStart = new Image(getClass().getResource("/deincraftlauncher/Images/start.png").toString());
        
        StartTile = new DCTile(startX, startY, startSizeX, startSizeY, imgStart, deincraftlauncher.FXMLSheetController.getInstance().mainPanel);
        StartTile.setBackgroundColor(Color.TRANSPARENT);
        Nodes.addAll(StartTile.getNodes());
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(StartText);
        StartText.setOnMouseEntered((MouseEvent e) -> {
                StartTile.handleHover(true, e);
        });
        StartText.setOnMouseExited((MouseEvent e) -> {
                StartTile.handleHover(false, e);
        });
        Nodes.addAll(StartTile.getNodes());
        StartText.setOnMouseClicked((MouseEvent e) -> {
                startClicked();
        });
        this.setVisible(false);
    }

    public boolean isVisible() {
        return Visible;
    }

    public void setVisible(boolean Visible) {
        if (Visible) {
            System.out.println("showing view: " + pack.getName());
        }
        
        for (Node node: Nodes) {
            node.setVisible(Visible);
        }
        
        if (WIP) {
            gray.toFront();
            titleWIP.toFront();
        }
        
        if (Loading) {
            setStartVisible(false);
        }
        
        gallery.startTimer();
        this.Visible = Visible;
    }
    
    public void setPatchNotesSmall(boolean state) {
        
        //int notesSizeY = (int) deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getPrefHeight() - notesY - defaultgap;
        
        if (state) {
            patchNotes.setPrefSize(notesSizeX - 2 * defaultgap, notesSizeY - DownloadHandler.downloaderHeight - 2 * defaultgap);
            patchNotesBack.setHeight(notesSizeY - DownloadHandler.downloaderHeight);
        } else {
            System.out.println("making notes normal");
            patchNotes.setPrefSize(notesSizeX - 2 * defaultgap, notesSizeY - 2 * defaultgap);
            patchNotesBack.setHeight(notesSizeY);
        }
    }
    
    public void enableLoading() {
        gallery.hide();
        
        loading = new ImageView();
        loading.setLayoutX(galleryX + gallerySizeX * 0.4);
        loading.setLayoutY(galleryY + gallerySizeY * 0.1);
        loading.setFitWidth(128);
        loading.setFitHeight(128);
        loading.setImage(new Image(getClass().getResource("/deincraftlauncher/Images/loading.gif").toString()));
        Nodes.add(loading);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(loading);
        
    }
    
    public void disableLoading() {
        
        System.out.println("showing gallery");
        
        Platform.runLater(() -> {
            gallery.hide();
            
            gallery = new Gallery(galleryX, galleryY, gallerySizeX, gallerySizeY);
            //gallery.addImage(pack.getScreenshots());
            gallery.show();
            Nodes.addAll(gallery.getNodes());

            Nodes.remove(loading);
            deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().remove(loading);
        });
        
    }
    
    public void updateInfo() {
        patchNotes.setText(pack.getNews());
        versionR.setText(pack.getMainVersionInstalled());
        if (pack.updateAvaible()) {
            StartText.setText("Update");
        } else {
            StartText.setText("Start");
        }
        pack.saveConfig();
    }
    
    public void updateStats() {
        
        playersInfoR.setText(pack.getPlayersOnline() + "/" + pack.getMaxPlayers());
        if (pack.getServerState().equals("Online")) {
            serverStateR.setTextFill(Color.GREEN);
        } else {
            serverStateR.setTextFill(Color.RED);            
        }
        serverStateR.setText(pack.getServerState());
    }
    
    private void startClicked() {
        if (!StartTile.isFocusable()) {
            System.out.println("pressed locked start");
            return;
        }
        pack.handleStart();
    }
    
    private void setStartVisible(boolean State) {
        
        if (!this.Visible) {
            return;
        }
        
        StartTile.setVisible(State);
        StartText.setVisible(State);
    }
    
    private void setStartLocked(String Text) {
        StartText.setText(Text);
        if (Text.length() > 5) {
            StartText.setFont(DesignHelpers.getFocusFont(14));
        }
        StartTile.setFocused(false);
        StartTile.setFocusable(false);
        StartTile.setImage(new Image(getClass().getResource("/deincraftlauncher/Images/start_locked.png").toString()));
    }
    
    private void setStartUnLocked(String Text) {
        
        StartText.setFont(DesignHelpers.getFocusFont());
        StartText.setText(Text);
        StartTile.setFocusable(true);
        StartTile.setImage(new Image(getClass().getResource("/deincraftlauncher/Images/start.png").toString()));
    }
    
    private void setStartLoading(boolean state) {
        this.Loading = state;
        
        setStartVisible(!state);
        
        if (state) {
            loadingb = new ImageView();
            loadingb.setLayoutX(startX + 20);
            loadingb.setLayoutY(startY);
            loadingb.setFitWidth(64);
            loadingb.setFitHeight(64);
            loadingb.setImage(new Image(getClass().getResource("/deincraftlauncher/Images/loading.gif").toString()));
            if (this.Visible) {
                Nodes.add(loadingb);
                deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(loadingb);
            }
        } else {
            System.out.println("removing start loading icon");
            Nodes.remove(loadingb);
            deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().remove(loadingb);            
        }
    }
    
    public void setWIP() {
        
        WIP = true;
        
        boolean visible = pack.equals(ModpackSelector.getInstance().selectedPack);
        
        gray = new Rectangle();
        gray.setFill(Color.rgb(30, 30, 30, 0.8));
        gray.setLayoutX(0);
        gray.setLayoutY(0);
        gray.setWidth(ModpackSelector.posX - defaultgap);
        gray.setHeight(deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getPrefHeight());
        Nodes.add(gray);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(gray);
        if (!visible) {
            gray.setVisible(false);
        }
        
        titleWIP = new Label();
        titleWIP.setText("Coming soon");
        titleWIP.setFont(Font.font("Verdana", FontWeight.BOLD, 72));
        titleWIP.setTextFill(Color.ALICEBLUE);
        titleWIP.setPrefWidth(ModpackSelector.posX - defaultgap);
        titleWIP.setPrefHeight(deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getPrefHeight());
        titleWIP.setAlignment(Pos.CENTER);
        titleWIP.setTextAlignment(TextAlignment.CENTER);
        Nodes.add(titleWIP);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(titleWIP);
        if (!visible) {
            titleWIP.setVisible(false);
        }
    }
    
    private void showPatchNotes() {
        System.out.println("showing patchnotes large");
        
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initOwner(deincraftlauncher.DeincraftLauncherUI.window);
        
        AnchorPane backgroundNotes = new AnchorPane();
        
        Rectangle bBlack = new Rectangle();
        bBlack.setWidth(sizeX);
        //bBlack.setHeight(sizeY);
        bBlack.setFill(backgroundColor);
        bBlack.setArcHeight(arc);
        bBlack.setArcWidth(arc);
        backgroundNotes.getChildren().add(bBlack);
        
        Rectangle separator = new Rectangle();
        separator.setWidth(sizeX);
        separator.setHeight(separatorSizeY);
        separator.setLayoutY(separatorY);
        separator.setFill(blueColor);
        backgroundNotes.getChildren().add(separator);
        
        
        Label Title = new Label();
        Title.setText("Patchnotes");
        Title.setPrefSize(sizeX, separatorY);
        Title.setTextFill(Color.WHITESMOKE);
        Title.setTextAlignment(TextAlignment.CENTER);
        Title.setFont(DesignHelpers.getFocusFont(42));
        Title.setAlignment(Pos.CENTER);
        backgroundNotes.getChildren().add(Title);
        Title.setOnMouseClicked((MouseEvent e) -> {
                hidePatchNotes(dialog);
            });
                
        Label patchNotesLarge = new Label();
        patchNotesLarge.setText(pack.getNews());
        patchNotesLarge.setLayoutX(defaultgap);
        patchNotesLarge.setLayoutY(separatorY + separatorSizeY + defaultgap);
        patchNotesLarge.setPrefWidth(sizeX - 2* defaultgap);
        patchNotesLarge.setTextFill(Color.WHITESMOKE);
        patchNotesLarge.setAlignment(Pos.TOP_LEFT);
        patchNotesLarge.setFont(DesignHelpers.getNotesFont());
        backgroundNotes.getChildren().add(patchNotesLarge);
        patchNotesLarge.setOnMouseClicked((MouseEvent e) -> {
                hidePatchNotes(dialog);
            });
        
        
        bBlack.setHeight(sizeY);
        
        Scene dialogScene = new Scene(backgroundNotes, sizeX, sizeY);
        dialog.setScene(dialogScene);
        dialog.show();
        
        //System.out.println("text height: " + patchNotesLarge.heightProperty());
        int height = patchNotesLarge.heightProperty().intValue() + separatorY + separatorSizeY;
        bBlack.setHeight(height);
        dialog.setHeight(height);
        
    }
    
    private void hidePatchNotes(Stage stage) {
        System.out.println("hiding patchnotes large");
        
        stage.close();
    }
    
}
