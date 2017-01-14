/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.IO.download;

import deincraftlauncher.designElements.DCTile;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darkp
 */
public class Downloader {
    
    //testlink: http://download808.mediafire.com/g8x2577ap5jg/ziep00plk3y1so3/TConstruct-1.7.10-1.8.8.jar
    
    private boolean prepared = false;
    private boolean preparing = false;
    private final String link;
    private String saveTo;
    private URL target;
    private String fileName;
    private float totalSize;
    private Function onFinished;
    private Function onFinishedB;
    private Thread prepareThread;
    private Thread downloadThread;
    private float totalProgress;
    private boolean finished = false;
    private FunctionProg onUpdate;
    private Function onPrepared;
    private final Downloader instance;
    
    public Downloader(String URL, String saveTo) {
        this.link = URL;
        this.saveTo = saveTo;
        
        onFinished = this::doNothing;
        onFinishedB = this::doNothing;
        onUpdate = this::doNothing;
        onPrepared = this::doNothing;
        instance = this;
        
    }
    
    public void prepare() {     //generating: fileName, target, 
        
        preparing = true;
        
        prepareThread = new Thread(){
            @Override
            public void run(){
                
            try {
                target = new URL(link);

                URLConnection targetCon = target.openConnection();

                totalSize = targetCon.getContentLength();

                String raw = targetCon.getHeaderField("Content-Disposition");
                
                if(raw != null && raw.contains("=")) {
                    fileName = raw.split("=")[1];
                    fileName = fileName.split(";")[0];
                } else {
                    fileName = "Unbekannt";
                }

                fileName = fileName.replaceAll("\"", "");

                System.out.println("Prepared Download: size: " + totalSize + " name=" + fileName);
                prepared = true;
                preparing = false;
                
                onPrepared.call(instance);

            } catch (MalformedURLException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }

            }
        };

        prepareThread.start();
                
    }
    
    private boolean started = false;
    
    public void start() {
        
        if (!prepared) {
            prepare();
        }
        
        if (preparing) {
            try {
                prepareThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (started) {
            return;
        }
        
        started = true;
        
        System.out.println("preparing download: " + link + " | " + fileName);
        
        finished = false;
        updateNum = 0;
        
        /*if (downloadThread != null) {
            try {
            downloadThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
        
        downloadThread = new Thread(){
            @Override
            public void run(){
                
                System.out.println("Download Thread started");
                
                File targetPath = new File(saveTo);
                if (!targetPath.exists()) {
                    targetPath.mkdirs();
                }
                
                saveTo += fileName;
        
                System.out.println("Starting Download; Link=" + link + " saveTo=" + saveTo);

                InputStream in = null; 
                try {
                    //download

                    in = new BufferedInputStream(target.openStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    float progress = 0;


                    byte[] buf = new byte[1024];
                    int n = 0;
                    System.out.println("showing download update progress");
                    update();

                    while (-1!=(n=in.read(buf))) {

                        out.write(buf, 0, n);

                        progress += 1000;
                        totalProgress = progress / (totalSize);
                        //System.out.println(totalProgress);

                    }

                    out.close();
                    in.close();

                    byte[] response = out.toByteArray();
                    FileOutputStream fos = new FileOutputStream(saveTo);
                    fos.write(response);
                    fos.close();
                    onFinished.call(instance);
                    onFinishedB.call(instance);
                    instance.finished = true;
                    System.out.println("Download fertig");
                    started = false;
                    //download fertig
                    downloadThread.interrupt();

                } catch (IOException ex) {
                    System.out.println("Error while downloading: " + ex);
                } finally {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        System.out.println("Error while finishing Download: " + ex);
                    }
                }
                
            }
        };

        downloadThread.start();
        started = false;
        
    }
    
    public float getProgress() {        //in %
        return 0;
    }
    
    public void setOnUpdate(FunctionProg func) {
        onUpdate = func;
    }
    
    private int updateNum = 0;
    
    private void update() {
        
        System.out.println("downloader update #" + updateNum);
        updateNum++;
        
        if (instance.finished) {
            System.out.println("cancelling updating");
            return;
        }
        
        onUpdate.call(totalProgress, totalSize, this);
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //periodic update
                timer.cancel();
                timer.purge();
                update();
            }
        }, 100);
        
    }
 
    public void pause() {
        
    }
    
    public void stop() {
        
    }
    
    public void setOnFinished(Function func) {
        onFinished = func;
    }

    public void setOnFinishedB(Function func) {
        onFinishedB = func;
    }
    
    public void setOnPrepared(Function func) {
        onPrepared = func;
    }

    public String getFileName() {
        return fileName;
    }

    public float getTotalSize() {
        return totalSize;
    }
    
    
    @FunctionalInterface
    public interface Function {
        void call(Downloader loader);
    }
    
    @FunctionalInterface
    public interface FunctionProg {
        void call(float progress, float totalSize, Downloader loader);
    }
    
    private void doNothing(Downloader loader) {
        System.out.println("Downloader finish not jet created");
    }
    
    private void doNothing(float A, float B, Downloader loader) {
        System.out.println("Downloader Update: Progress =" + A * 100 + " ges=" + B);
    }

    public Thread getPrepareThread() {
        return prepareThread;
    }
    
    public String getTargetFile() {
        return saveTo;
    }
    
}
