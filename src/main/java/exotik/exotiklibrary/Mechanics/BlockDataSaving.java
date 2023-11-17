package exotik.exotiklibrary.Mechanics;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.Yaml;

import exotik.exotiklibrary.ExotikLibrary;
import exotik.exotiklibrary.Logics.BlockData;
import exotik.exotiklibrary.Logics.BlockInventory;
import exotik.exotiklibrary.Logics.FileManager;

public class BlockDataSaving extends BukkitRunnable {

    // File of 'block_data'
    private static final String path = "Database";
    private static final String archive = "block_data";
    private static final File file = FileManager.file(path,archive);
    private static final Yaml yaml = FileManager.yaml();

    @Override
    public void run() {
        int saved = 0;
        List<Block> list = new ArrayList<>();

        try (FileReader reader = new FileReader(file)) {

            // Map for 'Data'
            Map<String,Map<String,Object>> data = yaml.load(reader);

            if (data != null) {                    

                // Return a 'String' with the 'Location' of 'Block'
                for (String information : data.keySet()) {

                    // Split the 'Location' of 'Block'
                    String[] block_location = information.split(",");

                    World world = Bukkit.getWorld(block_location[0]);
                    Integer x = Integer.parseInt(block_location[1]);
                    Integer y = Integer.parseInt(block_location[2]);
                    Integer z = Integer.parseInt(block_location[3]);

                    // Return 'Block' at 'Location'
                    Block block = world.getBlockAt(x,y,z);

                    // Better 'Location'
                    String location = BlockData.location(block);

                    // Map for 'Block' location
                    Map<String,Object> block_data = data.get(location);

                    // Check if the block have 'Inventory'
                    if (block_data.get("Inventory") != null) {
                        list.add(block);
                    }

                }
            }
            
            if (list != null) {
                Shorts.logInfo(Shorts.console_end_text);
                Shorts.logInfo("Saving all 'Block Data'... this can take a moment.");

                for (Block each : list) {
                    if (each != null) { 
                        BlockInventory.save(each);
                        saved++;
                    }
                }

                Integer delay = ExotikLibrary.config.getInt("DataSaving.BlockData");
                Shorts.logInfo(Shorts.space_line);
                Shorts.logInfo("The total of '&e" + saved + "&f' 'Block Data' was successfully saved!");
                Shorts.logInfo("Next saving in: '&e" + delay + "&f' minutes.");
                Shorts.logInfo("You can change it in '&aConfig.yml&f' file!");
                Shorts.logInfo(Shorts.console_end_text);
            }
        } catch (IOException error) {
            Shorts.logError(error.toString());
        }
    }
    
}
