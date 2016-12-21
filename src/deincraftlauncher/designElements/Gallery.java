/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.designElements;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Darkp
 */
public class Gallery {

    private static final int timerTime = 2000;
    private static final float circleR = 10;
    private static final float marginCircle = 17;
    private static final int bottomGap = 0;
    
    private final int posX;
    private final int posY;
    private final int sizeX;
    private final int sizeY;
    private final ArrayList<Image> Images = new ArrayList<>();
    private final ImageView mainView;
    private final ArrayList<Node> Nodes = new ArrayList<>();
    private final Circle selected = new Circle();
    private final Rectangle grayBack;
    private final ArrayList<Circle> Circles = new ArrayList<>();
    
    private Timer timer = new Timer();
    
    
    private boolean shown = false;
    private int curIndex = 0;
    
    public Gallery(int posX, int posY, int sizeX, int sizeY) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        
        mainView = new ImageView();
        mainView.setLayoutX(posX);
        mainView.setLayoutY(posY);
        mainView.setFitWidth(sizeX);
        mainView.setFitHeight(sizeY);
        mainView.setEffect(DesignHelpers.getSmallShadow());
        Nodes.add(mainView);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(mainView);
        
        grayBack = new Rectangle();
        grayBack.setX(posX);
        grayBack.setY(posY + sizeY - circleR - marginCircle * 1.7 - bottomGap);
        grayBack.setHeight(circleR + marginCircle * 1.7);
        grayBack.setWidth(sizeX);
        grayBack.setFill(Color.rgb(30, 30, 30, 0.6));
        System.out.println(grayBack);
        Nodes.add(grayBack);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(grayBack);
    }
    
    public void addImage(Image image) {
        Images.add(image);
    }
    
    public void clearImages() {
        Images.clear();
    }
    
    public void addImage(ArrayList<Image> List) {
        Images.addAll(List);
    }
    
    public void show() {
        shown = true;
        
        int imagesNum = Images.size();
        float totalSize = circleR * imagesNum + marginCircle * (imagesNum - 1);
        
        selected.setCenterX(posX + sizeX / 2 - totalSize / 2 + (circleR + marginCircle) * 0);
        selected.setCenterY(posY + sizeY - circleR - marginCircle / 2 - bottomGap);
        selected.setRadius(circleR * 0.7);
        selected.setFill(Color.WHITE);
        Nodes.add(selected);
        deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(selected);
        
        
        for (int i = 0; i < Images.size(); i++) {
            Circle cirle = new Circle();
            cirle.setCenterX(posX + sizeX / 2 - totalSize / 2 + (circleR + marginCircle) * i);
            cirle.setCenterY(posY + sizeY - circleR - marginCircle / 2 - bottomGap);
            cirle.setRadius(circleR);
            cirle.setStroke(Color.WHITE);
            cirle.setStrokeWidth(1);
            cirle.setFill(Color.TRANSPARENT);
            Nodes.add(cirle);
            Circles.add(cirle);
            
            deincraftlauncher.FXMLSheetController.getInstance().mainPanel.getChildren().add(cirle);
            
        }
        
        if (Images.isEmpty()) {
            return;
        }
        mainView.setImage(Images.get(0));
        if (timeron) {
            return;
        }
        nextImage();
    }
    
    private boolean timeron = false;
    
    public void startTimer() {
        
        if (timeron) {
            return;
        }
        timeron = true;
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextImage();
            }
        }, timerTime);
    }
    
    public void hide() {
        shown = false;
        for (Node node : Nodes) {
            node.setVisible(false);
        }
    }
    
    public void setVisible() {
        shown = true;
        for (Node node : Nodes) {
            node.setVisible(true);
        }
    }
    
    public ArrayList<Node> getNodes() {
        return Nodes;
    }
    
    private void nextImage() {
        
        //System.out.println("image timer down" + this);
        
        timeron = false;
        if (mainView.visibleProperty().get() && shown) {
            if (timeron) {
                return;
            }
            timeron = true;
            mainView.setImage(Images.get(curIndex));
            
            curIndex++;
            if (curIndex >= Images.size()) {
                curIndex = 0;
            }
                   
            
            selected.setCenterX(Circles.get(curIndex).getCenterX());
            
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    nextImage();
                }
            }, timerTime);
        }
        
    }
}
