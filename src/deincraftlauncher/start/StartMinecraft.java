/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.start;

import deincraftlauncher.Config;
import deincraftlauncher.modPacks.Modpack;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.rx14.jmclaunchlib.LaunchSpec;
import uk.co.rx14.jmclaunchlib.LaunchTask;
import uk.co.rx14.jmclaunchlib.LaunchTaskBuilder;
import uk.co.rx14.jmclaunchlib.util.ChangePrinter;

/**
 *
 * @author Darkp
 */
public class StartMinecraft {
    
    public static void start(Modpack pack) {
        //http://files.minecraftforge.net/maven/net/minecraftforge/forge/1.7.10-10.13.4.1558-1.7.10/forge-1.7.10-10.13.4.1558-1.7.10-universal.jar
        try {
            System.out.println("preparing minecraft start");
            System.out.println("pack starting: " + pack.getName() + " path=" + pack.getPath());
            LaunchTask task = new LaunchTaskBuilder()
            .setNetOffline(true)
            .setCachesDir(Config.getCacheFolder()) //Directory to cache stuff in, copies caches from .minecraft (and verifies)

            //.setMinecraftVersion("1.7.10") //Set vanilla version
            //OR
            .setForgeVersion("1.7.10", "1.7.10-10.13.4.1558-1.7.10") //Minecraftforge version

            .setInstanceDir(pack.getPath()) //Minecraft directory

            .setUsername("rearth") //Username for offline
            .setOffline() //Offline mode
                    
            .build(); //Build LaunchTask
            
            System.out.println("builded launch task");
            System.out.println(Arrays.toString(task.getDoneTasks().toArray()) + " | " + task.getRemainingTasks().toString());
            new ChangePrinter(
		() -> "" + task.getCompletedPercentage(), 100
            ).start();
            System.out.println(Arrays.toString(task.getDoneTasks().toArray()) + " | " + task.getRemainingTasks().toString());

            LaunchSpec spec = task.getSpec();
            System.out.println("starting MC process");
            System.out.println(Arrays.toString(spec.getJavaCommandlineArray()));
            
            Process run = spec.run(new File(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe").toPath());
            
            BufferedReader stdout = new BufferedReader(new InputStreamReader(run.getInputStream()));
            String line = null;
            try {
                    while ((line = stdout.readLine()) != null) {
                            System.out.println(line);
                    }
            } catch (IOException e) {
                    e.printStackTrace();
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(StartMinecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
