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
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author dell
 */
public class My_dir {

    //to arrange output in columns 
    private static final int NUMBER_OF_SPACES = -30;

    //Supported commands
    private static final String DIR_COMMAND = "dir";
    private static final String DIR_VIEW_OWNER_COMMAND = "dir/Q";
    private static final String DIR_LOWER_CASE_COMMAND = "dir/L";

    //format numbers (thousands separator)   
    private NumberFormat numberFormat;

    /*check if option is selected
     the supported options is:
     /q (display the owener of the file)
     /l (display file name in lowercase) */
    private boolean displayOwner = false;
    private boolean nameInLowerCase = false;

    private FileStore store;

    public static void main(String[] args) throws IOException {

        My_dir myDir = new My_dir();

        myDir.numberFormat = NumberFormat.getNumberInstance();

        //read the user command
        Scanner s = new Scanner(System.in);

        //to allow user to enter command with space or not ex: dir/Q or dir /Q
        String input = s.nextLine().replaceAll("\\s", "");

        File currentDirectory = new File(".");

        myDir.store = Files.getFileStore(currentDirectory.toPath());

        //check if command is supported
        if (input.equalsIgnoreCase(DIR_COMMAND) || input.equalsIgnoreCase(DIR_VIEW_OWNER_COMMAND)
                || input.equalsIgnoreCase(DIR_LOWER_CASE_COMMAND)) {
            if (input.equalsIgnoreCase(DIR_VIEW_OWNER_COMMAND)) {
                myDir.displayOwner = true;
            } else if (input.equalsIgnoreCase(DIR_LOWER_CASE_COMMAND)) {
                myDir.nameInLowerCase = true;
            }

            String root = Paths.get(currentDirectory.getAbsolutePath()).getRoot().toString();
            System.out.print("Volume in drive " + root.substring(0, 1).toUpperCase() + " ");

            if (!myDir.store.name().equals("") && myDir.store.name() != null) {
                System.out.println(myDir.store.name());
            } else {
                System.out.println("has no label.");
            }
            System.out.println("\nDirectory of " + currentDirectory.getCanonicalPath() + "\n");
            myDir.printAllFiles(currentDirectory);
        } else {
            System.out.println("commands: dir, dir/q ,dir/l only supported ");
        }
    }

    private void printAllFiles(File curDir) throws IOException {

        File[] filesList = curDir.listFiles();
        int directoriesCounter = 0;
        int filesCounter = 0;
        long filesSize = 0;
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
    private String getModifiedDateTime(File file) throws IOException {

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        Date creationDate = new Date(attr.lastModifiedTime().to(TimeUnit.MILLISECONDS));
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy  hh:mm a");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(creationDate);

    }

}
