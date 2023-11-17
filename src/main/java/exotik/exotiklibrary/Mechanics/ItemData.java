package exotik.exotiklibrary.Mechanics;

import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemData {
    
    // Set a 'Information' into a 'ItemStack' (Default is 'none')
    public static void set(ItemStack item, String information) {
        set(item, information, "none");
    }
    
    // Set a 'Information' into a 'ItemStack' (as 'String')
    public static void set(ItemStack item, String information, String value) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Shorts.plugin, information);
        
        data.set(key, PersistentDataType.STRING, value.toUpperCase());

        item.setItemMeta(meta);
    }
    
    // Set a 'Information' into a 'ItemStack' (as 'Integer')
    public static void set(ItemStack item, String information, Integer value) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Shorts.plugin, information);
        
        data.set(key, PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
    }
    
    // Return the 'Value' of the 'Information' saved inside some Item (as 'String')
    public static String asString(ItemStack item, String information) {
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Shorts.plugin, information);
        
        return data.get(key, PersistentDataType.STRING); 
    }

    // Return the 'Value' of the 'Information' saved inside some Item (as 'Integer')
    public static Integer asInteger(ItemStack item, String information) {
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Shorts.plugin, information);

        return data.get(key, PersistentDataType.INTEGER);
    }

    // Return all 'Value' saved in 'Item'
    public static Set<NamespacedKey> all(ItemStack item) {
        if (item.hasItemMeta()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

            return data.getKeys();
        }

        return null;
    }

    // Delete a 'Information' saved inside some Item
    public static void del(ItemStack item, String information) {
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Shorts.plugin, information);

        data.remove(key);
    }

    // Remove a 'Value' of the 'Information' saved inside some item (as 'Integer')
    public static void remove(ItemStack item, String information, Integer value) {
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Shorts.plugin, information);

        int before = data.get(key, PersistentDataType.INTEGER);
        int after = before - value;

        if (value < 0) {
            Shorts.logWarning("&fYou only can remove positive numbers!");
            return;
        }
        else if ((before - after) < 0) {
            Shorts.logWarning("&fThe result most be positive!");
            return;
        } else {
            data.set(key, PersistentDataType.INTEGER, after);
        }
    }
    
    // Add a 'Value' to the current 'Value' of the 'Information' saved inside some item (as 'Integer')
    public static void add(ItemStack item, String information, Integer value) {
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Shorts.plugin, information);

        int current = data.get(key, PersistentDataType.INTEGER);
        int after = current + value;

        if (value < 0) {
            Shorts.logWarning("&fYou only can add positive numbers");
            return;
        } else {
            data.set(key, PersistentDataType.INTEGER, after);
        }
    }

    // Change the boolean from 'True' to 'False' (vice versa)
    public static Boolean change(ItemStack item, String information) {
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Shorts.plugin, information);

        String current = data.get(key, PersistentDataType.STRING);

        if (current.equalsIgnoreCase("true")) {
            data.set(key, PersistentDataType.STRING, "false");
        } 
        else if (current.equalsIgnoreCase("false")) {
            data.set(key, PersistentDataType.STRING, "true");
        } else {
            Shorts.logWarning("&fYou only can change values of 'True' and 'False. Receive: " + current);
        }

        return false;
    }

    // Return 'True' if 'Item' has 'Information' saved into, 'False' if don't
    public static Boolean has(ItemStack item, String information) {
        if (item.hasItemMeta()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(Shorts.plugin, information);
            
            if (data.has(key, PersistentDataType.STRING)) {
                return true;
            } else if (data.has(key, PersistentDataType.INTEGER)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //  Return 'True' if 'Item' has any 'Information' saved into, 'False' if don't
    public static Boolean has(ItemStack item) {
        if (item.hasItemMeta()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

            return !data.getKeys().isEmpty();
        }
        return false;
    }

}
