/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.news;

import deincraftlauncher.Config;
import deincraftlauncher.FXMLSheetController;
import deincraftlauncher.IO.FileUtils;
import deincraftlauncher.designElements.DesignHelpers;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.ModpackSelector;
import deincraftlauncher.news.NewsData.NewsElement;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Darkp
 */

//TODO red icon for uncread elements

public class NewsHandler {
    
    private static FXMLSheetController UI = FXMLSheetController.getInstance();
    private static final ArrayList<SliderElement> SliderElements = new ArrayList<>();
    private static final ArrayList<Double> transparencies = new ArrayList<>();
    private static final ArrayList<Thread> Threads = new ArrayList<>();
    
    public static void init() {
        System.out.println("initing news, downloading and reading file");
        UI = FXMLSheetController.getInstance();
        NewsData.loadFromFile();
        System.out.println("loaded from file/web, trying to add local elements");
        addFromFile();
        System.out.println("attempted to add elements from file, sorting news");
        Collections.sort(NewsData.getInstance().getElements());
        System.out.println("sorted News, setting pack news");
        setPackNews();
        System.out.println("updated modpack news");
        showUnread();
    }
    
    public static void show() {
        System.out.println("showing news Tab");
        UI.labelNews.getStyleClass().clear();
        UI.labelNews.getStyleClass().add("MenuLabelFocus");
        
        UI.NewsPane.setVisible(true);
        UI.newsSeparator.setVisible(true);
        UI.NewsContentTitle.setVisible(true);
        UI.NewsContentText.setVisible(true);
        UI.newsDate.setVisible(true);
        
        NewsData.getInstance().lastOpened = new Date();
        showElements();
        
        System.out.println(NewsData.getInstance());
    }
    
    public static void hide() {
        System.out.println("hiding news Tab");
        UI.labelNews.getStyleClass().clear();
        UI.labelNews.getStyleClass().add("MenuLabel");
        UI.NewsPane.setVisible(false);
        UI.newsSeparator.setVisible(false);
        UI.NewsContentTitle.setVisible(false);
        UI.NewsContentText.setVisible(false);
        UI.newsDate.setVisible(false);
        
        for (SliderElement elem : SliderElements) {
            elem.setVisible(false);
        }
        
    }
    
    public static void addFromFile() {
        File maybe = new File(Config.getLauncherFolder() + "toAdd.txt");
        if (maybe.exists()) {
            System.out.println("found News to add!");
            ArrayList<String> elems = FileUtils.readFile(maybe);
            for (int i=0; i < elems.size(); i += 3) {
                NewsData.addItem(elems.get(i), elems.get(i + 1), elems.get(i + 2));
            }
        }
        
        maybe.delete();
    }
    
    private static void setPackNews() {
        //for (NewsElement elem : NewsData.getInstance().getElements()) {
        ArrayList<NewsElement> elements = NewsData.getInstance().getElements();
        for (int i = elements.size() - 1; i >= 0; i--) {
            for (Modpack pack : ModpackSelector.getInstance().getPacks()) {
                if (elements.get(i).belonging.equals(pack.getName())) {
                    System.out.println("found news for pack: " + pack.getName() + " title=" + elements.get(i).Title + " text=" + elements.get(i).Text);
                    pack.setNews(elements.get(i).Text);
                    pack.updateInfo();
                }
            }
        }
    }
    
    //adds elements to pane
    private static void showElements() {
        
        if (SliderElements.size() >= 1) {
            for (SliderElement elem : SliderElements) {
                elem.setVisible(true);
            }
            showUnread();
            return;
        }
        
        UI.ScrollPaneContent.setPrefWidth(UI.ScrollPaneContent.getPrefWidth() - 5);
        
        int posX = 0;
        int posY = 0;
        
        for (NewsElement elem : NewsData.getInstance().getElements()) {
            //UI.ScrollPaneContent.prefHeight(posY);
            SliderElement item = new SliderElement(posX, posY, elem);
            SliderElements.add(item);
            transparencies.add(new Double(0));
            Threads.add(new Thread());
            
            posY += SliderElement.height;
        }
        
        SliderElements.get(0).select();
        showUnread();
    }
    
    public static class SliderElement {
        
        private static final int height = 65;
        private static final int belongToSize = 12;
        private static final int bottomHeight = 2;
        
        private final int width;
        private final NewsElement elem;
        private final Label belongTo;
        private final Label title;
        private final Rectangle bottomEnd;
        private boolean selected = false;
    
        public SliderElement(int posX, int posY, NewsElement elem) {
            this.width = (int) UI.NewsPane.getWidth();
            this.elem = elem;
            
            Pane addTo = UI.ScrollPaneContent;
            
            belongTo = new Label(elem.belonging);
            belongTo.setLayoutX(posX);
            belongTo.setLayoutY(posY);
            belongTo.setFont(DesignHelpers.getTextFont(belongToSize));
            belongTo.setTextFill(Color.rgb(201, 201, 201, 0.61));
            
            title = new Label(elem.Title);
            title.setLayoutX(posX);
            title.setLayoutY(posY);
            title.setPrefSize(width, height);
            title.setTextAlignment(TextAlignment.CENTER);
            title.setAlignment(Pos.CENTER);
            title.setFont(DesignHelpers.getTextFont(16));
            title.setTextFill(Color.WHITE);
            title.setOnMouseEntered((MouseEvent e) -> {
                handleHover(true);
            });

            title.setOnMouseExited((MouseEvent e) -> {
                handleHover(false);
            });
            title.setOnMouseClicked((MouseEvent e) -> {
                select();
            });
            
            bottomEnd = new Rectangle();
            bottomEnd.setFill(Color.rgb(201, 201, 201, 0.61));
            bottomEnd.setLayoutX(posX);
            bottomEnd.setLayoutY(posY + height - bottomHeight);
            bottomEnd.setHeight(bottomHeight);
            bottomEnd.setWidth(width);
            
            addTo.getChildren().add(belongTo);
            addTo.getChildren().add(title);
            addTo.getChildren().add(bottomEnd);
            
        }
        
        public void setVisible(boolean state) {
            belongTo.setVisible(state);
            title.setVisible(state);
            bottomEnd.setVisible(state);
        }
        
        public void select() {
            System.out.println("selecting news: " + elem.Title);
            for (SliderElement elema : SliderElements) {
                elema.unselect();
            }
            if (!NewsData.getInstance().getRead().contains(elem.Text + elem.Title)) {
                NewsData.getInstance().getRead().add(elem.Text + elem.Title);
            }
            title.setStyle("-fx-background-color: rgba(201, 201, 201, 0.61);");
            selected = true;
            UI.NewsContentTitle.setText(elem.Title.toLowerCase());
            UI.NewsContentText.setText(elem.Text.toLowerCase());
            UI.newsDate.setText(NewsData.getFormat(elem.date));
            NewsData.save();
            showUnread();
        }
        
        public void unselect() {
            title.setStyle("-fx-background-color: rgba(201, 201, 201, 0);");
            selected = false;
        }
        
        private void handleHover(final boolean entered) {
            
            if (selected) {
                return;
            }
            
            final SliderElement thisElement = this;
            
            Thread hoverThread = new Thread() {
                @Override
                public void run(){
                    //doStuff

                    double fromA;
                    double to;
                    final int time = 300;
                    final int timeDelay = 20;
                    final int stepNum = time / timeDelay;
                    final double increaseNum;

                    if (entered) {
                        to = 0.61;
                        fromA = transparencies.get(SliderElements.indexOf(thisElement));
                        increaseNum = to / stepNum;
                    } else {
                        fromA = transparencies.get(SliderElements.indexOf(thisElement));
                        increaseNum = fromA / stepNum;
                    }

                    final double from = fromA;

                    for (int i = 0; i < stepNum; i++) {
                        final int a = i;
                        Platform.runLater(() -> {
                            double transparency = a * increaseNum;

                            if (entered) {
                                title.setStyle("-fx-background-color: rgba(201, 201, 201, " +  transparency +");");
                            } else {
                                title.setStyle("-fx-background-color: rgba(201, 201, 201, " +  (from - (transparency)) +");");
                            }
                            transparencies.set(SliderElements.indexOf(thisElement), transparency);
                        });
                        try {
                            Thread.sleep(timeDelay);
                        } catch (InterruptedException ex) {
                            //Logger.getLogger(NewsHandler.class.getName()).log(Level.SEVERE, null, ex);
                            break;
                        }
                    }

                }
            };
            Threads.get(SliderElements.indexOf(thisElement)).interrupt();
            Threads.set(SliderElements.indexOf(thisElement), hoverThread);
            hoverThread.start();
            
        }
        
    }
    
    private static void showUnread() {
        if (NewsData.getInstance().lastOpened == null) {
            NewsData.getInstance().lastOpened = new Date();
            NewsData.save();
        }
        //int notRead = NewsData.getInstance().getElements().size() - NewsData.getInstance().getRead().size();
        int notRead = 0;
        
        for (NewsElement elem : NewsData.getInstance().getElements()) {
            if (elem.date.compareTo(NewsData.getInstance().lastOpened) > 0) {
                System.out.println("found unread item!");
                notRead++;
            }
        }
        
        System.out.println("showing unread num" + notRead);
        
        if (notRead >= 1) {
            System.out.println("more than 1 unread item, displaying num (" + notRead + ")");
            UI.unreadNotes.setVisible(true);
            UI.unreadNotes.setText(Integer.toString(notRead));
        } else {
            UI.unreadNotes.setVisible(false);
            
        }
    }
    
    private static boolean isRead(NewsElement news) {
        System.out.println("checking if news is read " + news.Title);
        
        for (String elem : NewsData.getInstance().getRead()) {
            if (elem.equals(news.Text + news.Title)) {
                return true;
            }
        }
        
        return false;
    }
}
