/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import static deincraftlauncher.designElements.DesignHelpers.getSmallShadow;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Darkp
 */
public class ProgressBar {
    
    private static final int arc = 5;
    
    private final float posX;
    private float posY;
    private final float sizeX;
    private final float sizeY;
    private final Rectangle left;
    private final Rectangle right;
    private final ArrayList<Node> Nodes = new ArrayList<>();
    
    private float Progress;
    
    public ProgressBar(double posX, double posY, double sizeX, double sizeY) {
        
        this.posX = (float) posX;
        this.posY = (float) posY;
        this.sizeX = (float) sizeX;
        this.sizeY = (float) sizeY;
        
        
        left = new Rectangle();
        left.setFill(Color.GREENYELLOW);
        left.setLayoutX(posX);
        left.setLayoutY(posY);
        left.setWidth(2);
        left.setHeight(sizeY);
        //left.setEffect(getSmallShadow());
        left.setArcHeight(arc);
        left.setArcWidth(arc);
        Nodes.add(left);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(left);
        
        right = new Rectangle();
        right.setLayoutX(posX);
        right.setLayoutY(posY);
        right.setWidth(sizeX);
        right.setHeight(sizeY);
        right.setFill(Color.RED);
        //right.setEffect(getSmallShadow());
        right.setArcHeight(arc);
        right.setArcWidth(arc);
        Nodes.add(right);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(right);
        
        System.out.println("creating progress bar " + this.toString());
        
    }
    
    public void setProgress(float progress) {       //in %
        
        if (progress > 1) {
            progress = 1;
        }
        
        this.Progress = progress;
        
        left.setWidth(sizeX * progress + 2);
        right.setLayoutX(posX + sizeX * progress);
        right.setWidth(sizeX * (1- progress));
        
    }

    @Override
    public String toString() {
        return "ProgressBar{" + "posX=" + posX + ", posY=" + posY + ", sizeX=" + sizeX + ", sizeY=" + sizeY + ", left=" + left + ", right=" + right + ", Progress=" + Progress + '}';
    }

    public ArrayList<Node> getNodes() {
        return Nodes;
    }
    
    public void setVisible(boolean visible) {
        if (visible) {
            left.setVisible(true);
            right.setVisible(true);
        } else {
            right.setVisible(false);
            left.setVisible(false);
        }
    }
    
    public void setLayoutY(float pos) {
        this.posY = pos;
        
        left.setLayoutY(pos);
        right.setLayoutY(pos);
    }
    
}
