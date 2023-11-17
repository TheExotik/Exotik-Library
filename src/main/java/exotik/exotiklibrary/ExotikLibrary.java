package exotik.exotiklibrary;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import exotik.exotiklibrary.Events.BlockEvents;
import exotik.exotiklibrary.Items.Item;
import exotik.exotiklibrary.Logics.BlockData;
import exotik.exotiklibrary.Logics.BlockInventory;
import exotik.exotiklibrary.Logics.ItemFromFile;
import exotik.exotiklibrary.Mechanics.Shorts;
import exotik.exotiklibrary.Mechanics.BlockDataSaving;

public final class ExotikLibrary extends JavaPlugin{

    // Instance of this plugin
    private static ExotikLibrary instance;

    // Config file
    public static FileConfiguration config;


    // Things to happen when plugin Startup
    @Override
    public void onEnable() {
        // Initializing 'Instance'
        instance = this;
                
        // Logging 'Plugin Information'
        Shorts.logInfo(Shorts.console_end_text);
        Shorts.logInfo("&fThank you for using my plugin xD");
        Shorts.logInfo("&fHelp the &5Developer &fby making a donation: &b" + Shorts.donation);
        Shorts.logInfo("&fJoin our &9Discord Server&f: &b" + Shorts.discord);
        Shorts.logInfo(Shorts.space_line);
        
        // Loading 'Config' file
        saveDefaultConfig();
        config = Shorts.plugin.getConfig();

        // Load the 'block_data' File
        BlockData.load();
        BlockInventory.load();
        Shorts.logInfo(Shorts.space_line);
        
        // Initializing 'Items'
        Item.register();
        ItemFromFile.load();
        
        // Initializing 'Events'
        Shorts.event(new BlockEvents());
        
        // Initializing a 'Task' to save 'Block Inventories'
        Integer config_delay = config.getInt("DataSaving.BlockData");
        Integer save_delay = ((config_delay*20)*60);

        Shorts.logInfo("The 'Block Data' will be saved each: '&e" + config_delay + "&f' minutes!");            
        Shorts.logInfo(Shorts.console_end_text);
        new BlockDataSaving().runTaskTimer(Shorts.plugin,0,save_delay);
    }
    
    

    // Things to happen when plugin Shutdown
    @Override
    public void onDisable() {
        instance = null;
    }

    // Return the plugin instance
    public static ExotikLibrary getInstance() {return instance;}
}