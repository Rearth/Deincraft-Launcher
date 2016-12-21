/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 *
 * @author Darkp
 */
public class DesignHelpers {
    
    public static final int defaultgap = 3;
    public static final Color colorGray = Color.rgb(30, 30, 30, 0.4);
    
    private static final int shadowsize = 20;
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
            
    
}
