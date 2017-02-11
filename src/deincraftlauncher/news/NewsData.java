/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.news;

import deincraftlauncher.Config;
import deincraftlauncher.IO.download.Downloader;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.ModpackSelector;
import deincraftlauncher.modPacks.settings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darkp
 */
public class NewsData implements Serializable {
    
    private static final String onlineLink = "https://onedrive.live.com/download?cid=829AE01C48100392&resid=829AE01C48100392%21125&authkey=APgF8GgxNC7SLfQ";
    private static final long serialVersionUID = 12348765434689L;
    
    private final ArrayList<NewsElement> elements = new ArrayList<>();
    private final ArrayList<NewsElement> read = new ArrayList<>();
    
    private NewsData() {}
    
    private static NewsData instance = new NewsData();
    
    public static NewsData getInstance() {
        return instance;
    }
    
    public static void addItem(String packName, String Title, String Text) {
        NewsElement elem = new NewsElement(packName, Title, Text);
        System.out.println("added Item: " + elem);
        instance.elements.add(elem);
        save();
    }
    
    public static class NewsElement implements Serializable {
        
        private static final long serialVersionUID = 852364761L;
        
        public NewsElement(String packName, String Title, String Text) {
            
            this.belonging = packName;
            this.Title = Title;
            this.Text = Text.replace("\\n", System.getProperty("line.separator"));
            System.out.println("Text=" + this.Text);
        }
        
        public final String belonging;
        public final String Title;
        public final String Text;

        @Override
        public String toString() {
            return "NewsElement{" + ", belonging=" + belonging + ", Title=" + Title + ", Text=" + Text + '}';
        }
        
    }
    
    public static void save() {
        
        File existing = new File(Config.getLauncherFolder());
        if (!existing.exists()) {
            existing.mkdirs();
        }
        
        existing = new File(Config.getNewsFile());
        if (!existing.exists()) {
            try {
                existing.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(settings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(Config.getNewsFile()))) {

            System.out.println("saving news " + instance);
            
            ous.writeObject(instance);

        } catch (Exception ex) {
            System.err.println("Error saving news: " + ex);
        }
        
    }
    
    public static void loadFromFile() {
        
        //Download file from inet
        Downloader.direectDownload(onlineLink, Config.getNewsFile());
        
        System.out.println("Starting to read News from file");

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Config.getNewsFile()))) {

            instance = (NewsData) ois.readObject();

        } catch (Exception ex) {
            System.err.println("Error loading news: " + ex);
        }
        
        System.out.println("Read news file" + instance.toString());
    }

    @Override
    public String toString() {
        return "NewsData{" + "elements=" + elements + ", read=" + read + '}';
    }
    
    public static Modpack findPack(NewsElement elem) {
        for (Modpack pack : ModpackSelector.getInstance().getPacks()) {
                if (pack.getName().equals(elem.belonging)) {
                    return pack;
                }
            }
        return null;
    }

    public ArrayList<NewsElement> getElements() {
        return elements;
    }

    public ArrayList<NewsElement> getRead() {
        return read;
    }
    
}
