/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Darkp
 */
public class InstallController implements Initializable {
    
    @FXML
    Label pathLabel;
    @FXML
    Button changePath;
    @FXML
    Button ContinueButton;
    @FXML
    TextField Usernamefield;
    @FXML
    PasswordField Passwordfield;
    @FXML
    Label loginLabel;
    @FXML
    Button loginButton;
    @FXML
    Button registerButton;
    @FXML
    AnchorPane MainPanel;
    
    private static InstallController instance = null;
    String targetPath = "";
    final File folder = new File(System.getProperty("user.home") + File.separator + "Deincraft");
    File folderLauncher = null;
    File folderGame = null;
    final File config = new File(System.getProperty("user.home") + File.separator + "Deincraft" + File.separator + "config.txt");
    File defaultInstall = new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\");
    String Username;
    String Password;
    boolean loggedin = false;
    String ConfigFile;
    
    
    private final Insets insets =  new Insets(-2, -2, -2, -3);
    private final int radius = 1;
    private final CornerRadii radii = new CornerRadii(
            radius, radius, radius, radius, radius, radius, radius, radius,
            false,  false,  false,  false,  false,  false,  false,  false
    );
    
    public static InstallController getInstance() {
        return instance;
    }
    
    @FXML
    public AnchorPane mainPanel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        
        System.out.println("Starting Installer UI...");
        
        pathLabel.setBackground(new Background(new BackgroundFill(Color.rgb(170, 170, 170), radii, insets)));
        ContinueButton.setBackground(new Background(new BackgroundFill(Color.rgb(190, 190, 200), radii, insets)));
        //ContinueButton.setBackground(new Background(new BackgroundFill(Color.rgb(100, 190, 100), radii, insets))); //Green Version
                
        setDefaultPath();
        System.out.println("starting done");
    }
    
    @FXML
    void onChangePath(ActionEvent event) {
        System.out.println("changing Path...");
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setInitialDirectory(defaultInstall);
        fileChooser.setTitle("Wähle Installationsort");
        File chosen = fileChooser.showDialog(deincraftlauncher.DeincraftLauncherUI.window);
        if (chosen != null) {
            String choosed = chosen.toString();
            System.out.println("Chosen Path: " + choosed);

            targetPath = choosed + File.separator + "Deincraft" + File.separator;
            pathLabel.setText(targetPath);
        } else {
            System.out.println("pathchange cancelled");
        }
        //fileChooser.showOpenDialog(deincraftlauncher.DeincraftLauncherUI.window);
    }
    
    @FXML
    void Continue() {
        
        if (!loggedin) {
            popupMessage("Fehler: Bitte zuerst erst anmelden");
            
        } else {
            
            System.out.println("Creating config file...");
            try {
                folder.mkdirs();
                config.createNewFile();
            } catch (IOException ex) {
                System.err.println("error creating config file: " + ex);
            }
            
            folderLauncher = new File(targetPath + "Launcher");
            System.out.println("Launcher: " + folderLauncher);
            folderLauncher.mkdirs();
            folderGame = new File(targetPath + "Games");
            folderGame.mkdirs();
            
            popupMessage("Installation abgeschlossen. Starte Deincraft Launcher...");
            mainPanel.setVisible(false);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(InstallController.class.getName()).log(Level.SEVERE, null, ex);
            }
            File installlocation = new File(targetPath);
            installlocation.mkdirs();
            saveConfig();
            String jarFile = getDCFile();
            String command = "java -jar " + jarFile;
            try {
                Process p = Runtime.getRuntime().exec ("cmd /C " + command);
            } catch (IOException ex) {
                Logger.getLogger(InstallController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 200);
            
        }
        
        System.out.println("Finishing setting dialog...");
    }
    
    @FXML
    void cancel() {
        System.out.println("cancelled");
        System.exit(0);
    }
    
    void setDefaultPath() {
        if (defaultInstall.exists()) {
            pathLabel.setText(defaultInstall.toString() + File.separator + "Deincraft" + File.separator);
        } else {
            pathLabel.setText(folder.toString());
            defaultInstall = folder;
        }
        
        targetPath = defaultInstall.toString() + File.separator + "Deincraft" + File.separator;
        System.out.println("targetpath=" + targetPath);
    }
    
    @FXML
    void KlickUserName(MouseEvent event) {
        System.out.println("Username klicked");
        Usernamefield.setText("");
    }
    
    @FXML
    void SelectUserName(ActionEvent event) {
        System.out.println("Username selected: " + Usernamefield.getText());
        Passwordfield.requestFocus();
        Username = Usernamefield.getText();
    }
    
    @FXML
    void SelectPW(ActionEvent event) {
        doLogin(null);
    }
    
    @FXML
    void doLogin(ActionEvent event) {
        
        System.out.println("Pressing Login");
        if (checkName(Usernamefield.getText()) || checkName(Passwordfield.getText())) {
            popupMessage("Bitte trage einen Namen und ein Passwort ein.");
            return;
        }
        System.out.println("Password selected: " + Passwordfield.getText());
        ContinueButton.requestFocus();
        Username = Usernamefield.getText();
        Password = Passwordfield.getText();
        
        loginButton.setVisible(false);
        registerButton.setVisible(false);
        loginLabel.setText("Eingeloggt als " + Username);
        loggedin = true;
        
        ContinueButton.setBackground(new Background(new BackgroundFill(Color.rgb(100, 190, 100), radii, insets))); //Green Version
        
    }
    
    private boolean checkName(String name) {
        return name.equals("") || name.equals("Nutzername") || name.contains(" ");
    }   
    
    @FXML
    void doRegister(ActionEvent event) {
        System.out.println("Pressing Register");
    }
    
    private void popupMessage(String text) {
        
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initOwner(deincraftlauncher.DeincraftLauncherUI.window);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(text));
        Scene dialogScene = new Scene(dialogVbox, 300, 40);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    private void saveConfig() {
        
        String newln = System.getProperty("line.separator");
        String Texts[] = new String[2];
        Texts[0] = "Deincraft Config 1.0";
        Texts[1] = targetPath;
        
        String toWrite = "";
        for (String Text : Texts) {
            toWrite += Text + newln;
        }
        
        try {
                Files.write(Paths.get(config.toString()), toWrite.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ex) {
                System.err.println(ex);
            }
    }
    
    public String getDCFile() {
        try {
            System.out.println("Launcherjar= " + new File(InstallController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toString());
            return new File(InstallController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toString();
        } catch (URISyntaxException ex) {
            Logger.getLogger(InstallController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
