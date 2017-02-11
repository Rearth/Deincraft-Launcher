/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Darkp
 */
public class DesignHelpers {  
    
    public static final int defaultgap = 13;
    public static final Color foreGround = Color.rgb(40, 80, 140);
    public static final Color backGround = Color.rgb(37, 42, 51);
    
    private static final int shadowsize = 4;
    private static final double darkencolor = 0.5;
    
    public static Effect getShadowEffect() {
        
        return getShadowEffect(shadowsize);
    }
    
    public static Effect getSmallShadow() {
        
        return getShadowEffect((int) (shadowsize * 0.5));
        
    }
    
    public static Effect getShadowEffect(int size) {
        
        DropShadow totalShadow = new DropShadow();
        totalShadow.setOffsetY(shadowsize);
        totalShadow.setOffsetX(shadowsize);
        //totalShadow.setRadius(size);
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
        return Font.loadFont(DesignHelpers.class.getResource("/deincraftlauncher/Images/Elemental_End.ttf").toString(), 48);
    }
    
    public static Font getLabelFont() {
        return Font.loadFont(DesignHelpers.class.getResource("/deincraftlauncher/Images/Elemental_End.ttf").toString(), 11);
    }
    
    public static Font getTextFont(int size) {
        return Font.loadFont(DesignHelpers.class.getResource("/deincraftlauncher/Images/Elemental_End.ttf").toString(), size);
    }
    
    public static  Font getFocusFont() {
        return new DesignHelpers().getFocusFontHere();
    }
    
    public static  Font getFocusFont(int size) {
        return new DesignHelpers().getFocusFontHere(size);
    }
    
    private Font getFocusFontHere() {
        return Font.loadFont(getClass().getResource("/deincraftlauncher/Images/Elemental_End.ttf").toString(), 24);
    }
    
    private Font getFocusFontHere(int size) {
        return Font.loadFont(getClass().getResource("/deincraftlauncher/Images/Elemental_End.ttf").toString(), size);
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
    
    public static Font getNotesFont() {
        return Font.loadFont(DesignHelpers.class.getResource("/deincraftlauncher/Images/Elemental_End.ttf").toString(), 15);
    }   

    public static void popupMessage(String text) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Benachrichtigung:");
        alert.setContentText(text);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initOwner(deincraftlauncher.DeincraftLauncherUI.window);
        alert.show();
    }    
    
    public static void popupMessage(String text, String Link) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Benachrichtigung:");
        alert.setContentText(text);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initOwner(deincraftlauncher.DeincraftLauncherUI.window);
        alert.showAndWait();
        
        try {
            Desktop.getDesktop().browse(new URL(Link).toURI());
        } catch (MalformedURLException ex) {
            Logger.getLogger(DesignHelpers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(DesignHelpers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
}
