/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.start;

import deincraftlauncher.IO.download.DownloadHandler;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.settings;
import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.apache.commons.io.FileUtils;
import uk.co.rx14.jmclaunchlib.LaunchSpec;
import uk.co.rx14.jmclaunchlib.LaunchTask;
import uk.co.rx14.jmclaunchlib.LaunchTaskBuilder;
import uk.co.rx14.jmclaunchlib.auth.PasswordSupplier;

/**
 *
 * @author Darkp
 */
public class StartMinecraft {
    
    public static void start(Modpack pack) {
        
        if (!hasCaches(pack)) {
            createCaches(pack);
            
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    fixCaches(pack.getPath() + "Cache");
                    startMC(pack);
                }
            }, 15000);
        } else {
            startMC(pack);
        }
        
    }
    
    /*private static void startNew(Modpack pack) {
        
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

        System.out.println("creating start Args");

        
        String StartArg = spec.getJavaCommandline();
        String accessToken = MCAuthentication.getToken(settings.getUsername(), settings.getPassword());
        
        
        String searchKey = "--accessToken -";
        int TokenPos = StartArg.indexOf(searchKey) + searchKey.length();
        
        String partA = StartArg.substring(0, TokenPos);
        String partB = StartArg.substring(TokenPos);
        StartArg = partA + accessToken + partB;
        
        StringBuilder sb = new StringBuilder(StartArg);
        sb.deleteCharAt(TokenPos - 1);
        StartArg = sb.toString();
        
        System.out.println("Args:");
        System.out.println(StartArg);
        
        
        try {
            System.out.println("starting process, java dir=" + System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe");
            Runtime runtime = Runtime.getRuntime();
            
            Process p1 = runtime.exec("\"" + System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe" + "\" " + StartArg, null, new File(pack.getPath()));
            InputStream is = p1.getInputStream();
            int i ;
            while ((i = is.read()) != -1) {
                System.out.print((char)i);
            }
        } catch (IOException ex) {
            Logger.getLogger(StartMinecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }*/
    
    private static void createCaches(Modpack pack) {
        
        System.out.println("creating caches!");
        LaunchTask task = new LaunchTaskBuilder()
        //.setOffline()
        //.setNetOffline()
        .setCachesDir(pack.getPath() + "Cache")
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
        /*try {

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
        }*/

        try {
            GameInfos infos = new GameInfos(pack.getName(), new File(pack.getPath()), new GameVersion("1.7.10", GameType.V1_7_10), new GameTweak[] {GameTweak.FORGE});
            
            Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
            AuthResponse rep = authenticator.authenticate(AuthAgent.MINECRAFT, settings.getUsername(), settings.getPassword(), "");
            AuthInfos authInfos = new AuthInfos(rep.getSelectedProfile().getName(), rep.getAccessToken(), rep.getSelectedProfile().getId());
            
            //AuthInfos authInfos = new AuthInfos(settings.getUsername(), MCAuthentication.getToken(settings.getUsername(), settings.getPassword()), MCAuthentication.getUUID(settings.getUsername(), settings.getPassword()));
            
            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, getFolder(pack), authInfos);
            ExternalLauncher launcher = new ExternalLauncher(profile);
            
            Process launch = launcher.launch();
            
            BufferedReader stdout = new BufferedReader(new InputStreamReader(launch.getInputStream()));
            String line;
            
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
                        pack.getView().setStartLoading(false);
                        pack.getView().setStartUnLocked("Start");
                    });
                }
            }
            
        } catch (LaunchException | AuthenticationException | IOException ex) {
            Logger.getLogger(StartMinecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static GameFolder getFolder(Modpack pack) {
        
        String assetsFolder = "Cache" + File.separator + "assets";
        String libsFolder = "Cache" + File.separator + "libs";
        String nativesFolder = "Cache" + File.separator + "natives" + File.separator + "1.7.10";
        String mainJar = "Cache" + File.separator + "versions" + File.separator + "1.7.10.jar";
        
        return new GameFolder(assetsFolder, libsFolder, nativesFolder, mainJar);
        
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
    
    private static boolean hasCaches(Modpack pack) {
        
        File cachefolder = new File(pack.getPath() + "Cache");
        
        if (!cachefolder.exists()) {
            cachefolder.mkdirs();
            return false;
        }
        
        return cachefolder.listFiles().length >= 1;
        
    }
    
    private static void fixCaches(String dir) {
        
        try {
            //create guava 16.0 file
            FileUtils.deleteDirectory(new File(makePath(dir + "\\libs\\com\\google\\guava\\guava\\")));
            String targetPath = makePath(dir + "\\libs\\com\\google\\guava\\guava\\16.0\\");
            new File(targetPath).mkdirs();
            URL inputUrl = DownloadHandler.getInstance().getClass().getResource("/deincraftlauncher/Images/guava-16.0.jar");
            File dest = new File(targetPath + "guava-16.0.jar");
            FileUtils.copyURLToFile(inputUrl, dest);
        } catch (IOException ex) {
            Logger.getLogger(StartMinecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String makePath(String path) {
        return path.replaceAll("\\", File.separator);
    }
}
