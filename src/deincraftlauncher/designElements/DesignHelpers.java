/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import deincraftlauncher.IO.download.DownloadHandler;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Darkp
 */
public class DesignHelpers {  
    
    public static final int defaultgap = 13;
    public static final Color colorGray = Color.rgb(30, 30, 30, 0.4);
    public static final Color foreGround = Color.rgb(40, 80, 154);
    public static final Color backGround = Color.rgb(37, 42, 51);
    
    private static final int shadowsize = 0;
    private static final double darkencolor = 0.5;
    
    public static Effect getShadowEffect() {
        
        return getShadowEffect(shadowsize);
    }
    
    public static Effect getSmallShadow() {
        
        return getShadowEffect((int) (shadowsize * 0.5));
        
    }
    
    public static Effect getShadowEffect(int size) {
        
        DropShadow totalShadow = new DropShadow();
        totalShadow.setRadius(size);
        totalShadow.setColor(Color.web("0x243642"));
        
        return totalShadow;
    }
    
    public static Color brighter(Color color) {
        
        System.out.println("darkening color: " + color + " r=" + color.getRed());
        
        int red = (int) (color.getRed() * 255 + (255 - color.getRed() * 255) * darkencolor);
        int green = (int) (color.getGreen() * 255 + (255 - color.getGreen() * 255) * darkencolor);
        int blue = (int) (color.getBlue() * 255 + (255 - color.getBlue() * 255) * darkencolor);
        
        return Color.rgb(red, green, blue);
        
    }
    
    public static Font getTitleFont() {
        return Font.font("Verdana", FontWeight.BOLD, 48);
    }
    
    public static Font getLabelFont() {
        return Font.font("Roboto", FontWeight.BOLD,  14);
    }
    
    public static  Font getFocusFont() {
        return new DesignHelpers().getFocusFontHere();
    }
    
    public static  Font getFocusFont(int size) {
        return new DesignHelpers().getFocusFontHere(size);
    }
    
    private Font getFocusFontHere() {
        return Font.loadFont(getClass().getResource("/deincraftlauncher/Images/Minecrafter.ttf").toString(), 24);
    }
    
    private Font getFocusFontHere(int size) {
        return Font.loadFont(getClass().getResource("/deincraftlauncher/Images/Minecrafter.ttf").toString(), size);
    }
    
    public static void playFadeAnim(Node node, boolean hide) {
        
        FadeTransition ft = new FadeTransition(Duration.millis(250), node);
        if (hide) {
            ft.setFromValue(1.0F);
            ft.setToValue(0F);
        } else {
            ft.setFromValue(0F);
            ft.setToValue(1F);            
        }
        ft.setCycleCount(1);
        ft.setAutoReverse(true);
        ft.play();
    }
    
    public static void popupMessage(String Text) {
        Platform.runLater(() -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.initOwner(deincraftlauncher.DeincraftLauncherUI.window);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text(Text));
            Scene dialogScene = new Scene(dialogVbox, 300, 40);
            dialog.setScene(dialogScene);
            dialog.show();               
        });
        
    }
    
    public static Font getNotesFont() {
        return Font.font("Roboto", FontWeight.SEMI_BOLD,  16);
    }       
    
}
