/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_dir;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author dell
 */
public class My_dir {

    public static void main(String[] args) throws IOException {

        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        
         if (input.equalsIgnoreCase("dir")) {
            File curDir = new File(".");
     System.out.println("Label"+getVolume(curDir.getPath()));
  
            getAllFiles(curDir);
        }
    }

    private static String getVolume (String path){
        FileSystemView view = FileSystemView.getFileSystemView();
  File dir = new File(path);
  String name = view.getSystemDisplayName(dir);
  if (name == null) { return null; }
  name = name.trim();
  if (name == null || name.length() < 1) {
   return null;
  }
  int index = name.lastIndexOf(" (");
  if (index > 0) {
    name = name.substring(0, index);
   }
 return name;}
               private static void getAllFiles(File curDir) throws IOException {

        File[] filesList = curDir.listFiles();
        int dir = 0;
        int file = 0;
        long fileSize = 0;
        for (File f : filesList) {

            if (f.isDirectory() && !f.isHidden()) {

                System.out.print(getCreationDateTime(f) + "  ");
                
              try{
                System.out.print(Files.getOwner(f.toPath()).getName() + "  ");
              }
              catch(IOException e){
                  System.out.print("..."+"  ");
              }
                System.out.println(f.getName());
                dir++;

            }

            else if (f.isFile() && !f.isHidden()) {
                System.out.print(getCreationDateTime(f) + "  ");
                System.out.print(Files.getOwner(f.toPath()).getName() + "  ");
                System.out.printf("%,d  ", f.length());
                System.out.println(f.getName());
                file++;
                fileSize += f.length();

            }
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        FileStore store = Files.getFileStore(curDir.toPath());
        System.out.println(file + " File<s>     " + nf.format(fileSize) + " bytes ");
        System.out.println(dir + " Dir<s>     " + nf.format(store.getUsableSpace()) + " bytes free");

    }
public static boolean containsSymlink(File file) throws IOException {
  return file.getCanonicalFile().equals(file.getAbsoluteFile());
}
    public static String getCreationDateTime(File file) throws IOException {

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        Date creationDate = new Date(attr.lastModifiedTime().to(TimeUnit.MILLISECONDS));
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy  hh:mm a");
        format.setTimeZone(TimeZone.getDefault());

        return format.format(creationDate);

    }

}
