package exotik.exotiklibrary.Logics;

import java.io.File;
import exotik.exotiklibrary.Mechanics.Shorts;

public class FolderManager {
    
    private static final String main = Shorts.plugin.getDataFolder() + File.separator;

    // Create a new 'Folder'
    public static void create(String name) {
        String sub = name.replace(".", File.separator);
        File folder = new File(main,sub);
        String path = folder.getPath();
        String nickname = folder.getName();


        if (!folder.exists()) {
            folder.mkdirs();
        } else {
            Shorts.logWarning("Already exist a folder named: '&a" + nickname + "&f' at: '&e" + path + "&f'");
        }
    }

    // Delete a 'Folder'
    public static void delete(String name) {
        File folder = new File(main,name);
        String path = folder.getPath();
        String nickname = folder.getName();

        if (folder.exists()) {
            if (empty(folder)) {
                if (folder.delete()) {
                    Shorts.logWarning("Deleting folder named: '&a" + nickname + "&f' from: '&e" + path + "&f'");
                } else {
                    Shorts.logError("Cannot delete folder named: '&a" + nickname + "&f' at: '&e" + path +"&f'");
                }
            } else {
                Shorts.logError("Cannot deleted folder named: '&a" + nickname + "&f' at: '&e" + path +"&f' because theres more folder/file inside");
            }
        } else {
            Shorts.logError(folder_not_found(name,path));
        }
    }

    // Verify if 'Folder' is 'Empty' or not
    public static Boolean empty(File folder) {
        if (folder != null && folder.isDirectory()) {
            String[] files = folder.list();
            return files == null || files.length == 0;
        }
        return true;
    }

    // Return 'File not Found' message
    public static String folder_not_found(String name, String path) {
        return "Cannot find a folder named: '&a" + name + "&f' at: '&e" + path + "&f'";
    }

}
