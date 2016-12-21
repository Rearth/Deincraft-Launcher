/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.modPacks;

import deincraftlauncher.Config;
import deincraftlauncher.designElements.ModpackView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author Darkp
 */
public class Modpack {

    private final String Name;
    private final String Version;
    private final Image image;
    private final ArrayList<Image> Screenshots = new ArrayList<>();
    private final ModpackView view;
    private final Color titleColor;
    
    private int playersOnline = 0;
    private int maxPlayers = 20;
    private boolean installed = false;
    private boolean updated = false;
    private boolean serverOnline = true;
    private String News = "Patchnotes: \nLorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet,\"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet";
    
    public Modpack(String Name, String Version, Image image, Color color) {
        this.Name = Name;
        this.Version = Version;
        this.image = image;
        this.titleColor = color;
        
        try {
            File Images = new File(Config.getImagesFolder() + Name + File.separator);
            ArrayList<File> screenSFiles = new ArrayList<>();
            screenSFiles.addAll(Arrays.asList(Images.listFiles()));

            for (File file : screenSFiles) {
                Image img = new Image(file.toURI().toString());
                Screenshots.add(img);
                System.out.println("found Screenshot: " + file.toString());
            }

            if (Screenshots.isEmpty()) {
                Screenshots.add(new Image(getClass().getResource("/deincraftlauncher/Images/PlaceHolder_Image.jpg").toString()));
            }
        } catch(java.lang.NullPointerException ex) {
            System.out.println("cant load screenshots");
            Screenshots.add(new Image(getClass().getResource("/deincraftlauncher/Images/PlaceHolder_Image.jpg").toString()));
        }
        
        view = new ModpackView(this);
        
    }

    public String getName() {
        return Name;
    }

    public String getVersion() {
        return Version;
    }

    public Image getImage() {
        return image;
    }
    
    public void select() {
        ModpackSelector.getInstance().selectedPack = this;
        System.out.println("Selected Modpack: " + this.Name);
        showUp();
    }
    
    private void showUp() {
        view.setVisible(true);
    }
    
    public void hide() {
        view.setVisible(false);
    }

    public ArrayList<Image> getScreenshots() {
        return Screenshots;
    }
    
    public void reloadScreenshots() {
        
        System.out.println("reloading screenshots");
        
        File Images = new File(Config.getImagesFolder() + Name + File.separator);
        if (!Images.exists()) {
            Images.mkdirs();
        }
        ArrayList<File> screenSFiles = new ArrayList<>();
        screenSFiles.addAll(Arrays.asList(Images.listFiles()));
        
        Screenshots.clear();
        
        for (File file : screenSFiles) {
            Image img = new Image(file.toURI().toString());
            Screenshots.add(img);
            System.out.println("found Screenshot: " + file.toString());
        }
        
        if (Screenshots.isEmpty()) {
            System.out.println("couldnt find images");
            Screenshots.add(new Image(getClass().getResource("/deincraftlauncher/Images/PlaceHolder_Image.jpg").toString()));
        }
        
        getView().disableLoading();
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public int getPlayersOnline() {
        return playersOnline;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isInstalled() {
        return installed;
    }

    public boolean isUpdated() {
        return updated;
    }

    public String getNews() {
        return News;
    }
    
    public String getState() {
        if (updated) {
            return "Aktuelle Version installiert";
        } else if (installed){
            return "Update Verfügbar!";
        } else {
            return "Nicht Installiert";
        }
    }
    
    public String getServerState() {
        if (serverOnline) {
            return "Online";
        } else {
            return "Offline";
        }
    }

    public ModpackView getView() {
        return view;
    }
    
}
