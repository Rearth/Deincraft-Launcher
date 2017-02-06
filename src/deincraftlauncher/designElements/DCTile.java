/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import deincraftlauncher.FXMLSheetController;
import static deincraftlauncher.designElements.DesignHelpers.getShadowEffect;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class DCTile {

    private static final double scaleTo = 1.1;
    
    private final ImageView imageLabel;
    private Function onClick;
    private Function onFocus;
    
    private int posX;
    private int posY;
    private int height;
    private Image image;
    private int width;
    private String Text;
    private boolean focused = false;
    private String Name = "";
    private boolean focusable = true;
    
    public DCTile(int posX, int posY, int width, int height, Image imagefile, Pane pane) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.image = imagefile;
        this.width = width;
        this.Text = Text;
        
        
        imageLabel = new ImageView();
        if (imagefile != null) {
            imageLabel.setImage(image);
            imageLabel.setLayoutX(posX);
            imageLabel.setLayoutY(posY);
            imageLabel.setFitHeight(height);
            imageLabel.setFitWidth(width);
            pane.getChildren().add(imageLabel);
            imageLabel.setVisible(true);
            
            imageLabel.setOnMouseExited((MouseEvent e) -> {
                handleHover(false, e);
            });
            imageLabel.setOnMouseEntered((MouseEvent e) -> {
                handleHover(true, e);
            });
            imageLabel.setOnMouseClicked((MouseEvent e) -> {
                handleClick(e);
            });
        }
        
        onClick = (DCTile tile) -> this.noClick();
        onFocus = (DCTile tile) -> this.noClick();
        
    }
    
    private void noClick() {
        System.out.println("no click handler set for: " + this.toString());
    }
    
    public DCTile(int posX, int posY, int size, Image imagefile, Pane pane) {
        this (posX, posY, size, size, imagefile, pane);
        
    }
    
    public DCTile(int posX, int posY, Image imagefile, Pane pane) {
        this (posX, posY, (int) imagefile.getWidth(), (int) imagefile.getHeight(), imagefile, pane);
        
    }
    
    public DCTile(int posX, int posY, Pane pane) {
        this (posX, posY, 0, 0, null, pane);
        
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
        imageLabel.setLayoutX(posX);
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
        imageLabel.setLayoutY(posY);
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public void setImage(Image image) {
        if (image != null) {
            imageLabel.setImage(image);
        }
        
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }
    
    public void setVisible(boolean state) {
        imageLabel.setVisible(state);
    }

    @Override
    public String toString() {
        return "DCTile{" + "posX=" + posX + ", posY=" + posY + ", height=" + height + ", image=" + image + ", width=" + width + ", Text=" + Text + '}';
    }
    
    private boolean AnimPlaying = false;
    
    public void handleHover(boolean entered, MouseEvent e) {
        
        //System.out.println("tile:" + entered);
        
        /*if (AnimPlaying || inTile(e)) {
            //System.out.println("handling hover: not handling; playing=" + AnimPlaying + " inTile=" + inTile(e));
            return;
        }
        
        if (entered && !focused) {
            System.out.println("Entered Tile");
            startTimer();
            focusAnim(1.0, scaleTo, 120);
            focused = true;
            onFocus.call(this);
        } else if (focused) {
            System.out.println("Left tile");
            focusAnim(scaleTo, 1.0, 120);
            focused = false;
        }*/
        
        if (!focusable) {
            return;
        }
        
        if (entered) {
            startTimer();
            focusAnim(1.0, scaleTo, 120);
            focused = true;
            onFocus.call(this);
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
        node.add(imageLabel);
        
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
    
    private boolean inTile(MouseEvent p) {
        
        Point e = new Point((int) p.getSceneX(), (int) p.getSceneY());
                
        return inTile(e);
    }
    
    private boolean inTile(Point e) {
        //System.out.println("x=" + e.x + " posX=" + (posX - 2) + " ; e.x > posX - 2:" + (e.x > (posX - 2)));
        if (e.x > (posX + 2) && e.x < posX + width - 3 && e.y > posY + 1 && e.y < posY + height - 2) {
            return true;
        }
        
        return false;
    }
    
    public void setBackgroundColor(Color color) {
        
        if (color.equals(Color.TRANSPARENT)) {
            imageLabel.setEffect(getShadowEffect());
        }
        
    }
    
    @FunctionalInterface
    public interface Function {
        void call(DCTile tileClicked);
    }
    
    public void setOnClick(final Function function) {
        onClick = function;
    }
    
    public void setOnFocus(final Function function) {
        onFocus = function;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setShadow(boolean state) {
        //removeThis
    }
    
    public ArrayList<Node> getNodes() {
        ArrayList<Node> Nodes = new ArrayList<>();
        Nodes.add(imageLabel);
        
        return Nodes;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }
    
}
