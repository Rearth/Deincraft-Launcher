/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.modPacks;

import deincraftlauncher.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Darkp
 */
public class settings implements Serializable {
    
    private static final int arc = 10;
    private static final int sizeX = 400;
    private static final int sizeY = 250;
    private static final Color backgroundColor = Color.rgb(50, 50, 60);
    private static final Color blueColor = Color.DARKBLUE;
    private static final int separatorY = 60;
    private static final int separatorSizeY = 5;
    private static final int saveY = 225;
    private static final int saveX = 340;
    private static final int cancelX = 280;
    
    public String Username = "error";
    public String Password = "error";
    public int RAM = 4096;
    
    private settings() {
        
    }
    
    private static settings instance = new settings();
    
    public static settings getInstance() {
        return instance;
    }
    
    public static void save() {
        
        File existing = new File(Config.getLauncherFolder());
        if (!existing.exists()) {
            existing.mkdirs();
        }
        
        existing = new File(Config.getSettingFile());
        if (!existing.exists()) {
            try {
                existing.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(settings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(Config.getSettingFile()))) {

            System.out.println("saving config " + instance);
            
            ois.writeObject(instance);

        } catch (Exception ex) {
            System.err.println("Error saving settings: " + ex);
        }
        
    }
    
    public static void loadFromFile() {
        
        System.out.println("Starting to read settings from file");

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Config.getSettingFile()))) {

            instance = (settings) ois.readObject();

        } catch (Exception ex) {
            System.err.println("Error loading settings: " + ex);
        }
        
        System.out.println("Read settings file" + instance.toString());
    }

    @Override
    public String toString() {
        return "settings{" + "Username=" + Username + ", Password=" + Password + ", RAM=" + RAM + '}';
    }

    public static String getUsername() {
        return instance.Username;
    }

    public static String getPassword() {
        return instance.Password;
    }

    public static int getRAM() {
        return instance.RAM;
    }

    public static void setUsername(String Username) {
        instance.Username = Username;
    }

    public static void setPassword(String Password) {
        instance.Password = Password;
    }

    public static void setRAM(int RAM) {
        instance.RAM = RAM;
    }
    
    public static void showWindow() {
        
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initOwner(deincraftlauncher.DeincraftLauncherUI.window);
        
        AnchorPane background = new AnchorPane();
        
        Rectangle bBlack = new Rectangle();
        bBlack.setWidth(sizeX);
        bBlack.setHeight(sizeY);
        bBlack.setFill(backgroundColor);
        bBlack.setArcHeight(arc);
        bBlack.setArcWidth(arc);
        background.getChildren().add(bBlack);
        
        Rectangle separator = new Rectangle();
        separator.setWidth(sizeX);
        separator.setHeight(separatorSizeY);
        separator.setLayoutY(separatorY);
        separator.setFill(blueColor);
        background.getChildren().add(separator);
        
        Label save = new Label();
        save.setText("Speichern");
        save.setTextFill(Color.WHITESMOKE);
        save.setLayoutX(saveX);
        save.setLayoutY(saveY);
        save.setStyle("-fx-border-color:  rgba(0, 0, 139, 1); -fx-border-radius: 5; -fx-border-width: 2;");
        background.getChildren().add(save);
        save.setOnMouseClicked((MouseEvent e) -> {
                handleSave(dialog);
            });
        
        Scene dialogScene = new Scene(background, sizeX, sizeY);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    private static void handleSave(Stage stage) {
        stage.close();
    }
    
}
