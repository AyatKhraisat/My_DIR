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

    //Supported commands
    private static final String DIR_COMMAND = "dir";
    private static final String DIR_VIEW_OWNER_COMMAND = "dir/Q";
    private static final String DIR_LOWER_CASE_COMMAND = "dir/L";
    private static final String DIR_SUB_DIRS_COMMAND = "dir/S";

    /*check if option is selected
     the supported options is:
     /q (display the owener of the file)
     /l (display file name in lowercase) */
    private boolean displayOwner = false;
    private boolean nameInLowerCase = false;

    public static void main(String[] args) throws IOException {

        My_dir myDir = new My_dir();

        //read the user command
        Scanner s = new Scanner(System.in);

        //to allow user to enter command with space or not ex: dir/Q or dir /Q
        String input = s.nextLine().replaceAll("\\s", "");

        File currentDirectory = new File(".");

        //check if command is supported
        if (input.equalsIgnoreCase(DIR_COMMAND)) {
            Utils.printVolumeInfo(currentDirectory);
            Utils.printAllFiles(currentDirectory, myDir.displayOwner, myDir.nameInLowerCase);

        } else if (input.equalsIgnoreCase(DIR_VIEW_OWNER_COMMAND)) {
            myDir.displayOwner = true;
            Utils.printVolumeInfo(currentDirectory);
            Utils.printAllFiles(currentDirectory, myDir.displayOwner, myDir.nameInLowerCase);
        } else if (input.equalsIgnoreCase(DIR_LOWER_CASE_COMMAND)) {
            myDir.nameInLowerCase = true;
            Utils.printVolumeInfo(currentDirectory);
            Utils.printAllFiles(currentDirectory, myDir.displayOwner, myDir.nameInLowerCase);
        } else if (input.equalsIgnoreCase(DIR_SUB_DIRS_COMMAND)) {
            Utils.printVolumeInfo(currentDirectory);
            FileVisitor fileVisitor = new FileVisitor();
            Files.walkFileTree(currentDirectory.toPath(), fileVisitor);
        } else {
            System.out.println("commands: dir, dir/Q ,dir/L, dir/S only supported ");
        }
    }

}
