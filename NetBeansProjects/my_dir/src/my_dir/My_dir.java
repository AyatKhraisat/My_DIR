/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_dir;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    //to arrange output in columns 
    private static final int NUMBER_OF_SPACES = -30;
    
    //Supported commands
    private static final String DIR_COMMAND = "dir";
    private static final String DIR_VIEW_OWNER_COMMAND = "dir/q";
    private static final String DIR_LOWER_CASE_COMMAND = "dir/L";

    //format numbers (thousands separator)   
    private NumberFormat numberFormat;
    
    /*check if option is selected
     the supported options is:
     /q (display the owener of the file)
     /l (display file name in lowercase) */
    private boolean displayOwner = false;
    private boolean nameInLowerCase = false;

  
    
    public static void main(String[] args) throws IOException {
       
        
        My_dir obj = new My_dir();
        
        obj.numberFormat = NumberFormat.getNumberInstance();
        
        //read the user command
        Scanner s = new Scanner(System.in);
        String input = s.nextLine().replaceAll("\\s", "");

             File currentDirectory = new File(".");
    
            FileStore store = Files.getFileStore(currentDirectory.toPath());
        //check if command is supported
        if (input.equalsIgnoreCase(DIR_COMMAND) || input.equalsIgnoreCase(DIR_VIEW_OWNER_COMMAND)
                || input.equalsIgnoreCase(DIR_LOWER_CASE_COMMAND)) {

            if (input.equalsIgnoreCase(DIR_VIEW_OWNER_COMMAND)) {
                obj.displayOwner = true;
            } else if (input.equalsIgnoreCase(DIR_LOWER_CASE_COMMAND)) {
                obj.nameInLowerCase = true;
            }

            String root = Paths.get(currentDirectory.getAbsolutePath()).getRoot().toString();
            System.out.print("Volume in drive " + root.substring(0, 1).toUpperCase() + " ");

            if (!store.name().equals("") && store.name() != null) {
                System.out.println(store.name());
            } else {
                System.out.println("has no label.");
            }
            System.out.println("\nDirectory of " + currentDirectory.getCanonicalPath()+ "\n");
            obj.getAllFiles(currentDirectory);
        }
        else {
            System.out.println("command not supported");
        }
    }

    private void getAllFiles(File curDir) throws IOException {

        File[] filesList = curDir.listFiles();
        int directoriesCounter = 0;
        int filesCounter = 0;
        long filesSize = 0;
        for (File f : filesList) {
            if ((f.isDirectory() || f.isFile()) && !f.isHidden()) {
                System.out.print(String.format("%" + NUMBER_OF_SPACES + "s", getCreationDateTime(f)));

                if (f.isDirectory()) {

                    System.out.print(String.format("%" + NUMBER_OF_SPACES + "s", "<DIR>"));

                    directoriesCounter++;

                } else if (f.isFile()) {
                    System.out.print(String.format("%" + -NUMBER_OF_SPACES + "s", numberFormat.format(f.length()) + " "));

                    filesCounter++;
                    filesSize += f.length();

                }
                if (displayOwner) {
                    try {
                        System.out.print(String.format("%" + NUMBER_OF_SPACES + "s", Files.getOwner(f.toPath()).getName()));
                    } catch (IOException e) {
                        System.out.print(String.format("%" + NUMBER_OF_SPACES + "s", "..."));
                    }
                }
                String fileName = f.getName();
                if (nameInLowerCase) {
                    fileName = fileName.toLowerCase();
                }
                System.out.println(String.format("%" + NUMBER_OF_SPACES + "s", fileName));
            }
        }

        FileStore store = Files.getFileStore(curDir.toPath());
        System.out.println(filesCounter+" File<S>    "+ numberFormat.format(filesSize) + " bytes ");
        System.out.println(directoriesCounter+" Dir<S>    "+numberFormat.format(store.getUsableSpace()) + " bytes free");

    }

    //return file date and time 
    public static String getCreationDateTime(File file) throws IOException {

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        Date creationDate = new Date(attr.lastModifiedTime().to(TimeUnit.MILLISECONDS));
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy  hh:mm a");
        format.setTimeZone(TimeZone.getDefault());

        return format.format(creationDate);

    }

}
