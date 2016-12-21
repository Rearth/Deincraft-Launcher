/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deincraftlauncher.IO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Darkp
 */
public class ZIPExtractor {
    
    public static void extractArchive(String SourceFilePath, String TargetPath) throws Exception {
        
        File archive = new File (SourceFilePath);
        File destDir = new File(TargetPath);
        
        if (!destDir.exists()) {
            destDir.mkdir();
        }
 
        try (ZipFile zipFile = new ZipFile(archive)) {
            Enumeration entries = zipFile.entries();
            
            byte[] buffer = new byte[16384];
            int len;
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                
                String entryFileName = entry.getName();
                
                File dir = buildDirectoryHierarchyFor(entryFileName, destDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                if (!entry.isDirectory()) {
                    BufferedInputStream bis;
                    try (BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(new File(destDir, entryFileName)))) {
                        bis = new BufferedInputStream(zipFile.getInputStream(entry));
                        while ((len = bis.read(buffer)) > 0) {
                            bos.write(buffer, 0, len);
                        }   bos.flush();
                    }
                    bis.close();
                }
            }
        }
        System.out.println("Entpacken fertig");
        archive.delete();
    }
 
    private static File buildDirectoryHierarchyFor(String entryName, File destDir) {
        
        int lastIndex = entryName.lastIndexOf('/');
        
        String internalPathToEntry = entryName.substring(0, lastIndex + 1);
        
        return new File(destDir, internalPathToEntry);
    } 
}
