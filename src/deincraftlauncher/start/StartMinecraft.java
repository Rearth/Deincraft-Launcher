/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.start;

import deincraftlauncher.Config;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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
        //http://files.minecraftforge.net/maven/net/minecraftforge/forge/1.7.10-10.13.4.1558-1.7.10/forge-1.7.10-10.13.4.1558-1.7.10-universal.jar
        try {
            
            System.out.println("pack starting: " + pack.getName() + " path=" + pack.getPath());
            System.out.println("starting with username=" + settings.getUsername() + " password=" + settings.getPassword());
            
            //YggdrasilAuth.auth(settings.getUsername(), settings.getPassword());
            
            LaunchTask task = new LaunchTaskBuilder()
            //.setOffline()
            //.setNetOffline()
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
            
            System.out.println("current task: " + task.getCurrentTasks());
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
                    } else if (line.contains("[Client thread/INFO]: Stopping!")) {
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

    private static class MCPasswordSupplier implements PasswordSupplier {

        public MCPasswordSupplier() {
            
        }

        @Override
        public String getPassword(String string, boolean bln, String string1) {
            return settings.getPassword();
        }
    }
    /*
    private final static String authserver = "https://authserver.mojang.com";
    
    public static String authenticate(String username, String password) throws Exception {
    
    String genClientToken = UUID.randomUUID().toString();
    
    // Setting up json POST request
    String payload = "{\"agent\": {\"name\": \"Minecraft\",\"version\": 1},\"username\": \"" + username
    + "\",\"password\": \"" + password + "\",\"clientToken\": \"" + genClientToken + "\"}";
    
    String output = postReadURL(payload, new URL(authserver + "/authenticate"));
    
    // Setting up patterns
    String authBeg = "{\"accessToken\":\"";
    String authEnd = "\",\"clientToken\":\"";
    String clientEnd = "\",\"selectedProfile\"";
    
    // What we are looking for
    String authtoken = getStringBetween(output, authBeg, authEnd);
    return authtoken;
    }
    
    private static String postReadURL(String payload, URL url) throws Exception {
    HttpsURLConnection con = (HttpsURLConnection) (url.openConnection());
    
    con.setReadTimeout(15000);
    con.setConnectTimeout(15000);
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setDoInput(true);
    con.setDoOutput(true);
    
    OutputStream out = con.getOutputStream();
    out.write(payload.getBytes("UTF-8"));
    out.close();
    
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    
    String output = "";
    String line = null;
    while ((line = in.readLine()) != null)
    output += line;
    
    in.close();
    
    return output;
    }
    
    private static String getStringBetween(String base, String begin, String end) {
    
    Pattern patbeg = Pattern.compile(Pattern.quote(begin));
    Pattern patend = Pattern.compile(Pattern.quote(end));
    
    int resbeg = 0;
    int resend = base.length() - 1;
    
    Matcher matbeg = patbeg.matcher(base);
    
    if (matbeg.find())
    resbeg = matbeg.end();
    
    Matcher matend = patend.matcher(base);
    
    if (matend.find())
    resend = matend.start();
    
    return base.substring(resbeg, resend);
    }
    */
}
