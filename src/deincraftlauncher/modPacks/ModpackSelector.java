/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.modPacks;

import deincraftlauncher.designElements.DCTile;
import deincraftlauncher.designElements.PackViewHandler;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Darkp
 */
public class ModpackSelector {
    
    private AnchorPane pane = null;
    private static final ModpackSelector instance = new ModpackSelector();
    private static final int iconSize = 82;
    public static final int posX = 14;
    private static final int topGap = 120;
    
    private final ArrayList<Modpack> Modpacks = new ArrayList<>();
    private final ArrayList<DCTile> Tiles = new ArrayList<>();
    private final ArrayList<Integer> usedIndex = new ArrayList<>();
    
    public Modpack selectedPack = null;
    
    public static ModpackSelector getInstance() {
        return instance;
    }
    
    public void init() {
        pane = deincraftlauncher.FXMLSheetController.getInstance().mainPanel;
        /*background.setFill(DesignHelpers.foreGround);
        background.setLayoutX(posX - defaultgap);
        background.setLayoutY(0);
        background.setWidth(iconSize + 2 * defaultgap);
        background.setHeight(pane.getPrefHeight());
        background.setVisible(true);
        pane.getChildren().add(background);*/
    }
    
    private ModpackSelector() {
        
    }
    
    public void registerModpack(Modpack modpack) {
        Modpacks.add(modpack);
        
        DCTile tile = new DCTile(posX, calcY(freeY()), iconSize, modpack.getImage(), pane);
        Tiles.add(tile);
        tile.setOnFocus(this::packFocused);
        tile.setOnClick(this::selectPack);
        tile.setName(modpack.getName());
        
    }
    
    public void setPosition(int position, Modpack modpack) {
        
        Modpack pack = Modpacks.get(Modpacks.indexOf(modpack));
        
    }
    
    private int calcY(int index) {
        return 40 * index + iconSize * index + topGap;
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
        
        Modpacks.get(Tiles.indexOf(tileClicked)).select();
        
    }
    
    public void setStartLoading(boolean state) {
        PackViewHandler.setStartLoading(state);
    }
    
    public ArrayList<Modpack> getPacks() {
        
        return Modpacks;
        
    }
    
    public void setVisible(boolean state) {
        for (DCTile tile : Tiles) {
            tile.setVisible(state);
        }
    }
    
}
