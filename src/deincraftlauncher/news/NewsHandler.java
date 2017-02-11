/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.news;

import deincraftlauncher.Config;
import deincraftlauncher.FXMLSheetController;
import deincraftlauncher.IO.FileUtils;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.ModpackSelector;
import deincraftlauncher.news.NewsData.NewsElement;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Darkp
 */
public class NewsHandler {
    
    private static FXMLSheetController UI = FXMLSheetController.getInstance();
    
    public static void init() {
        System.out.println("initing news, downloading and reading file");
        UI = FXMLSheetController.getInstance();
        NewsData.loadFromFile();
        System.out.println("loaded from file/web, trying to add local elements");
        addFromFile();
        System.out.println("attempted to add elements from file, setting pack news");
        setPackNews();
        System.out.println("updated modpack news");
    }
    
    public static void show() {
        System.out.println("showing news Tab");
        UI.labelNews.getStyleClass().clear();
        UI.labelNews.getStyleClass().add("MenuLabelFocus");
        
        UI.NewsPane.setVisible(true);
        
        System.out.println(NewsData.getInstance());
    }
    
    public static void hide() {
        System.out.println("hiding news Tab");
        UI.labelNews.getStyleClass().clear();
        UI.labelNews.getStyleClass().add("MenuLabel");
        UI.NewsPane.setVisible(false);
    }
    
    public static void addFromFile() {
        File maybe = new File(Config.getLauncherFolder() + "toAdd.txt");
        if (maybe.exists()) {
            System.out.println("found News to add!");
            ArrayList<String> elems = FileUtils.readFile(maybe);
            for (int i=0; i < elems.size(); i += 3) {
                NewsData.addItem(elems.get(i), elems.get(i + 1), elems.get(i + 2));
            }
        }
        
        maybe.delete();
    }
    
    private static void setPackNews() {
        for (NewsElement elem : NewsData.getInstance().getElements()) {
            for (Modpack pack : ModpackSelector.getInstance().getPacks()) {
                if (elem.belonging.equals(pack.getName())) {
                    System.out.println("found news for pack: " + pack.getName() + " title=" + elem.Title + " text=" + elem.Text);
                    pack.setNews(elem.Text);
                    pack.updateInfo();
                }
            }
        }
    }
    
    //adds elements to pane
    private static void showElements() {
        //UI.NewsPane.getc
    }
}
