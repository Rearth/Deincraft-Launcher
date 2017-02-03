/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher;

import deincraftlauncher.IO.MCAuthentication;
import static deincraftlauncher.IO.ZIPExtractor.extractArchive;
import deincraftlauncher.designElements.DesignHelpers;
import deincraftlauncher.designElements.TextButton;
import deincraftlauncher.modPacks.settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;

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
    TextField Usernamefield;
    @FXML
    PasswordField Passwordfield;
    @FXML
    Label loginLabel;
    @FXML
    public AnchorPane mainPanel;
    
    private static InstallController instance = null;
    private static final int arc = 10;
    private static final Color blueColor = Color.DARKBLUE;
    private static final int separatorY = 60;
    private static final int separatorSizeY = 5;
    private TextButton save;
    private TextButton login;
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
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        
        System.out.println("Starting Installer UI...");
        
        int saveX = 393;
        int cancelY = 165;
        int saveY = 200;
        int sizeX = 481;
        
        Rectangle separator = new Rectangle();
        separator.setWidth(sizeX);
        separator.setHeight(separatorSizeY);
        separator.setLayoutY(separatorY);
        separator.setFill(blueColor);
        mainPanel.getChildren().add(separator);
        mainPanel.setBackground(Background.EMPTY);
                
        TextButton cancel = new TextButton(saveX, cancelY, "cancel", Color.RED, mainPanel);
        cancel.setOnClick((TextButton tile) -> {
                cancel();
            });
        
        save = new TextButton(saveX, saveY, "Weiter", Color.GRAY, mainPanel);
        save.setFocusable(false);
        save.setOnClick((TextButton tile) -> {
                Continue();
            });
        
        login = new TextButton(14, 151, "Login", Color.BLUE, mainPanel);
        login.setOnClick((TextButton tile) -> {
                doLogin(null);
            });
        
        Label Title = new Label();
        Title.setText("Installer");
        Title.setPrefSize(sizeX, separatorY);
        Title.setTextFill(Color.WHITESMOKE);
        Title.setTextAlignment(TextAlignment.CENTER);
        Title.setFont(DesignHelpers.getFocusFont(42));
        Title.setAlignment(Pos.CENTER);
        mainPanel.getChildren().add(Title);
        
        pathLabel.setBackground(new Background(new BackgroundFill(Color.rgb(170, 170, 170), radii, insets)));
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
            
            settings.setPassword(Password);
            settings.setUsername(Username);
            settings.setRAM(getDefaultRam());
            settings.save();
            
            /*folderLauncher = new File(targetPath + "Launcher");
            System.out.println("Launcher: " + folderLauncher);
            folderLauncher.mkdirs();
            folderGame = new File(targetPath + "Games");
            folderGame.mkdirs();*/
            createGameDir(targetPath);
            
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
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 4000);
            try {
                Process p = Runtime.getRuntime().exec ("cmd /C " + command);
                BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                try {
                        while ((line = stdout.readLine()) != null) {
                                System.out.println(line);
                        }
                } catch (IOException e) {
                    System.err.println(e);
                }
            } catch (IOException ex) {
                Logger.getLogger(InstallController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
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
    }
    
    @FXML
    void SelectUserName(ActionEvent event) {
        System.out.println("Username selected: " + Usernamefield.getText());
        Passwordfield.requestFocus();
        Username = Usernamefield.getText();
    }
    
    @FXML
    void SelectPW(ActionEvent event) {
        login.playAnim();
        doLogin(null);
    }
    
    void doLogin(ActionEvent event) {
        
        System.out.println("Pressing Login");
        if (checkName(Usernamefield.getText()) || checkName(Passwordfield.getText())) {
            popupMessage("Bitte trage einen Namen und ein Passwort ein.");
            return;
        }
        
        System.out.println("attempting login as:" + Usernamefield.getText() + " pw:" + Passwordfield.getText());
        
        boolean validData = MCAuthentication.isValidLogin(Usernamefield.getText(), Passwordfield.getText());
        if (!validData) {
            popupMessage("Ungültige Anmeldedaten");
            return;
        }
        
        System.out.println("Password selected: " + Passwordfield.getText());
        Username = Usernamefield.getText();
        Password = Passwordfield.getText();
        
        login.setVisible(false);
        
        loginLabel.setText("Eingeloggt als " + Username);
        loggedin = true;
        
        save.setFocusable(true);
        save.setColor(Color.GREEN);
        
    }
    
    private boolean checkName(String name) {
        return name.equals("") || name.equals("Nutzername") || name.contains(" ");
    }
    
    private void popupMessage(String text) {
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Benachrichtigung:");
        alert.setContentText(text);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initOwner(deincraftlauncher.DeincraftLauncherUI.window);
        alert.show();
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
    
    private int getDefaultRam() {
        int RAM;
        long memorySize = ((com.sun.management.OperatingSystemMXBean) 
        ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        long RAMinMB = memorySize / 1024 /1024; 
        if (RAMinMB >= 8000) {
            RAM = 4096;
        } else if (RAMinMB >= 6000) {
            RAM = 2572;
        } else if (RAMinMB >= 4000) {
            RAM = 2048;
        } else {
            RAM = 1536;
        }
        
        return RAM;
    }

    private void createGameDir(String Path) {
        
        System.out.println("filling DC Dir");
        
        new File(Path).mkdirs();
        try {
            URL inputUrl = getClass().getResource("/deincraftlauncher/Images/DCInstall.zip");
            String zipFile = Path + "DCInstall.zip";
            File dest = new File(zipFile);
            FileUtils.copyURLToFile(inputUrl, dest);
            extractArchive(zipFile, Path);
        } catch (Exception ex) {
            System.err.println("unable to create game directory " + ex);
            ex.printStackTrace();
        }
        
        
    }
    
}
