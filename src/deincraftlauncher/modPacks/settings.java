/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.modPacks;

import deincraftlauncher.Config;
import deincraftlauncher.designElements.DesignHelpers;
import deincraftlauncher.designElements.TextButton;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
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
    private static final int infoLeftX = 20;
    private static final int infoLeftBX = 70;
    private static final int userNameY = 80;
    private static final int RAMY = 120;
    private static final int saveY = 214;
    private static final int saveX = sizeX - TextButton.getWidth("speichern") - 6;
    private static final int cancelX = saveX - 6 - TextButton.getWidth("cancel");
    
    private static TextField editRam;

    public String Username = "error";
    public String Password = "error";
    public String NickName = "error";
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
        return "settings{" + "Username=" + Username + ", Password=" + Password + ", NickName=" + NickName + ", RAM=" + RAM + '}';
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
        
        TextButton cancel = new TextButton(cancelX, saveY, "cancel", Color.RED, background);
        cancel.setOnClick((TextButton tile) -> {
                handleCancel(dialog);
            });
        
        TextButton save = new TextButton(saveX, saveY, "Speichern", Color.GREEN, background);
        save.setOnClick((TextButton tile) -> {
                handleSave(dialog);
            });
        
        TextButton openfolder = new TextButton(saveX - 20, separatorY + separatorSizeY + 3, "Open folder", Color.DARKCYAN, background);
        openfolder.setOnClick((TextButton tile) -> {
                handleOpen(dialog);
            });
        
        Label Title = new Label();
        Title.setText("Optionen");
        Title.setPrefSize(sizeX, separatorY);
        Title.setTextFill(Color.WHITESMOKE);
        Title.setTextAlignment(TextAlignment.CENTER);
        Title.setFont(DesignHelpers.getFocusFont(42));
        Title.setAlignment(Pos.CENTER);
        background.getChildren().add(Title);
        
        Label usernameInfoA = new Label();
        usernameInfoA.setText("User:");
        usernameInfoA.setLayoutX(infoLeftX);
        usernameInfoA.setLayoutY(userNameY);
        usernameInfoA.setTextFill(Color.WHITE);
        background.getChildren().add(usernameInfoA);
        
        Label usernameInfo = new Label();
        usernameInfo.setText(settings.getUsername());
        usernameInfo.setLayoutX(infoLeftBX);
        usernameInfo.setLayoutY(userNameY);
        usernameInfo.setTextFill(Color.WHITE);
        background.getChildren().add(usernameInfo);
        
        Label RamField = new Label();
        RamField.setText("RAM: ");
        RamField.setLayoutX(infoLeftX);
        RamField.setLayoutY(RAMY);
        RamField.setTextFill(Color.WHITE);
        background.getChildren().add(RamField);
        
        editRam = new TextField();
        editRam.setText(Integer.toString(settings.getRAM()));
        editRam.setLayoutX(infoLeftBX);
        editRam.setLayoutY(RAMY);
        editRam.setPrefWidth(100);
        background.getChildren().add(editRam);
        
        Scene dialogScene = new Scene(background, sizeX, sizeY);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    private static void handleSave(Stage stage) {
        System.out.println("saving settings");
        int readRAM = Integer.valueOf(editRam.getText());
        System.out.println("changed RAM to: " + readRAM + " (from):" + instance.RAM);
        instance.RAM = readRAM;
        settings.save();
        stage.close();
    }
    
    
    private static void handleCancel(Stage stage) {
        System.out.println("cancelling settings");
        stage.close();
    }
    
    private static void handleOpen(Stage stage) {
        try {
            Desktop.getDesktop().open(new File(Config.getGameFolder()));
        } catch (IOException ex) {
            Logger.getLogger(settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
