/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.modPacks;

import deincraftlauncher.Config;
import deincraftlauncher.IO.download.DownloadHandler;
import deincraftlauncher.designElements.DCTile;
import static deincraftlauncher.designElements.DesignHelpers.defaultgap;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Darkp
 */
public class ModpackSelector {
    
    private AnchorPane pane = null;
    private static final ModpackSelector instance = new ModpackSelector();
    private static final int iconSize = 84;
    public static final int posX = 644 - iconSize - defaultgap;
    private static final int topGap = 37;
    
    private final ArrayList<Modpack> Modpacks = new ArrayList<>();
    private final ArrayList<DCTile> Tiles = new ArrayList<>();
    private final ArrayList<Integer> usedIndex = new ArrayList<>();
    private final Rectangle background = new Rectangle();
    
    public Modpack selectedPack = null;
    private boolean labelShort = false;
    
    public static ModpackSelector getInstance() {
        return instance;
    }
    
    public void init() {
        pane = deincraftlauncher.FXMLSheetController.getInstance().mainPanel;
        background.setFill(Color.LIGHTBLUE);
        background.setLayoutX(posX - defaultgap);
        background.setLayoutY(0);
        background.setWidth(iconSize + 2 * defaultgap);
        background.setHeight(pane.getPrefHeight());
        background.setVisible(true);
        pane.getChildren().add(background);
    }
    
    private ModpackSelector() {
        
    }
    
    public void registerModpack(Modpack modpack) {
        Modpacks.add(modpack);
        
        DCTile tile = new DCTile(posX, calcY(freeY()), 84, modpack.getImage(), pane);
        Tiles.add(tile);
        tile.setOnFocus(this::packFocused);
        tile.setOnClick(this::selectPack);
        tile.setName(modpack.getName());
        
    }
    
    public void setPosition(int position, Modpack modpack) {
        
        Modpack pack = Modpacks.get(Modpacks.indexOf(modpack));
        
    }
    
    private int calcY(int index) {
        return defaultgap + defaultgap * 2 * index + iconSize * index + topGap;
    }
    
    private int freeY() {
        
        int i = 0;
        while(i < 10) {
            if (!usedIndex.contains(i)) {
                usedIndex.add(i);
                return i;
            } else {
                i++;
            }
        }
        return 0;
    }
    
    private void packFocused(DCTile tileClicked) {
        for (DCTile tile : Tiles) {
            tile.setFocused(false);
        }
    }
    
    private void selectPack(DCTile tileClicked) {
        System.out.println("Tile Klicked: " + tileClicked.getName());
        for (Modpack pack : Modpacks) {
            pack.hide();
        }
        Modpacks.get(Tiles.indexOf(tileClicked)).select();
        if (labelShort) {
            shortenLabels();
        }
        
        
        
    }
    
    public int getBottomEnd() {
        return Tiles.get(Tiles.size() - 1).getPosY() + Tiles.get(Tiles.size() - 1).getHeight();
    }
    
    public void addEnds() {
        
        Image exitImg = new Image(getClass().getResource("/deincraftlauncher/Images/exit.png").toString());
        DCTile exit = new DCTile(ModpackSelector.posX  + 50, defaultgap, 34, exitImg, deincraftlauncher.FXMLSheetController.getInstance().mainPanel);
        exit.setBackgroundColor(Color.TRANSPARENT);
        exit.setOnClick(this::handleExit);
        //exit.setShadow(false);
        
        Image minimizeImg = new Image(getClass().getResource("/deincraftlauncher/Images/minimize.png").toString());
        DCTile minimize = new DCTile(ModpackSelector.posX, defaultgap, 59 - 2 * defaultgap - 7, 59 + 1, minimizeImg, deincraftlauncher.FXMLSheetController.getInstance().mainPanel);
        minimize.setBackgroundColor(Color.TRANSPARENT);
        minimize.setOnClick(this::handleMinimize);
        //minimize.setShadow(false);
        
        Image optImg = new Image(getClass().getResource("/deincraftlauncher/Images/Settings-Tile.png").toString());
        DCTile opt = new DCTile(ModpackSelector.posX, getBottomEnd() + defaultgap, iconSize, 80, optImg, deincraftlauncher.FXMLSheetController.getInstance().mainPanel);
        opt.setBackgroundColor(Color.TRANSPARENT);
        opt.setOnClick(this::handleOptionClick);
        System.out.println("Dimensions:" + opt.getHeight() + " | " + opt.getWidth());
    }
    
    private void handleExit(DCTile tileClicked) {
        System.out.println("pressed exit Tile");
        deincraftlauncher.FXMLSheetController.getInstance().exit();
    }
    
    private void handleOptionClick(DCTile tileClicked) {
        System.out.println("opening settings");
        settings.showWindow();
        
    }
    
    private void handleMinimize(DCTile tileClicked) {
        ((Stage) background.getScene().getWindow()).setIconified(true);
    }
    
    public void shortenLabels() {
        selectedPack.getView().setPatchNotesSmall(true);
        labelShort = true;
    }
    
    public void normalLabels() {
        selectedPack.getView().setPatchNotesSmall(false);
        labelShort = false;
    }
    
    public void reloadScreenshots() {
        for (Modpack pack : Modpacks) {
            pack.reloadScreenshots();
        }
    }
    
    public void setStartLoading(boolean state) {
        for (Modpack pack : Modpacks) {
            pack.getView().setStartLoading(state);
        }
    }
    
    public ArrayList<Modpack> getPacks() {
        
        return Modpacks;
        
    }
    
}
