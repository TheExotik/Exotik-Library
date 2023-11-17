package exotik.exotiklibrary.Logics;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import exotik.exotiklibrary.Mechanics.Shorts;

public class FileManager {

    private static String main = Shorts.plugin.getDataFolder() + File.separator;
    
    // Create a 'File'
    public static void create(String folder, String archive) {
        File file = file(folder,archive);
        String path = file.getPath();
        String name = file.getName();
        
        // If 'Folder' didn't exist -> Create 'Folder'
        if (!local(folder).exists()) {
            FolderManager.create(folder); 
        }

        // If 'File' didn't exist -> Create a new 'File'
        // If 'File' cannot be created -> Return 'Error while try create a new file' message
        // Otherwise -> Return 'File already exist' message
        if (!file.exists()) {
            try {
                file.createNewFile();

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(default_message());
                }

            } catch(IOException error) {Shorts.logError(error.toString());
            }
        } else {
            Shorts.logWarning("Already exist a file named: '&a" + name + "&f' at: '&e" + path + "&f'");
        }
    }

    // Delete 'File'
    public static void delete(String folder, String archive) {
        File file = file(folder,archive);
        String path = file.getPath();
        String name = file.getName();
        
        // If 'File' exist -> Delete 'File'
        // If 'File' cannot be deleted -> Return 'Error while try delete file' message
        // Otherwise -> Return 'File do not exist' message
        if (file.exists()) {
            if (file.delete()) {
                Shorts.logWarning("Deleting file named: '&a" + name + "&f' from: '&e" + path +"&f'");
            }
        } else {
            Shorts.logError(file_not_found(name,path));
        }
    }
    
    // Write something inside the 'File'
    public static void write(String folder, String archive, String id, String information) {
        File file = file(folder,archive);
        String path = file.getPath();
        String name = file.getName();
        
        // If 'File' exist -> Write something inside
        // If cannot write -> Return 'Error while try write in file xxxx.yml' message
        // Otherwise -> Return 'File not found' message
        if (file.exists()) {
            try {

                // Make the file look better
                DumperOptions format = new DumperOptions();
                format.setPrettyFlow(true);

                Yaml yaml = new Yaml(format);
                // Map of 'Information'
                Map<String, Object> map;

                // Read the current 'File'
                try (FileReader reader = new FileReader(file)) {
                    map = yaml.load(reader);
                }
                if (map == null) {
                    map = new LinkedHashMap<>();
                }

                // If 'ID' do not exist -> Create
                if (!map.containsKey(id)) {

                    // Add 'ID + Information' inside 'Map'
                    map.put(id,information);
                    
                    // Thing that will 'Write' inside 'File'
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(default_message());
                        yaml.dump(map,writer);
                    }                    
                }
            } catch (IOException error) {
                Shorts.logError(error.toString());
            }
        } else {
            Shorts.logError(file_not_found(name,path));
        }
    }
   
    // Erase something inside the 'File'
    public static void erase(String folder, String archive, String id) {
        File file = file(folder,archive);
        String path = file.getPath();
        String name = file.getName();
        
        if (file.exists()) {
            
            try {
                // Make the file look better
                DumperOptions format = new DumperOptions();
                format.setPrettyFlow(true);

                Yaml yaml = new Yaml(format);
                // Map of 'Information'
                Map<String, Object> map;

                // Read the current 'File'
                try (FileReader reader = new FileReader(file)) {
                    map = yaml.load(reader);
                }
                if (map == null) {
                    map = new LinkedHashMap<>();
                }
                
                if (map.containsKey(id)) {
                    map.remove(id);

                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(default_message());
                        yaml.dump(map,writer);
                    }

                }
            } catch (IOException error) {
                Shorts.logError(error.toString());
            }
        } else {
            Shorts.logError(file_not_found(name,path));
        }
    }

    // Format 'Folder' name to actual 'Path' format
    public static String path(String folder) {
        return folder.replace(".",File.separator);
    }

    // Return 'Local' of the folder
    public static File local(String folder) {
        return new File(main,path(folder));
    }

    // Return 'File'
    public static File file(String folder, String archive) {
        return new File(local(folder),archive + ".yml");
    }
    
    // Return 'String' of 'Default Message'
    public static String default_message() {
        // Date format
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = format.format(new Date());

        // String
        StringBuilder text = new StringBuilder();

        // Information
        text.append(Shorts.break_line + "\n");
        text.append("#\n");
        text.append("#   Plugin made by : The_Exotik\n");
        text.append("#   Plugin version : " + Shorts.version() + "\n");
        text.append("#   Discord server : " + Shorts.discord + "\n");
        text.append("#   Donations      : " + Shorts.donation + "\n");
        text.append("#   File generated : " + date + "\n");
        text.append("#   Server version : " + Shorts.server().getVersion() + "\n");
        text.append("#\n");
        text.append(Shorts.break_line + "\n");

        return text.toString();
    }

    // Return 'File not Found' message
    public static String file_not_found(String name, String path) {
        return "Cannot find a file named: '&a" + name + "&f' at: '&e" + path + "&f'";
    }

    // Style for 'File'
    public static Yaml yaml() {
        // Make the file look better
        DumperOptions format = new DumperOptions();
        format.setPrettyFlow(true);

        return new Yaml(format);
    }

}
