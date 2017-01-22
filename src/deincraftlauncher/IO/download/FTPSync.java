/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.IO.download;

import deincraftlauncher.modPacks.Modpack;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Darkp
 */
public class FTPSync {
    
    private static final String ftpUsername = "user3-1";
    private static final String ftpPassword = "NBzF3d3gLJ";
    private static final String ftpServer = "46.4.75.39";

    private final String folder;
    private final String FTPDir;
    private final Modpack pack;
    private final ArrayList<String> ClientFiles;
    private final ArrayList<String> OnlineFiles;
    private final ArrayList<String> DownloadFiles = new ArrayList<>();
    
    public FTPSync(String folder, String FTPDir, Modpack pack) {
        this.folder = folder;
        this.FTPDir = FTPDir;
        
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        
        DownloadHandler.setStartBlocked(true);
        pack.getView().setStartLoading(true);
        
        System.out.println("starting ftp sync for folder " + folder + "| " + FTPDir);
        
        ClientFiles = convertToString(new ArrayList<>(Arrays.asList(new File(folder).listFiles())));
        ClientFiles.sort(String::compareToIgnoreCase);
        OnlineFiles = new ArrayList<>(Arrays.asList(listFiles(FTPDir)));
        OnlineFiles.sort(String::compareToIgnoreCase);
        
        System.out.println("server files: " + OnlineFiles);
        System.out.println("client files: " + ClientFiles);
        
        deleteClientFiles();
        Thread prepareThread = new Thread(){
            @Override
            public void run(){
                downloadServerFiles();
            }
        };

        prepareThread.start();
        
        this.pack = pack;
    }
    
    private void deleteClientFiles() {
        
        for (String elem : ClientFiles) {
            if (!OnlineFiles.contains(elem)) {
                System.out.println("deleting client file: " + elem);
                new File(folder + elem).delete();
            }
        }
        
    }
    
    private void downloadServerFiles() {
        
        FTPClient client = new FTPClient();
        
        try {
            //loader = DownloadHandler.addDummy(size, name, folder);
            client.connect(ftpServer);
            client.login(ftpUsername, ftpPassword);
            client.changeWorkingDirectory(FTPDir);
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            
            /*//countfiles
            for (String elem : OnlineFiles) {
            if (!ClientFiles.contains(elem)) {
            FTPFile file = client.mlistFile(elem);
            loader = DownloadHandler.addDummy(file.getSize(), elem, folder);
            }
            }*/
            for (String elem : OnlineFiles) {
                if (!ClientFiles.contains(elem)) {
                    if (!elem.contains(".") && !elem.contains(".jar") && !elem.contains(".zip")) {
                    } else {
                        DownloadFiles.add(elem);
                    }
                }
            }
            
            for (String elem : OnlineFiles) {
                if (!ClientFiles.contains(elem)) {
                    //System.out.println("downloading online file: " + elem);
                    downloadFTPFile(elem, client);
                }
            }
            DownloadHandler.setStartBlocked(false);
            Platform.runLater(() -> {
                pack.getView().setStartLoading(false);
            });
            Platform.runLater(DownloadHandler::start);

            client.logout();
            
        } catch (IOException e) {
            System.err.println("Error connecting to ftp (listfiles): " + e);
        } finally {
            
            try {
                client.disconnect();
            } catch (IOException e) {
                System.err.println("Error disconnecting to ftp (listfiles): " + e);
            }
        }
    }
    
    private void downloadFTPFile(String name, FTPClient client) throws IOException {
        
        
        if (!name.contains(".jar") && !name.contains(".zip")) {
        
            System.out.println("file is directory, skipping....");
            return;
        }
        
        DownloadHandler.addFTPItem(name, FTPDir, folder, this::onDownloaderFinish);
                
        /*OutputStream output = new FileOutputStream(folder + name);
        
        
        CountingOutputStream cos = new CountingOutputStream(output){
        
        boolean updateReady = true;
        
        @Override
        protected void beforeWrite(int n){
        super.beforeWrite(n);
        
        if (!updateReady) {
        return;
        }
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        @Override
        public void run() {
        updateReady = true;
        }
        }, 100);
        loader.update(getCount() / size, size);
        loader.setProgress(getCount() / size);
        updateReady = false;
        }
        };
        client.retrieveFile(file.getName(), cos);
        loader.finish();
        onFinished.call();
        cos.close();
        output.close();*/
            
    }
    
    public void onDownloaderFinish(Downloader loader) {
        
        String lastFile = DownloadFiles.get(DownloadFiles.size() - 1);
        System.out.println("download finished in ftpsync: " + loader.getFileName() + " lastFile=" + lastFile);
        validateFile(loader);
        if (loader.getFileName().equals(lastFile)) {
            System.out.println("downloaded last file, stopping...");
            pack.onModsFinished();
        }
        
    }
    
    private void validateFile(Downloader loader) {
        File created = new File(loader.getTargetFile());
        if (loader.getFileName().contains(".jar") || loader.getFileName().contains(".zip")) {
            if (created.length() <= 100) {
                System.out.println("invalid downloaded file + " + loader.getFileName());
                created.delete();
                DownloadHandler.addFTPItem(loader.getFileName(), FTPDir, folder, this::onDownloaderFinish);
            }
        }
    }
    
    public static String[] listFiles(String dir) {
        
        FTPClient client = new FTPClient();

        try {
            client.connect(ftpServer);
            client.login(ftpUsername, ftpPassword);
            client.changeWorkingDirectory(dir);

            // Obtain a list of filenames in the current working
            // directory. When no file found an empty array will 
            // be returned.
            String[] names = client.listNames();
            
            client.logout();
            
            return names;
            
        } catch (IOException e) {
            System.err.println("Error connecting to ftp (listfiles): " + e);
        } finally {
            
            try {
                client.disconnect();
            } catch (IOException e) {
                System.err.println("Error disconnecting to ftp (listfiles): " + e);
            }
        }
        
        return null;
        
   }
    
    private static ArrayList<String> convertToString(ArrayList<File> File) {
        ArrayList<String> toReturn = new ArrayList<>();
        
        for (File elem : File) {
            toReturn.add(elem.getName());
        }
        
        return toReturn;
    }

    public static String getFtpUsername() {
        return ftpUsername;
    }

    public static String getFtpPassword() {
        return ftpPassword;
    }

    public static String getFtpServer() {
        return ftpServer;
    }
    
}
