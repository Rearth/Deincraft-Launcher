/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Darkp
 */
public class DeincraftLauncherUI extends Application {
    
    public static Stage window;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        System.out.println("starting DC Launcher");
        
        Parent root;
        if (FirstTime()) {
            root = FXMLLoader.load(getClass().getResource("FXMLInstall.fxml"));
        } else {
        root = FXMLLoader.load(getClass().getResource("FXMLSheet.fxml"));
        }
        
        Scene scene = new Scene(root);
        
        
        scene.getStylesheets().add(getClass().getResource("Main_Design.css").toExternalForm());
        
        window = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/deincraftlauncher/Images/IconA.png")));
        stage.setScene(scene);
        stage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting Deincraft Launcher...");
        launch(args);
    }
    
    private boolean FirstTime() {
        System.out.println("checking for first time");
        
        String UserDir = System.getProperty("user.home");
        System.out.println("Userdirectory = " + UserDir);
        
        File file = new File(UserDir + File.separator + "Minefactory" + File.separator + "config.txt");
        System.out.println("config path: " + UserDir + File.separator + "Minefactory");
        System.out.println("first time: " + !file.exists());
        return !file.exists();
    }
    
}
