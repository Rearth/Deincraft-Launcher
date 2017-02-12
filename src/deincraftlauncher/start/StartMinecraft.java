/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.start;

import deincraftlauncher.IO.download.DownloadHandler;
import deincraftlauncher.designElements.DesignHelpers;
import deincraftlauncher.designElements.PackViewHandler;
import deincraftlauncher.modPacks.Modpack;
import deincraftlauncher.modPacks.settings;
import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.JavaUtil;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.LogUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.apache.commons.io.FileUtils;
import uk.co.rx14.jmclaunchlib.auth.PasswordSupplier;
import uk.co.rx14.jmclaunchlib.util.OS;

/**
 *
 * @author Darkp
 */
public class StartMinecraft {
    
    
    public static void start(Modpack pack) {
        
        startMC(pack);
        
    }
    
    public static void startMC(Modpack pack) {
        
        try {
            
            System.out.println("trying to start minecraft");
            
            GameInfos infos = new GameInfos(pack.getName(), new File(pack.getPath()), new GameVersion(pack.getMCVersion(), pack.getGameType()), new GameTweak[] {GameTweak.FORGE});
            System.out.println("GameInfos done");
            
            System.out.println("Zugangsdaten: " + settings.getUsername() + settings.getPassword());
            
            Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
            AuthResponse rep = authenticator.authenticate(AuthAgent.MINECRAFT, settings.getUsername(), settings.getPassword(), "");
            AuthInfos authInfos = new AuthInfos(rep.getSelectedProfile().getName(), rep.getAccessToken(), rep.getSelectedProfile().getId());
            System.out.println("authinfos done");
            
            //AuthInfos authInfos = new AuthInfos(settings.getUsername(), MCAuthentication.getToken(settings.getUsername(), settings.getPassword()), MCAuthentication.getUUID(settings.getUsername(), settings.getPassword()));
            
            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, getFolder(pack), authInfos);
            List<String> vmArgs = profile.getVmArgs();
            vmArgs.add(String.valueOf("-Xms" + settings.getRAM() + "m"));
            //System.out.println("vm args: " + vmArgs);
            profile.setVmArgs(vmArgs);
            ExternalLauncher launcher = new MCLauncher(profile);
            System.out.println("profile and launcher done " + launcher.getProfile());
            
            Process launch = launcher.launch();
            
            BufferedReader stdout = new BufferedReader(new InputStreamReader(launch.getInputStream()));
            String line;
            
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Completed early MinecraftForge initialization")) {
                    Platform.runLater(() -> {
                        System.out.println("minecraft starting, enabling start button");
                        PackViewHandler.setStartLoading(false);
                        PackViewHandler.setStartLocked("started");
                        pack.setStarted(true);
                    });
                } else if (isErrorCode(line)) {
                    Platform.runLater(() -> {
                        System.out.println("minecraft stopped");
                        PackViewHandler.setStartLoading(false);
                        PackViewHandler.setStartUnLocked("Start");
                        pack.setStarted(false);
                    });
                }
                
            }
            
            checkAlive(launch, pack);
            
        } catch (LaunchException | AuthenticationException | IOException ex) {
            Logger.getLogger(StartMinecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void checkAlive(Process launch, Modpack pack) {
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!launch.isAlive()) {
                    Platform.runLater(() -> {
                        System.out.println("minecraft stopped, process is dead!");
                        PackViewHandler.setStartLoading(false);
                        PackViewHandler.setStartUnLocked("Start");
                    });
                } else {
                    checkAlive(launch, pack);
                }
            }
        }, 3000);
    }
    
    private static GameFolder getFolder(Modpack pack) {
        
        String assetsFolder = "Cache" + File.separator + "assets";
        String libsFolder = "Cache" + File.separator + "libs";
        String nativesFolder = "Cache" + File.separator + "natives" + File.separator + "1.7.10";
        String mainJar = "Cache" + File.separator + "versions" + File.separator + "1.7.10.jar";
        
        return new GameFolder(assetsFolder, libsFolder, nativesFolder, mainJar);
        
    }
    
    private static boolean isErrorCode(String Code) {
        //System.out.println("checking error code for: " + Code);
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
    
    public static boolean hasCaches(Modpack pack) {
        
        File cachefolder = new File(pack.getPath() + "Cache");
        
        if (!cachefolder.exists()) {
            cachefolder.mkdirs();
            return false;
        }
        
        return cachefolder.listFiles().length >= 1;
        
    }
    
    public static void fixCaches(String dir) {
        
        try {
            //create guava 16.0 file
            FileUtils.deleteDirectory(new File(dir + "/libs/com/google/guava/guava/"));
            String targetPath = dir + "/libs/com/google/guava/guava/16.0/";
            
            new File(targetPath).mkdirs();
            URL inputUrl = DownloadHandler.getInstance().getClass().getResource("/deincraftlauncher/Images/guava-16.0.jar");
            File dest = new File(targetPath + "guava-16.0.jar");
            FileUtils.copyURLToFile(inputUrl, dest);
            
            //fix Forge Multipart
            System.out.println("fixing forge multipart if existing");
            String targetMod = dir.replace("Cache", "mods") + File.separator + "ForgeMultipart-1.7.10-1.2.0.345-universal.jar";
            if (new File(targetMod).exists()) {
                String targetModPath = dir.replace("Cache", "mods") + File.separator + "1.7.10" + File.separator;
                String targetModLocation = targetModPath + "ForgeMultipart-1.7.10-1.2.0.345-universal.jar";
                new File(targetModPath).mkdirs();
                new File(targetMod).renameTo(new File(targetModLocation));
            }
            
        } catch (IOException ex) {
            Logger.getLogger(StartMinecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     */
    public static class MCLauncher extends ExternalLauncher {
    
        ExternalLaunchProfile profile;
        
        public MCLauncher(ExternalLaunchProfile profile) {
            super(profile, null);
            this.profile = profile;
            
        }
        
        @Override
        public Process launch() throws LaunchException {
            
            LogUtil.info("hi-ext");

            ProcessBuilder builder = new ProcessBuilder();
            ArrayList<String> commands = new ArrayList<>();
            commands.add(getJavaCommand());
            commands.addAll(Arrays.asList(JavaUtil.getSpecialArgs()));

            if (profile.getMacDockName() != null && System.getProperty("os.name").toLowerCase().contains("mac"))
                commands.add(JavaUtil.macDockName(profile.getMacDockName()));
            if (profile.getVmArgs() != null)
                commands.addAll(profile.getVmArgs());

            commands.add("-cp");
            commands.add(profile.getClassPath());

            commands.add(profile.getMainClass());

            if (profile.getArgs() != null)
                commands.addAll(profile.getArgs());

            if (profile.getDirectory() != null)
                builder.directory(profile.getDirectory());

            if (profile.isRedirectErrorStream())
                builder.redirectErrorStream(true);

            builder.command(commands);

            String entireCommand = "";
            for (String command : commands)
                entireCommand += command + " ";

            LogUtil.info("ent", ":", entireCommand);
            LogUtil.info("start", profile.getMainClass());

            try
            {
                Process p = builder.start();

                return p;
            }
            catch (IOException e)
            {
                throw new LaunchException("Cannot launch !", e);
            }
        }
    
    }
    
    private static String getJavaCommand() {
        
        String path = System.getProperty("java.home") + File.separator + "bin" + File.separator + (OS.getCURRENT() == OS.WINDOWS ? "java.exe" : "java");
        
        String minecraftRuntime = "C:\\Program Files (x86)\\Minecraft\\runtime\\jre-x64\\1.8.0_25\\bin\\java.exe";
        if (new File(minecraftRuntime).exists()) {
            System.out.println("using minecraft runtime!");
            return minecraftRuntime;
        }
        
        File x64Test = new File("C:\\Program Files\\Java");
        
        if (path.contains(" (x86)")) {
            if (x64Test.exists()) {
                return path.replace(" (x86)", "");
            }
            
            Platform.runLater(() -> {
                DesignHelpers.popupMessage("Du verwendest eine 32-Bit Java version. Dies kann evtl zu problemen führen. Bitte lade dir die aktuelle 64-bit version herunter", "http://www.chip.de/downloads/Java-Runtime-Environment-64-Bit_42224883.html");
            });
        }
        
        return path;
    }
}
