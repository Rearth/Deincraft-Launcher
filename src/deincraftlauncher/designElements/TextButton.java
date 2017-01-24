/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import deincraftlauncher.FXMLSheetController;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 *
 * @author Darkp
 */
public class TextButton {
    
    private static final Font font = DesignHelpers.getFocusFont(18);
    private static final java.awt.Font fonthere = new java.awt.Font("Roman", java.awt.Font.BOLD, 23);
    private static final double scaleTo = 1.08;
    private static final Color textColor = Color.WHITE;
    
    private final Label textLabel;
    private final Rectangle background;
    private Function onClick;
    
    private int posX;
    private int posY;
    private int height;
    private int width;
    private String Text;
    private boolean focused = false;    
    private final Color Button;
    private boolean focusable = true;
    
    public TextButton(int posX, int posY, String Text, Color color, Pane pane) {
        this.posX = posX;
        this.posY = posY;
        this.Text = Text;
        this.Button = color;
        
        
        //this.height = (int) (textLabel.getPrefHeight() * 1.1);
        //this.width = (int) (textLabel.getPrefWidth() * 1.1);   
        System.out.println("dims: " + height + " | " + width);
        //textLabel.setPrefSize(width, height);
        width = (int) (calcWidth(Text) * 1.05);
        height = (int) (30 * 1.05);
        
        textLabel = new Label();
        textLabel.setText(Text);
        textLabel.setLayoutX(posX);
        textLabel.setLayoutY(posY);
        textLabel.setFont(font);
        textLabel.setTextFill(textColor);
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.setAlignment(Pos.CENTER);
        
        textLabel.setPrefSize(width, height);
        
        background = new Rectangle();
        background.setFill(Button);
        background.setLayoutX(posX);
        background.setLayoutY(posY);
        background.setHeight(height);
        background.setWidth(width);
        background.setStyle("-fx-border-radius: 6; -fx-border-width: 6;");
        background.setEffect(DesignHelpers.getShadowEffect());
        
        pane.getChildren().add(background);
        pane.getChildren().add(textLabel);

        textLabel.setOnMouseExited((MouseEvent e) -> {
            handleHover(false, e);
        });
        textLabel.setOnMouseEntered((MouseEvent e) -> {
            handleHover(true, e);
        });
        textLabel.setOnMouseClicked((MouseEvent e) -> {
            handleClick(e);
        });
        
        onClick = (TextButton tile) -> this.noClick();
        
    }
    
    private void noClick() {
        System.out.println("no click handler set for: " + this.toString());
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
        textLabel.setText(Text);
    }
    
    public void setVisible(boolean state) {
        textLabel.setVisible(state);
        background.setVisible(state);
    }

    @Override
    public String toString() {
        return "TextButton{" + "textLabel=" + textLabel + ", background=" + background + ", onClick=" + onClick + ", posX=" + posX + ", posY=" + posY + ", height=" + height + ", width=" + width + ", Text=" + Text + ", focused=" + focused + ", Color=" + Button + ", focusable=" + focusable + ", AnimPlaying=" + AnimPlaying + '}';
    }
    
    private boolean AnimPlaying = false;
    
    public void handleHover(boolean entered, MouseEvent e) {
        
        
        if (!focusable) {
            return;
        }
        
        if (entered) {
            //startTimer();
            focusAnim(1.0, scaleTo, 120);
            focused = true;
        } else {
            focusAnim(scaleTo, 1.0, 120);
            focused = false;
            
        }
        
    }
    
    public void setFocused(boolean state) {
        
        if (AnimPlaying) {
            return;
        }
        
        if (state && !focused) {
            System.out.println("Focusing Tile");
            focusAnim(1.0, scaleTo, 120);
            focused = true;
        } else if (focused) {
            System.out.println("Unfocusing Tile");
            focusAnim(scaleTo, 1.0, 120);
            focused = false;
        }
    }
    
    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Tile timer down");
                timerup();
            }
        }, 250);
        
    }
    
    private void timerup() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        p = new Point((int) FXMLSheetController.getInstance().mainPanel.screenToLocal(p.x, p.y).getX(), (int) FXMLSheetController.getInstance().mainPanel.screenToLocal(p.x, p.y).getY());
        //System.out.println("point=" + p);
        if (!inTile(p)) {
            if (focused) {
                //System.out.println("Unfocusing tile: Timer out");
                focusAnim(scaleTo, 1.0, 120);
                focused = false;
            }
        } else {
            startTimer();
        }
    }
    
    private void focusAnim(double from, double to, int time) {
        
        AnimPlaying = true;
        
        ArrayList<Node> node = new ArrayList<>();
        node.add(textLabel);
        node.add(background);
        
        for (Node Elem : node) {
            ScaleTransition scaleanim = new ScaleTransition(Duration.millis(time), Elem);
            scaleanim.setFromX(from);
            scaleanim.setToX(to);
            scaleanim.setFromY(from);
            scaleanim.setToY(to);
            scaleanim.setCycleCount(1);
            scaleanim.setAutoReverse(true);
            scaleanim.play();
        }
        
    }
    
    private void handleClick(MouseEvent e) {
        //System.out.println("pressed Tile: " + getText());
        onClick.call(this);
    }
    
    private boolean inTile(Point e) {
        //System.out.println("x=" + e.x + " posX=" + (posX - 2) + " ; e.x > posX - 2:" + (e.x > (posX - 2)));
        if (e.x > (posX + 2) && e.x < posX + width - 3 && e.y > posY + 1 && e.y < posY + height - 2) {
            return true;
        }
        
        return false;
    }
    
    @FunctionalInterface
    public interface Function {
        void call(TextButton tileClicked);
    }
    
    public void setOnClick(final Function function) {
        onClick = function;
    }
    
    public ArrayList<Node> getNodes() {
        ArrayList<Node> Nodes = new ArrayList<>();
        Nodes.add(textLabel);
        Nodes.add(background);
        
        return Nodes;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }
    
    private static int calcHeight(String Text) {
        
        AffineTransform affinetransform = new AffineTransform();     
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
        return (int)(fonthere.getStringBounds(Text, frc).getHeight() * 1.1);
    }
    
    private static int calcWidth(String Text) {
        AffineTransform affinetransform = new AffineTransform();     
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);  
        return (int)(fonthere.getStringBounds(Text, frc).getWidth() * 1.1);
    }
    
    public static int getWidth(String Text) {
        AffineTransform affinetransform = new AffineTransform();     
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);  
        return (int)(fonthere.getStringBounds(Text, frc).getWidth() * 1.1 * 1.05);
    }
}
