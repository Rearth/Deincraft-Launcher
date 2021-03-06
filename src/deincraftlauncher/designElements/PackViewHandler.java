/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import deincraftlauncher.FXMLSheetController;
import deincraftlauncher.IO.download.DownloadHandler;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.ModpackSelector;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author Darkp
 */
public class PackViewHandler {
    
    private static ImageView loadingb;
    private static boolean startLoading = false;
    private static final FXMLSheetController UI = FXMLSheetController.getInstance();
    private static boolean DownloadVisible = false;
    
    public enum StartState {
        Locked, Loading, Normal
    }
    
    public static void showPack(Modpack pack) {
        
        UI.PatchNotesTitle.setText(pack.getName().toLowerCase() + " news");
        UI.PatchNotesContent.setText(pack.getNews().toLowerCase());
        
    }
    
    public static void setPatchNotes(String Text) {
        UI.PatchNotesContent.setText(Text);
    }

    public static void updateStats(Modpack packi) {
        
        final Modpack pack = ModpackSelector.getInstance().selectedPack;
        
        Platform.runLater(() -> {UI.StatusServer.setText(pack.getServerState());
            if (pack.getServerState().equals("Online")) {
                UI.StatusServer.setStyle("-fx-text-fill: green; -fx-effect: dropshadow(three-pass-box, black, 1, 1, 0, 0);");
            } else {
                UI.StatusServer.setStyle("-fx-text-fill: red; -fx-effect: dropshadow(three-pass-box, black, 1, 1, 0, 0);");
            }

            UI.StatusPlayers.setText(pack.getPlayersOnline() + "/" + pack.getMaxPlayers()); 
        });
        
    }
    
    private static void setStartVisible(boolean State) {
                
        UI.startButton.setVisible(State);
        UI.startRect.setVisible(State);
    }
    
    private static void setStartLocked(String Text) {
        UI.startButton.setText(Text);
        if (Text.length() > 5) {
            UI.startButton.setFont(DesignHelpers.getFocusFont(14));
        }
        UI.startRect.setFill(Color.GRAY);
        UI.startButton.setOnMouseClicked((MouseEvent e) -> {
            //doNufin
        });
        UI.startButton.setOnMouseEntered((MouseEvent e) -> {
            //doNufin
        });
        UI.startButton.setOnMouseExited((MouseEvent e) -> {
            //doNufin
        });
    }
    
    private static void setStartUnLocked(String Text) {
        
        if (DownloadHandler.isActive()) {
            System.out.println("attempted to unlock start while downloading...");
            UI.startButton.setText("Downloade");
            UI.startButton.setFont(DesignHelpers.getFocusFont(14));
            UI.startRect.setFill(Color.GRAY);
        }
        
        UI.startButton.setFont(DesignHelpers.getFocusFont());
        UI.startButton.setText(Text);
        UI.startRect.setFill(Color.rgb(56, 102, 185));
        
        UI.startButton.setOnMouseClicked((MouseEvent e) -> {
            ModpackSelector.getInstance().selectedPack.handleStart();
        });
        UI.startButton.setOnMouseEntered((MouseEvent e) -> {
            FXMLSheetController.getInstance().handleHover(true, e);
        });
        UI.startButton.setOnMouseExited((MouseEvent e) -> {
            FXMLSheetController.getInstance().handleHover(false, e);
        });
    }
    
    private static void setStartLoading(boolean state) {
        
        if (startLoading && state) {
            return;
        }
        
        startLoading = state;
        
        setStartVisible(!state);
        
        if (state) {
            loadingb = new ImageView();
            loadingb.setLayoutX(UI.startRect.getLayoutX() + UI.startRect.getWidth() / 2 - 32);
            loadingb.setLayoutY(UI.startRect.getLayoutY() + UI.startRect.getHeight() / 2 + 32);
            loadingb.setFitWidth(64);
            loadingb.setFitHeight(64);
            loadingb.setImage(new Image(PackViewHandler.class.getResource("/deincraftlauncher/Images/loading.gif").toString()));
            deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(loadingb);
        } else {
            System.out.println("removing start loading icon");
            //loadingb.setVisible(false);
            deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().remove(loadingb);            
        }
    }
    
    private static void setStartText(String Text) {
        if (Text.length() > 5) {
            UI.startButton.setFont(DesignHelpers.getFocusFont(14));
        }
        UI.startButton.setText(Text);
    }
    
    public static void initialSelect() {
        System.out.println("initial selecting for Modpacks");
        UI.labelModpacks.getStyleClass().clear();
        UI.labelModpacks.getStyleClass().add("MenuLabelFocus");
    }
    
    public static void hide() {
        UI.labelModpacks.getStyleClass().clear();
        UI.labelModpacks.getStyleClass().add("MenuLabel");
        
        DownloadVisible = UI.downloadProgress.isVisible();
        
        for (Node node :  UI.ModpackPane.getChildren()) {
            node.setVisible(false);
        }
        
        ModpackSelector.getInstance().setVisible(false);
        
        try {
            loadingb.setVisible(false);
        } catch (NullPointerException ex) {
            //expected
        }
    }
    
    public static void show() {
        UI.labelModpacks.getStyleClass().clear();
        UI.labelModpacks.getStyleClass().add("MenuLabelFocus");
        
        for (Node node :  UI.ModpackPane.getChildren()) {
            node.setVisible(true);
        }
        
        if (!DownloadVisible) {
            UI.progressTextLeft.setVisible(false);
            UI.progressTextRight.setVisible(false);
            UI.downloadProgress.setVisible(false);
        }
        Modpack pack = ModpackSelector.getInstance().selectedPack;
        pack.select();
        
        ModpackSelector.getInstance().setVisible(true);
    }
    
    public static void reloadStart(StartState startState, String startText, Modpack pack) {
        if (!pack.equals(ModpackSelector.getInstance().selectedPack)) {
            System.out.println("changed start state for not selected pack");
            return;
        }
        
        switch(startState) {
            case Locked:
                setStartLocked(startText);
                setStartLoading(false);
                break;
            case Loading:
                setStartLoading(true);
                break;
            case Normal:
                setStartUnLocked(startText);
                setStartLoading(false);
                break;
        }
        
    }
    
}
