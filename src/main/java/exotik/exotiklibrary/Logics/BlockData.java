package exotik.exotiklibrary.Logics;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import exotik.exotiklibrary.Mechanics.Shorts;

public class BlockData {

    private static String path = "Database";
    private static String archive = "block_data";
    private static File file = FileManager.file(path,archive);
    private static final Yaml yaml = FileManager.yaml();


    // Create, Save a 'Information' in 'Block' and 'File' (String)
    public static void add(Block block, String id, String value) {
        block.setMetadata(id,new FixedMetadataValue(Shorts.plugin,value.toUpperCase()));
        save_in_file(block,id,value.toUpperCase());
    }

    // Create, Save a 'Information' in 'Block' and 'File' (Integer)
    public static void add(Block block, String id, Integer value) {
        block.setMetadata(id,new FixedMetadataValue(Shorts.plugin,value));
        save_in_file(block,id,value);        
    }

    // Create, Save a 'Information' in 'Block' and 'File' (Boolean)
    public static void add(Block block, String id, Boolean value) {
        block.setMetadata(id,new FixedMetadataValue(Shorts.plugin,value));
        save_in_file(block,id,value);
    }

    
    // Remove a 'Information' in 'Block' and 'File'
    public static void remove(Block block, String id) {
        block.removeMetadata(id,Shorts.plugin);
        remove_from_file(block,id);
    }
    
    // Clear all 'Information' in 'Block' and 'File'
    public static void clear(Block block) {
        for (MetadataValue id : block.getMetadata(Shorts.plugin.getName())) {
            if (id.getOwningPlugin().equals(Shorts.plugin)) {
                block.removeMetadata(id.getOwningPlugin().getName(),Shorts.plugin);
            }
        }
        delete_from_file(block);
        BlockInventory.delete(block);
    }

    // Return 'Information' from 'Block' (as 'String')
    public static String get(Block block, String id) {
        for (MetadataValue value : block.getMetadata(id)) {
            if (value.getOwningPlugin().equals(Shorts.plugin)) {
                return value.asString();
            }
        }
        
        return null;
    }
    
     // Return 'True' if 'Block' has 'Information' saved into, 'False' if don't
    public static Boolean has(Block block) {
        try (FileReader reader = new FileReader(file)) {
            // Map for 'Data'
            Map<String,Object> data = yaml.load(reader);
            if (data != null) {
                for (String key : data.keySet()) {
                    String location = location(block);

                    if (location.equalsIgnoreCase(key)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } catch (IOException error) {
            Shorts.logError(error.toString());
        }
        return false;
    }
    
    // Functions (Above)
    
    
    
    
    // Logics (Under)

    // Save 'Information' inside 'File' (String)
    private static <T> void save_in_file(Block block, String id, T value) {
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                
                // Location where 'Information' will be saved
                String location = location(block);
                
                // Custom 'Style' to 'File'
                DumperOptions options = new DumperOptions();
                options.setPrettyFlow(true);
                
                // File with 'Style'
                Yaml yaml = new Yaml(options);
                
                // 'Map' of the 'File'
                Map<String,Map<String,Object>> data = yaml.load(reader);
                
                // If 'Data' is null -> Create a new 'Map'
                if (data == null) {
                    data = new LinkedHashMap<>();
                }
                
                // New 'Map' for the actual 'Information'
                Map<String,Object> info = data.get(location);
                
                // If 'Info' is null -> Create a new 'Map'
                if (info == null) {
                    info = new LinkedHashMap<>();
                }
                
                // Add 'ID' and 'Value' in 'Info' Map
                info.put(id,value);
                
                // Add 'Info' in 'Data' map with 'Location' as 'ID'
                data.put(location,info);
                
                // Update 'File'
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(default_message());
                    yaml.dump(data,writer);
                }
            }  catch (IOException error) {
                Shorts.logError(error.toString());
            }
        } else {
            FileManager.create("Database","block_data");
        }
    }

    // Remove 'Information' inside 'File'
    private static void remove_from_file(Block block, String id) {

        if(file.exists()) {
            try (FileReader reader = new FileReader(file)){
                
                // Location where 'Information' will be saved
                String location = location(block);
                
                Yaml yaml = FileManager.yaml();
                
                // 'Map' of the 'File'
                Map<String,Map<String,Object>> data = yaml.load(reader);

                // If 'Data' is null -> Create a new 'Map'
                if (data == null) {
                    data = new LinkedHashMap<>();
                }

                // New 'Map' for the actual 'Information'
                Map<String,Object> info = data.get(location);
                
                // If 'Info' is null -> Create a new 'Map'
                if (info == null) {
                    info = new LinkedHashMap<>();
                }

                // Remove 'ID' and 'Value' in 'Info' Map
                info.remove(id);
                
                // Add 'Info' in 'Data' map with 'Location' as 'ID'
                data.put(location,info);

                // Update 'File'
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(default_message());
                    yaml.dump(data,writer);
                }
            } catch (IOException error) {
                Shorts.logError(error.toString());
            }
        }
    }
    
    // Delete all 'Information' inside 'File'
    private static void delete_from_file(Block block) {
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Yaml yaml = FileManager.yaml();
                
                // 'Map' of the 'File'
                Map<String,Map<String,Object>> data = yaml.load(reader);

                // If 'Data' is null -> Return
                if (data == null) {
                    return;
                }

                // Clear all 'Data'
                data.remove(location(block));
                
                // Update 'File'
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(default_message());
                    yaml.dump(data,writer);
                }
            }  catch (IOException error) {
                Shorts.logError(error.toString());
            }
        }
    }

    // Load all 'Information' inside 'File'
    public static void load() {
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                // 'Map' of 'Information' inside 'File'
                Map<String,Map<String,Object>> data = yaml.load(reader);
                
                // If 'Data' is valid
                if (data != null) {
                    // Return a 'String' with the 'Location' of 'Block'
                    for (String information : data.keySet()) {

                        // Split the 'Location' of 'Block'
                        String[] location = information.split(",");

                        World world = Bukkit.getWorld(location[0]);
                        Integer x = Integer.parseInt(location[1]);
                        Integer y = Integer.parseInt(location[2]);
                        Integer z = Integer.parseInt(location[3]);

                        // Return 'Block' at 'Location'
                        Block block = world.getBlockAt(x,y,z);

                        // Create a 'Map' with all 'Information' saved in 'Block'
                        Map<String,Object> block_information = data.get(information);

                        // Set each 'Information' in 'Block'
                        for (String id : block_information.keySet()) {
                            if (block_information.get(id) instanceof String) {
                                add(block,id, (String)block_information.get(id));
                            }
                            if (block_information.get(id) instanceof Integer) {
                                add(block,id, (Integer)block_information.get(id));
                            }
                        }
                    }
                    Shorts.logInfo("&aAll 'Block Data' was successfully loaded!");
                }
            } catch (IOException error) {
                Shorts.logError(error.toString());
            }
        } else {
            FileManager.create(path, archive);
        }
    }

    // Format 'Block Location' to a better 'String'
    public static String location(Block block) {

        String formatted = 
            block.getWorld().getName() + "," + 
            block.getX() + "," + 
            block.getY() + "," + 
            block.getZ();

        return formatted;
    }
    
    // Default message to 'File'
    public static String default_message() {
        StringBuilder text = new StringBuilder();
        text.append(FileManager.default_message());
        text.append("#\n");
        text.append("#   This file save ALL information of 'Blocks'\n");
        text.append("#   Please, do NOT edit or delete this file!\n");
        text.append("#   This file is VERY sensitive.\n");
        text.append("#\n");
        text.append(Shorts.break_line + "\n");

        return text.toString();
    }

}
