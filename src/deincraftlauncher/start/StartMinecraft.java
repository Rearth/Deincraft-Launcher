/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.start;

import deincraftlauncher.modPacks.Modpack;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darkp
 */
public class StartMinecraft {
    
    public static void start(Modpack pack) {
        
        try {
            String OS = System.getProperty("os.name").toLowerCase();
            
            if (OS.contains("mac")) {
                /*Process p = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", "java" + StartArgs.getStartArgs(username, RAM) }, null, new File(Path.DCpath() + "modpacks/tekkitmain"));
                InputStream is = p.getInputStream();
                int i = 0;
                while ((i = is.read()) != -1) {
                    System.out.print((char)i);
                }*/
            } else {
                System.out.println("trying to start game: ");
                Runtime runtime = Runtime.getRuntime();

                Process p1 = runtime.exec(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe" + StartArgs.getStartArgs("rearth", 4096, pack.getPath()), null, new File(pack.getPath()));
                InputStream is = p1.getInputStream();
                int i = 0;
                while ((i = is.read()) != -1) {
                    System.out.print((char)i);
                }
            }
        } catch (NumberFormatException|IOException e) {
            System.out.println(e);
        } catch (Exception ex) {
            Logger.getLogger(StartMinecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
