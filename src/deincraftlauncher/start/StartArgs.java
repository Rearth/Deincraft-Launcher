/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.start;

/**
 *
 * @author Darkp
 */
public class StartArgs {
        
    public static String getStartArgs(String Username, int RAM, String path) {
        String DCargs = " ";
        System.out.println(Username + ":" + RAM);
        String OS = System.getProperty("os.name").toLowerCase();
        
        if (OS.contains("mac")) {
            String[] Startargs = {
                
            "-Xdock:name=Deincraft",
            "-Xmx" + RAM + "m",
            "-XX:MaxPermSize=256m",
            "-Djava.library.path=\"" + path + "/bin/natives\"",
            "-Dfml.core.libraries.mirror=http://mirror.technicpack.net/Technic/lib/fml/%s",
            "-Dminecraft.applet.TargetDirectory=\"" + path + "\"",
            "-Xms" + RAM + "M",
            "-Xmx" + RAM + "M",
            "-XX:PermSize=256M",
            "-Dfml.queryResult=Confirm",
            //"-cp " + path + "cache/net/minecraft/launchwrapper/1.11/launchwrapper-1.11.jar:" + path + "cache/org/ow2/asm/asm-all/5.0.3/asm-all-5.0.3.jar:" + path + "cache/com/typesafe/akka/akka-actor_2.11/2.3.3/akka-actor_2.11-2.3.3.jar:" + path + "cache/com/typesafe/config/1.2.1/config-1.2.1.jar:" + path + "cache/org/scala-lang/scala-actors-migration_2.11/1.1.0/scala-actors-migration_2.11-1.1.0.jar:" + path + "cache/org/scala-lang/scala-compiler/2.11.1/scala-compiler-2.11.1.jar:" + path + "cache/org/scala-lang/plugins/scala-continuations-library_2.11/1.0.2/scala-continuations-library_2.11-1.0.2.jar:" + path + "cache/org/scala-lang/plugins/scala-continuations-plugin_2.11.1/1.0.2/scala-continuations-plugin_2.11.1-1.0.2.jar:" + path + "cache/org/scala-lang/scala-library/2.11.1/scala-library-2.11.1.jar:" + path + "cache/org/scala-lang/scala-parser-combinators_2.11/1.0.1/scala-parser-combinators_2.11-1.0.1.jar:" + path + "cache/org/scala-lang/scala-reflect/2.11.1/scala-reflect-2.11.1.jar:" + path + "cache/org/scala-lang/scala-swing_2.11/1.0.1/scala-swing_2.11-1.0.1.jar:" + path + "cache/org/scala-lang/scala-xml_2.11/1.0.2/scala-xml_2.11-1.0.2.jar:" + path + "cache/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar:" + path + "cache/lzma/lzma/0.0.1/lzma-0.0.1.jar:" + path + "cache/com/mojang/realms/1.3.5/realms-1.3.5.jar:" + path + "cache/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" + path + "cache/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" + path + "cache/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" + path + "cache/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" + path + "cache/java3d/vecmath/1.3.1/vecmath-1.3.1.jar:" + path + "cache/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar:" + path + "cache/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar:" + path + "cache/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar:" + path + "cache/com/paulscode/codecwav/20101023/codecwav-20101023.jar:" + path + "cache/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar:" + path + "cache/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar:" + path + "cache/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar:" + path + "cache/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar:" + path + "cache/com/google/guava/guava/16.0/guava-16.0.jar:" + path + "cache/org/apache/commons/commons-lang3/3.2.1/commons-lang3-3.2.1.jar:" + path + "cache/commons-io/commons-io/2.4/commons-io-2.4.jar:" + path + "cache/commons-codec/commons-codec/1.9/commons-codec-1.9.jar:" + path + "cache/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" + path + "cache/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" + path + "cache/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar:" + path + "cache/com/mojang/authlib/1.5.16/authlib-1.5.16.jar:" + path + "cache/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar:" + path + "cache/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar:" + path + "cache/org/lwjgl/lwjgl/lwjgl/2.9.1/lwjgl-2.9.1.jar:" + path + "cache/org/lwjgl/lwjgl/lwjgl_util/2.9.1/lwjgl_util-2.9.1.jar:" + path + "cache/tv/twitch/twitch/5.16/twitch-5.16.jar:" + path + "modpacks/tekkitmain/bin/modpack.jar:" + path + "modpacks/tekkitmain/bin/minecraft.jar",
            "-cp \"" + path + "cache/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar\":\"" + path + "cache/org/ow2/asm/asm-all/5.0.3/asm-all-5.0.3.jar\":\"" + path + "cache/com/typesafe/akka/akka-actor_2.11/2.3.3/akka-actor_2.11-2.3.3.jar\":\"" + path + "cache/com/typesafe/config/1.2.1/config-1.2.1.jar\":\"" + path + "cache/org/scala-lang/scala-actors-migration_2.11/1.1.0/scala-actors-migration_2.11-1.1.0.jar\":\"" + path + "cache/org/scala-lang/scala-compiler/2.11.1/scala-compiler-2.11.1.jar\":\"" + path + "cache/org/scala-lang/plugins/scala-continuations-library_2.11/1.0.2/scala-continuations-library_2.11-1.0.2.jar\":\"" + path + "cache/org/scala-lang/plugins/scala-continuations-plugin_2.11.1/1.0.2/scala-continuations-plugin_2.11.1-1.0.2.jar\":\"" + path + "cache/org/scala-lang/scala-library/2.11.1/scala-library-2.11.1.jar\":\"" + path + "cache/org/scala-lang/scala-parser-combinators_2.11/1.0.1/scala-parser-combinators_2.11-1.0.1.jar\":\"" + path + "cache/org/scala-lang/scala-reflect/2.11.1/scala-reflect-2.11.1.jar\":\"" + path + "cache/org/scala-lang/scala-swing_2.11/1.0.1/scala-swing_2.11-1.0.1.jar\":\"" + path + "cache/org/scala-lang/scala-xml_2.11/1.0.2/scala-xml_2.11-1.0.2.jar\":\"" + path + "cache/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar\":\"" + path + "cache/lzma/lzma/0.0.1/lzma-0.0.1.jar\":\"" + path + "cache/com/mojang/realms/1.2.9/realms-1.2.9.jar\":\"" + path + "cache/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar\":\"" + path + "cache/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar\":\"" + path + "cache/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar\":\"" + path + "cache/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar\":\"" + path + "cache/java3d/vecmath/1.3.1/vecmath-1.3.1.jar\":\"" + path + "cache/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar\":\"" + path + "cache/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar\":\"" + path + "cache/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar\":\"" + path + "cache/com/paulscode/codecwav/20101023/codecwav-20101023.jar\":\"" + path + "cache/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar\":\"" + path + "cache/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar\":\"" + path + "cache/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar\":\"" + path + "cache/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar\":\"" + path + "cache/com/google/guava/guava/17.0/guava-17.0.jar\":\"" + path + "cache/org/apache/commons/commons-lang3/3.3.2/commons-lang3-3.3.2.jar\":\"" + path + "cache/commons-io/commons-io/2.4/commons-io-2.4.jar\":\"" + path + "cache/commons-codec/commons-codec/1.9/commons-codec-1.9.jar\":\"" + path + "cache/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar\":\"" + path + "cache/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar\":\"" + path + "cache/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar\":\"" + path + "cache/com/mojang/authlib/1.5.13/authlib-1.5.13.jar\":\"" + path + "cache/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar\":\"" + path + "cache/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar\":\"" + path + "cache/org/lwjgl/lwjgl/lwjgl/2.9.1/lwjgl-2.9.1.jar\":\"" + path + "cache/org/lwjgl/lwjgl/lwjgl_util/2.9.1/lwjgl_util-2.9.1.jar\":\"" + path + "cache/tv/twitch/twitch/5.16/twitch-5.16.jar\":\"" + path + "bin/modpack.jar\":\"" + path + "bin/minecraft.jar\"",

            "net.minecraft.launchwrapper.Launch",
            "--username " + Username,
            "--version 1.7.10-Forge10.13.2.1236",
            "--gameDir \"" + path + "modpacks/tekkitmain\"",
            "--assetsDir \"" + path + "assets\"",
            "--assetIndex 1.7.10",
            "--uuid 0",
            "--accessToken 0",
            "--userProperties {}",
            "--userType legacy",
            "--tweakClass cpw.mods.fml.common.launcher.FMLTweaker",
            "--title Deincraft",

            "--icon \"" + path + "assets/packs/tekkitmain/icon.png\""
        };
            
        for (int i = 0; i <= 23 ;i++) {
        DCargs = DCargs + Startargs[i] + " ";
        }
        
        } else if (OS.contains("windows")) {
            System.out.println("using Windows");
            String[] Startargs = {
            "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump",
            "-Xmx" + RAM + "m",
            "-XX:MaxPermSize=256m",
            "-Djava.library.path=\"" + path + "/bin/natives\"",
            "-Dfml.core.libraries.mirror=http://mirror.technicpack.net/Technic/lib/fml/%s",
            "-Dminecraft.applet.TargetDirectory=\"" + path + "\"",
            "-cp \"" + path + "cache/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar\":\"" + path + "cache/org/ow2/asm/asm-all/5.0.3/asm-all-5.0.3.jar\":\"" + path + "cache/com/typesafe/akka/akka-actor_2.11/2.3.3/akka-actor_2.11-2.3.3.jar\":\"" + path + "cache/com/typesafe/config/1.2.1/config-1.2.1.jar\":\"" + path + "cache/org/scala-lang/scala-actors-migration_2.11/1.1.0/scala-actors-migration_2.11-1.1.0.jar\":\"" + path + "cache/org/scala-lang/scala-compiler/2.11.1/scala-compiler-2.11.1.jar\":\"" + path + "cache/org/scala-lang/plugins/scala-continuations-library_2.11/1.0.2/scala-continuations-library_2.11-1.0.2.jar\":\"" + path + "cache/org/scala-lang/plugins/scala-continuations-plugin_2.11.1/1.0.2/scala-continuations-plugin_2.11.1-1.0.2.jar\":\"" + path + "cache/org/scala-lang/scala-library/2.11.1/scala-library-2.11.1.jar\":\"" + path + "cache/org/scala-lang/scala-parser-combinators_2.11/1.0.1/scala-parser-combinators_2.11-1.0.1.jar\":\"" + path + "cache/org/scala-lang/scala-reflect/2.11.1/scala-reflect-2.11.1.jar\":\"" + path + "cache/org/scala-lang/scala-swing_2.11/1.0.1/scala-swing_2.11-1.0.1.jar\":\"" + path + "cache/org/scala-lang/scala-xml_2.11/1.0.2/scala-xml_2.11-1.0.2.jar\":\"" + path + "cache/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar\":\"" + path + "cache/lzma/lzma/0.0.1/lzma-0.0.1.jar\":\"" + path + "cache/com/mojang/realms/1.2.9/realms-1.2.9.jar\":\"" + path + "cache/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar\":\"" + path + "cache/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar\":\"" + path + "cache/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar\":\"" + path + "cache/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar\":\"" + path + "cache/java3d/vecmath/1.3.1/vecmath-1.3.1.jar\":\"" + path + "cache/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar\":\"" + path + "cache/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar\":\"" + path + "cache/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar\":\"" + path + "cache/com/paulscode/codecwav/20101023/codecwav-20101023.jar\":\"" + path + "cache/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar\":\"" + path + "cache/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar\":\"" + path + "cache/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar\":\"" + path + "cache/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar\":\"" + path + "cache/com/google/guava/guava/17.0/guava-17.0.jar\":\"" + path + "cache/org/apache/commons/commons-lang3/3.3.2/commons-lang3-3.3.2.jar\":\"" + path + "cache/commons-io/commons-io/2.4/commons-io-2.4.jar\":\"" + path + "cache/commons-codec/commons-codec/1.9/commons-codec-1.9.jar\":\"" + path + "cache/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar\":\"" + path + "cache/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar\":\"" + path + "cache/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar\":\"" + path + "cache/com/mojang/authlib/1.5.13/authlib-1.5.13.jar\":\"" + path + "cache/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar\":\"" + path + "cache/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar\":\"" + path + "cache/org/lwjgl/lwjgl/lwjgl/2.9.1/lwjgl-2.9.1.jar\":\"" + path + "cache/org/lwjgl/lwjgl/lwjgl_util/2.9.1/lwjgl_util-2.9.1.jar\":\"" + path + "cache/tv/twitch/twitch/5.16/twitch-5.16.jar\":\"" + path + "bin/modpack.jar\":\"" + path + "bin/minecraft.jar\"",
            "net.minecraft.launchwrapper.Launch",
            "--username " + Username,
            "--version 1.7.10-Forge10.13.4.1558",
            "--gameDir \"" + path + "\"",
            "--assetsDir \"" + path + "assets\"",
            "--assetIndex 1.7.10",
            "--uuid 0",
            "--accessToken 0",
            "--userProperties {}",
            "--userType legacy",
            "--tweakClass cpw.mods.fml.common.launcher.FMLTweaker",
            "--title Deincraft-Tekkit",
            "--icon " + "\"" + path + "assets/packs/tekkitmain/icon.png\""
        };
            
        for (int i = 0; i <= 19 ;i++) {
            DCargs = DCargs + Startargs[i] + " ";
        }
        
        } else { //linux
            String[] Startargs = {
            "",
            "-Xmx" + RAM + "m",
            "-XX:MaxPermSize=256m",
            "-Djava.library.path=" + path + "modpacks/tekkitmain/bin/natives",
            "-Dfml.core.libraries.mirror=http://mirror.technicpack.net/Technic/lib/fml/%s",
            "-Dminecraft.applet.TargetDirectory=" + path,
            "-cp \"" + path + "cache/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar\":\"" + path + "cache/org/ow2/asm/asm-all/5.0.3/asm-all-5.0.3.jar\":\"" + path + "cache/com/typesafe/akka/akka-actor_2.11/2.3.3/akka-actor_2.11-2.3.3.jar\":\"" + path + "cache/com/typesafe/config/1.2.1/config-1.2.1.jar\":\"" + path + "cache/org/scala-lang/scala-actors-migration_2.11/1.1.0/scala-actors-migration_2.11-1.1.0.jar\":\"" + path + "cache/org/scala-lang/scala-compiler/2.11.1/scala-compiler-2.11.1.jar\":\"" + path + "cache/org/scala-lang/plugins/scala-continuations-library_2.11/1.0.2/scala-continuations-library_2.11-1.0.2.jar\":\"" + path + "cache/org/scala-lang/plugins/scala-continuations-plugin_2.11.1/1.0.2/scala-continuations-plugin_2.11.1-1.0.2.jar\":\"" + path + "cache/org/scala-lang/scala-library/2.11.1/scala-library-2.11.1.jar\":\"" + path + "cache/org/scala-lang/scala-parser-combinators_2.11/1.0.1/scala-parser-combinators_2.11-1.0.1.jar\":\"" + path + "cache/org/scala-lang/scala-reflect/2.11.1/scala-reflect-2.11.1.jar\":\"" + path + "cache/org/scala-lang/scala-swing_2.11/1.0.1/scala-swing_2.11-1.0.1.jar\":\"" + path + "cache/org/scala-lang/scala-xml_2.11/1.0.2/scala-xml_2.11-1.0.2.jar\":\"" + path + "cache/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar\":\"" + path + "cache/lzma/lzma/0.0.1/lzma-0.0.1.jar\":\"" + path + "cache/com/mojang/realms/1.2.9/realms-1.2.9.jar\":\"" + path + "cache/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar\":\"" + path + "cache/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar\":\"" + path + "cache/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar\":\"" + path + "cache/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar\":\"" + path + "cache/java3d/vecmath/1.3.1/vecmath-1.3.1.jar\":\"" + path + "cache/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar\":\"" + path + "cache/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar\":\"" + path + "cache/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar\":\"" + path + "cache/com/paulscode/codecwav/20101023/codecwav-20101023.jar\":\"" + path + "cache/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar\":\"" + path + "cache/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar\":\"" + path + "cache/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar\":\"" + path + "cache/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar\":\"" + path + "cache/com/google/guava/guava/17.0/guava-17.0.jar\":\"" + path + "cache/org/apache/commons/commons-lang3/3.3.2/commons-lang3-3.3.2.jar\":\"" + path + "cache/commons-io/commons-io/2.4/commons-io-2.4.jar\":\"" + path + "cache/commons-codec/commons-codec/1.9/commons-codec-1.9.jar\":\"" + path + "cache/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar\":\"" + path + "cache/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar\":\"" + path + "cache/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar\":\"" + path + "cache/com/mojang/authlib/1.5.13/authlib-1.5.13.jar\":\"" + path + "cache/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar\":\"" + path + "cache/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar\":\"" + path + "cache/org/lwjgl/lwjgl/lwjgl/2.9.1/lwjgl-2.9.1.jar\":\"" + path + "cache/org/lwjgl/lwjgl/lwjgl_util/2.9.1/lwjgl_util-2.9.1.jar\":\"" + path + "cache/tv/twitch/twitch/5.16/twitch-5.16.jar\":\"" + path + "bin/modpack.jar\":\"" + path + "bin/minecraft.jar\"",
            "net.minecraft.launchwrapper.Launch",
            "--username " + Username,
            "--version 1.7.10-Forge10.13.2.1236",
            "--gameDir " + path + "modpacks/tekkitmain",
            "--assetsDir " + path + "assets",
            "--assetIndex 1.7.10",
            "--uuid 0",
            "--accessToken 0",
            "--userProperties {}",
            "--userType legacy",
            "--tweakClass cpw.mods.fml.common.launcher.FMLTweaker",
            "--title Deincraft-Tekkit",
            "--icon " + "\"" + path + "assets/packs/tekkitmain/icon.png\""
        };

        for (int i = 0; i <= 19 ;i++) {
            DCargs = DCargs + Startargs[i] + " ";
        }

        }
        
        System.out.println("Args:" + DCargs);
        return DCargs;
    }
}
