/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_dir;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {

    //to arrange output in columns 
    private static final int NUMBER_OF_SPACES = -30;

    public static void printAllFiles(File curDir, boolean displayOwner, boolean nameInLowerCase) throws IOException {

        File[] filesList = curDir.listFiles();
        FileStore store = Files.getFileStore(curDir.toPath());
        NumberFormat numberFormat = NumberFormat.getInstance();
        int directoriesCounter = 0;
        int filesCounter = 0;
        long filesSize = 0;
        System.out.println("\nDirectory of " + curDir.getCanonicalPath() + "\n");
        StringBuilder stringBuilder = new StringBuilder();
        for (File file : filesList) {
            if ((file.isDirectory() || file.isFile()) && !file.isHidden()) {
                stringBuilder.append(String.format("%" + NUMBER_OF_SPACES + "s", getModifiedDateTime(file)));

                if (file.isDirectory()) {

                    stringBuilder.append(String.format("%" + NUMBER_OF_SPACES + "s", "<DIR>"));

                    directoriesCounter++;

                } else if (file.isFile()) {
                    stringBuilder.append(String.format("%" + -NUMBER_OF_SPACES + "s", numberFormat.format(file.length()) + " "));

                    filesCounter++;
                    filesSize += file.length();

                }
                if (displayOwner) {
                    try {
                        stringBuilder.append(String.format("%" + NUMBER_OF_SPACES + "s", Files.getOwner(file.toPath()).getName()));
                    } catch (IOException e) {
                        stringBuilder.append(String.format("%" + NUMBER_OF_SPACES + "s", "..."));
                    }
                }
                String fileName = file.getName();
                if (nameInLowerCase) {
                    fileName = fileName.toLowerCase();
                }
                stringBuilder.append(String.format("%" + NUMBER_OF_SPACES + "s", fileName)).append("\n");
            }
        }

        stringBuilder.append(filesCounter).append(" File<s>    ").append(numberFormat.format(filesSize))
                .append(" bytes ").append("\n");

        stringBuilder.append(directoriesCounter).append(" Dir<s>    ").append(numberFormat.format(store.getUsableSpace()))
                .append(" bytes free").append("\n");
        System.out.print(stringBuilder.toString());

    }

    //return last modifidied date and time of the file 
    private static String getModifiedDateTime(File file) throws IOException {

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        Date creationDate = new Date(attr.lastModifiedTime().to(TimeUnit.MILLISECONDS));
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy  hh:mm a");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(creationDate);

    }

    public static void printVolumeInfo(File currentDirectory) throws IOException {

        FileStore store = Files.getFileStore(currentDirectory.toPath());

        String root = Paths.get(currentDirectory.getAbsolutePath()).getRoot().toString();
        System.out.print("Volume in drive " + root.substring(0, 1).toUpperCase() + " ");

        if (!store.name().equals("") && store.name() != null) {
            System.out.println(store.name());
        } else {
            System.out.println("has no label.");
        }

    }

}
