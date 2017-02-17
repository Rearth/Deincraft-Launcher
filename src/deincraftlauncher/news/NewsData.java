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
import java.util.Calendar;
import java.util.Date;
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
    private static ArrayList<String> read = new ArrayList<>();
    public Date lastOpened;
    
    private NewsData() {}
    
    private static NewsData instance = new NewsData();
    
    public static NewsData getInstance() {
        return instance;
    }
    
    public static void addItem(String packName, String Title, String Text) {
        NewsElement elem = new NewsElement(packName, Title, Text, new Date());
        System.out.println("added Item: " + elem);
        instance.elements.add(elem);
        save();
    }
    
    public static class NewsElement implements Serializable, Comparable<NewsElement> {
        
        private static final long serialVersionUID = 852364761L;
        
        public NewsElement(String packName, String Title, String Text, Date date) {
            
            this.belonging = packName;
            this.Title = Title;
            this.Text = Text.replace("\\n", System.getProperty("line.separator"));
            System.out.println("Text=" + this.Text);
            this.date = date;
            System.out.println("NewsElement added at: " + date);
        }
        
        public final String belonging;
        public final String Title;
        public final String Text;
        public final Date date;

        @Override
        public String toString() {
            return "NewsElement{" + ", belonging=" + belonging + ", Title=" + Title + ", Text=" + Text + '}';
        }

        @Override
        public int compareTo(NewsElement o) {
            return o.date.compareTo(date);
        }
        
    }
    
    public static void save() {
        
        System.out.println("saving");
        
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

            //System.out.println("saving news " + instance);
            
            ous.writeObject(instance);

        } catch (Exception ex) {
            System.err.println("Error saving news: " + ex);
        }
        
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(Config.getNewsFile() + "personal"))) {

            //System.out.println("saving news " + instance);
            
            ous.writeObject(instance.read);

        } catch (Exception ex) {
            System.err.println("Error saving news personal: " + ex);
        }
        
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(Config.getNewsFile() + "date"))) {

            //System.out.println("saving news " + instance);
            
            ous.writeObject(instance.lastOpened);

        } catch (Exception ex) {
            System.err.println("Error saving news date: " + ex);
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
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Config.getNewsFile() + "personal"))) {

            instance.read = (ArrayList<String>) ois.readObject();

        } catch (Exception ex) {
            System.err.println("Error loading news personal: " + ex);
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Config.getNewsFile() + "date"))) {

            instance.lastOpened = (Date) ois.readObject();

        } catch (Exception ex) {
            System.err.println("Error loading news date: " + ex);
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

    public ArrayList<String> getRead() {
        return read;
    }
    
    public enum relativeDays {
        Heute, Gestern
    }
    
    public enum Monate {
        Jan, Feb, März, Apr, Mai, Jun, Jul, Aug, Sep, Okt, Nov, Dez
    }
    
    //in days
    public static int howLongAgo(Date date) {
        
        Date olddate = new Date();
        olddate.setDate(date.getDate());
        olddate.setMonth(date.getMonth());
        olddate.setYear(date.getYear());
                        
        long difference =  new Date().getTime() - olddate.getTime();

        return (int) (difference / (24 * 1000 * 60 * 60));
            
    }

    public static int getDayNumber(Date date) {
         Calendar c = Calendar.getInstance();
         c.setTime(date);

         return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonthNumber(Date date) {
         Calendar c = Calendar.getInstance();
         c.setTime(date);

         return c.get(Calendar.MONTH);
    }
    
    public static String getFormat(Date date) {
        if (howLongAgo(date) == 0) {
            return relativeDays.Heute.name();
        } else if (howLongAgo(date) == 1) {
            return relativeDays.Gestern.name();
        } else if (howLongAgo(date) < 7) {
            return Integer.toString(howLongAgo(date)) + " Tage";
        }
        
        String month = Monate.values()[getMonthNumber(date) - 1].name();
        
        return getDayNumber(date) + ". " + month;
    }
    
    public static void createUploadFile() {
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(Config.getNewsFile() + "toUpload"))) {

            //System.out.println("saving news " + instance);
            
            ous.writeObject(instance);

        } catch (Exception ex) {
            System.err.println("Error saving news upload file: " + ex);
        }
    }
    
}
