/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import deincraftlauncher.IO.download.DownloadHandler;
import static deincraftlauncher.IO.download.DownloadHandler.downloaderHeight;
import static deincraftlauncher.designElements.DesignHelpers.*;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.ModpackSelector;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Darkp
 */
public final class ModpackView {
    
    private static final int galleryX = defaultgap;
    private static final int galleryY = 80;
    private static final int gallerySizeX = 425;
    private static final int gallerySizeY = 265;
    private static final int infoX = galleryX + gallerySizeX + defaultgap * 2;
    private static final int stateY = 90;
    private static final int playersY = stateY + 25;
    private static final int notesX = defaultgap;
    private static final int notesY = galleryY + gallerySizeY + defaultgap;
    private static final int versionY = playersY + 25;
    private static final int notesSizeX = ModpackSelector.posX - defaultgap * 3;
    private static final int radius = 0;
    private static final Insets insets =  new Insets(0, 0, 0, 0);
    private static final CornerRadii radii = new CornerRadii(
            radius, radius, radius, radius, radius, radius, radius, radius,
            false,  false,  false,  false,  false,  false,  false,  false
    );

    private final Modpack pack;
    private final ArrayList<Node> Nodes = new ArrayList<>();
    private Gallery gallery;
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
    
    private ImageView mainView;
    
    private boolean Visible = false;
    
    public ModpackView(Modpack pack) {
        System.out.println("creating view for: " + pack.getName());
        this.pack = pack;
        gallery = new Gallery(galleryX, galleryY, gallerySizeX, gallerySizeY);
        gallery.addImage(pack.getScreenshots());
        gallery.show();
        Nodes.addAll(gallery.getNodes());
        
        title = new Label();
        title.setText(pack.getName());
        title.setLayoutX(0);
        title.setLayoutY(0);
        title.setPrefHeight(galleryY - defaultgap);
        title.setPrefWidth(ModpackSelector.posX - defaultgap);
        title.setAlignment(Pos.CENTER);
        title.setFont(getTitleFont());
        title.setEffect(DesignHelpers.getShadowEffect(50));
        title.setBackground(new Background(new BackgroundFill(pack.getTitleColor(), radii, insets)));
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(title);
        Nodes.add(title);
        
        background = new Rectangle();
        background.setFill(DesignHelpers.brighter(DesignHelpers.brighter(pack.getTitleColor())));
        background.setLayoutX(galleryX + gallerySizeX + defaultgap);
        background.setLayoutY(galleryY);
        background.setWidth(ModpackSelector.posX - galleryX - gallerySizeX - defaultgap * 3);
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
        patchNotesBack.setFill(DesignHelpers.brighter(pack.getTitleColor()));
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
        playersInfoR.setPrefWidth(ModpackSelector.posX - infoX - defaultgap * 3);
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
        versionR.setPrefWidth(ModpackSelector.posX - infoX - defaultgap * 3);
        versionR.setTextAlignment(TextAlignment.RIGHT);
        versionR.setAlignment(Pos.CENTER_RIGHT);
        versionR.setText(pack.getVersion());
        versionR.setFont(getLabelFont());
        Nodes.add(versionR);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(versionR);
        
        serverStateR = new Label();
        serverStateR.setLayoutX(infoX);
        serverStateR.setLayoutY(stateY);
        serverStateR.setPrefWidth(ModpackSelector.posX - infoX - defaultgap * 3);
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
        patchNotes.setText(pack.getNews());
        Nodes.add(patchNotes);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(patchNotes);
        
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
        
        mainView = new ImageView();
        mainView.setLayoutX(galleryX + gallerySizeX * 0.4);
        mainView.setLayoutY(galleryY + gallerySizeY * 0.4);
        mainView.setFitWidth(128);
        mainView.setFitHeight(128);
        mainView.setImage(new Image(getClass().getResource("/deincraftlauncher/Images/loading.gif").toString()));
        Nodes.add(mainView);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(mainView);
        
    }
    
    public void disableLoading() {
        
        System.out.println("showing gallery");
        
        Platform.runLater(() -> {
            gallery.hide();
            
            gallery = new Gallery(galleryX, galleryY, gallerySizeX, gallerySizeY);
            gallery.addImage(pack.getScreenshots());
            gallery.show();
            Nodes.addAll(gallery.getNodes());

            Nodes.remove(mainView);
            deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().remove(mainView);
        });
        
    }
    
}
