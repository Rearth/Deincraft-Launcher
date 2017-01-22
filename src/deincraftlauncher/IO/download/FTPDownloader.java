/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.IO.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Darkp
 */
public class FTPDownloader extends Downloader {
    //testlink: http://download808.mediafire.com/g8x2577ap5jg/ziep00plk3y1so3/TConstruct-1.7.10-1.8.8.jar
    
    private boolean prepared = false;
    private boolean preparing = false;
    private String link;
    private InputStream is;
    private String saveTo;
    private URL target;
    private final String fileName;
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
    private FTPFile ftpfile;
    private String ftpFolder;
    private boolean started = false;

    public FTPDownloader(String name, String ftpfolder, String folder) {
        super(name, folder);
        this.fileName = name;
        this.saveTo = folder;
        this.ftpFolder = ftpfolder;
        
        onFinished = this::doNothing;
        onFinishedB = this::doNothing;
        onUpdate = this::doNothing;
        onPrepared = this::doNothing;
        
        instance = this;
    }
    
    @Override
    public void prepare() {     //generating: fileName, totalSize, 
        
        preparing = true;
        
        prepareThread = new Thread(){
            @Override
            public void run(){
                //client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                try {
                    //System.out.println("ftp info: Server=" + FTPSync.getFtpServer() + " user=" + FTPSync.getFtpUsername() + " PW=" + FTPSync.getFtpPassword());
                    
                    FTPConnection.changeDir(ftpFolder);
                    
                    //System.out.println(Arrays.toString(client.listNames()));
                    
                    ftpfile = FTPConnection.getClient().mlistFile(fileName);
                    
                    totalSize = ftpfile.getSize();
                } catch (IOException ex) {
                    Logger.getLogger(FTPDownloader.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.println("Prepared FTPDownload: size: " + totalSize + " name=" + fileName + " dir=" + ftpFolder);
                prepared = true;
                preparing = false;

                onPrepared.call(instance);

            }
        };

        prepareThread.start();
                
    }
        
    @Override
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
        
        System.out.println("starting ftp download: " + fileName);
        
        finished = false;
        updateNum = 0;
        
        
        downloadThread = new Thread(){
            @Override
            public void run(){
                
                System.out.println("FTPDownload Thread started");
                
                File targetPath = new File(saveTo);
                if (!targetPath.exists()) {
                    targetPath.mkdirs();
                }
                
                saveTo += fileName;
        
                System.out.println("Starting Download; Link=" + link + " saveTo=" + saveTo);

                //Actual download code
                try {
                    OutputStream output = new FileOutputStream(saveTo);
                    CountingOutputStream cos = new CountingOutputStream(output){

                        boolean updateReady = true;

                        @Override
                        protected void beforeWrite(int n){
                            super.beforeWrite(n);

                            if (!updateReady) {
                                return;
                            }
                            
                            
                            totalProgress = this.getCount() / totalSize;

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    updateReady = true;
                                }
                            }, updateDelay);

                            updateReady = false;
                        }
                    };
                    update();
                    
                    FTPConnection.getClient().retrieveFile(ftpfile.getName(), cos);

                    cos.close();
                    output.close();
                    onFinished.call(instance);
                    onFinishedB.call(instance);
                    finished = true;
                    System.out.println("Download fertig");
                    started = false;
                    //download fertig
                    downloadThread.interrupt();
                    
                } catch(IOException ex) {
                    System.err.println("error while downliading via ftp: " + ex);
                }
                
            }
        };

        downloadThread.start();
        started = false;
        
    }
    
    @Override
    public float getProgress() {        //in %
        return totalProgress;
    }
    
    @Override
    public void setOnUpdate(FunctionProg func) {
        onUpdate = func;
    }
    
    private int updateNum = 0;
    
    private void update() {
        
        System.out.println("downloader update #" + updateNum);
        updateNum++;
        
        if (finished) {
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
        }, updateDelay);
        
    }
    
    @Override
    public void setOnFinished(Function func) {
        onFinished = func;
    }

    @Override
    public void setOnFinishedB(Function func) {
        onFinishedB = func;
    }
    
    @Override
    public void setOnPrepared(Function func) {
        onPrepared = func;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public float getTotalSize() {
        return totalSize;
    }
        
    private void doNothing(Downloader loader) {
        System.out.println("Downloader finish not jet created");
    }
    
    private void doNothing(float A, float B, Downloader loader) {
        System.out.println("Downloader Update: Progress =" + A * 100 + " ges=" + B);
    }

    @Override
    public Thread getPrepareThread() {
        return prepareThread;
    }
    
    @Override
    public String getTargetFile() {
        return saveTo;
    }
    
}
