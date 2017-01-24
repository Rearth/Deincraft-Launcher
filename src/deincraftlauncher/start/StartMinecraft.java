/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.start;

import deincraftlauncher.Config;
import deincraftlauncher.IO.download.FTPConnection;
import deincraftlauncher.IO.download.FTPDownloader;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import uk.co.rx14.jmclaunchlib.LaunchSpec;
import uk.co.rx14.jmclaunchlib.LaunchTask;
import uk.co.rx14.jmclaunchlib.LaunchTaskBuilder;
import uk.co.rx14.jmclaunchlib.auth.PasswordSupplier;
import uk.co.rx14.jmclaunchlib.util.ChangePrinter;

/**
 *
 * @author Darkp
 */
public class StartMinecraft {
    
    public static void start(Modpack pack) {
        
        if (hasCaches()) {
            startMC(pack);
        } else {
            
            Thread prepareThread = new Thread(){
                @Override
                public void run(){
                    createCaches(pack);
                }
            };

            prepareThread.start();
            
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("starting offline, caches done");
                    prepareThread.interrupt();
                    startMC(pack);
                }
            }, 15000);
        }
    }
    
    private static void createCaches(Modpack pack) {
        System.out.println("creating caches!");
            LaunchTask task = new LaunchTaskBuilder()
            //.setOffline()
            //.setNetOffline()
            .setCachesDir(Config.getCacheFolder())
            .setForgeVersion("1.7.10", pack.getForgeVersion())
            .setInstanceDir(pack.getPath())
            .setUsername(settings.getUsername())
            .setPasswordSupplier(new MCPasswordSupplier())
            .build();
            
            LaunchSpec spec = task.getSpec();
            //Process run = spec.run(new File(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe").toPath());
            
            //LaunchSpec spec = task.getSpec();
    }
    
    public static void startMC(Modpack pack) {
        //http://files.minecraftforge.net/maven/net/minecraftforge/forge/1.7.10-10.13.4.1558-1.7.10/forge-1.7.10-10.13.4.1558-1.7.10-universal.jar
        try {
            
            System.out.println("pack starting: " + pack.getName() + " path=" + pack.getPath());
            System.out.println("starting with username=" + settings.getUsername() + " password=" + settings.getPassword());
            
            //YggdrasilAuth.auth(settings.getUsername(), settings.getPassword());
            
            LaunchTask task = new LaunchTaskBuilder()
            .setOffline()
            .setNetOffline()
            .setCachesDir(Config.getCacheFolder())
            .setForgeVersion("1.7.10", pack.getForgeVersion())
            .setInstanceDir(pack.getPath())
            .setUsername(settings.getUsername())
            .setPasswordSupplier(new MCPasswordSupplier())
            .build();
            
            System.out.println("created launch task");
            
            new ChangePrinter(
                () -> "" + task.getCompletedPercentage(), 100
            ).start();
            
            System.out.println("creating launchspec..." + Arrays.toString(task.getRemainingTasks().toArray()));
            
            System.out.println("current task: " + task.getCurrentTasks() + task.isStarted());
            
            LaunchSpec spec = task.getSpec();
            
            System.out.println("starting MC process");
            System.out.println(spec.getJavaCommandline());
            
            Process run = spec.run(new File(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe").toPath());
            
            BufferedReader stdout = new BufferedReader(new InputStreamReader(run.getInputStream()));
            String line = null;
            try {
                while ((line = stdout.readLine()) != null) {
                    System.out.println(line);
                    if (line.contains("Completed early MinecraftForge initialization")) {
                        Platform.runLater(() -> {
                            System.out.println("minecraft starting, enabling start button");
                            pack.getView().setStartLoading(false);
                            pack.getView().setStartLocked("started");
                        });
                    } else if (isErrorCode(line)) {
                        Platform.runLater(() -> {
                            System.out.println("minecraft stopped");
                            pack.getView().setStartUnLocked("Start");
                        });
                    }
                }
            } catch (IOException e) {
            System.err.println("error reading mc output " + e);
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(StartMinecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static boolean isErrorCode(String Code) {
        String errA = "cpw.mods.fml.relauncher.FMLSecurityManager$ExitTrappedException";
        String errB = "[Client thread/INFO]: Stopping!";
        String errC = "#@!@# Game crashed! Crash report saved to: #@!@#";
        return Code.contains(errA) || Code.contains(errB) || Code.contains(errC);
    }

    private static class MCPasswordSupplier implements PasswordSupplier {

        public MCPasswordSupplier() {
            
        }

        @Override
        public String getPassword(String string, boolean bln, String string1) {
            return settings.getPassword();
        }
    }
    
    private static boolean hasCaches() {
        
        File cachefolder = new File(Config.getCacheFolder());
        
        if (!cachefolder.exists()) {
            cachefolder.mkdirs();
            return false;
        }
        
        return cachefolder.listFiles().length >= 1;
        
    }
}
