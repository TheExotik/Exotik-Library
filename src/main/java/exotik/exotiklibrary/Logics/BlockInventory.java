package exotik.exotiklibrary.Logics;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Multimap;

import exotik.exotiklibrary.Mechanics.ItemData;
import exotik.exotiklibrary.Mechanics.Shorts;


public class BlockInventory {

    // File of 'block_data'
    private static final String path = "Database";
    private static final String archive = "block_data";
    private static final File file = FileManager.file(path,archive);
    private static final Yaml yaml = FileManager.yaml();

    // Map for 'Inventory Location'
    private static Map<String,Inventory> inventory_data = new HashMap<>();
    private static Map<String,Object> inventory_information = new HashMap<>();

    // Create a 'inventory' -> Save 'Data' in 'File'
    public static void create(Block block, Integer size, String title) {
        Inventory inventory = Shorts.server().createInventory(null,size,title);
        String location = location(block);

        inventory_data.put(location,inventory);
        inventory_information.put("Title",title);
        inventory_information.put("Size",size);

        save(block);
    }

    // Delete a 'inventory'
    public static void delete(Block block) {
        inventory_data.remove(location(block));        
    }

    // Open a 'inventory' saved in
    public static void open(Block block, Player player) {
        Inventory inventory = inventory_data.get(location(block));

        if (inventory != null) {
            player.openInventory(inventory);
        }
    }

    // Drop all 'Contents' inside 'inventory'
    public static void drop(Block block, Location location) {
        Inventory inventory = inventory_data.get(location(block));

        if (inventory != null) {
            ItemStack[] contents = inventory.getContents();

            if (contents != null) {
                for (ItemStack item : contents) {
                    if (item != null) {
                        location.getWorld().dropItemNaturally(location,item);
                    }
                }
            }
        }
    }
    
    // Save a 'inventory' in 'File'
    public static void save(Block block) {
        // Block information
        String location = location(block);
        Inventory inventory = inventory_data.get(location);
        
        if (file.exists()) {
            if (inventory != null) {
                try (FileReader reader = new FileReader(file)) {
                    // Map for 'Data'
                    Map<String,Map<String,Map<String,Object>>> data = yaml.load(reader);
                    if (data == null) {
                        data = new LinkedHashMap<>();
                    }

                    // Map for 'Block Data'
                    Map<String,Map<String,Object>> block_data = data.get(location);
                    if (block_data == null) {
                        block_data = new LinkedHashMap<>();
                    }

                    // Map for 'Inventory Data'
                    Map<String,Object> inventory_map = new LinkedHashMap<>();
                    inventory_map.put("Title", inventory_information.get("Title"));
                    inventory_map.put("Size", inventory_information.get("Size"));

                    // Map for each 'ItemStack'
                    Map<String,Object> list = new LinkedHashMap<>();

                    for (int i = 0; i < inventory.getSize(); i++) {
                        ItemStack item = inventory.getItem(i);

                        if (item != null) {
                            Map<String,Object> item_map = new LinkedHashMap<>();
                            ItemMeta item_meta = item.getItemMeta();

                            if (item_meta != null) {
                                if (item_meta.hasDisplayName()) {
                                    item_map.put("Name",item.getItemMeta().getDisplayName());
                                }
                                item_map.put("Type",item.getType().toString());
                                item_map.put("Amount",item.getAmount());

                                if (item_meta.hasLore()) {
                                    List<String> lore = item_meta.getLore();
                                    item_map.put("Lore",lore);
                                }
                                if (item_meta.hasEnchants()) {
                                    Map<Enchantment, Integer> enchantments_list = item_meta.getEnchants();

                                    Map<String,Object> enchant_map = new LinkedHashMap<>();
                                    
                                    for (Enchantment enchant : enchantments_list.keySet()) {
                                        String name = enchant.getKey().getKey();
                                        Map<String,Object> enchant_data = new LinkedHashMap<>();
                                        
                                        enchant_data.put("Enchantment Key",enchant.getKey().toString());
                                        enchant_data.put("Enchantment Level",enchantments_list.get(enchant));

                                        enchant_map.put(name,enchant_data);
                                    }

                                    item_map.put("Enchantments List",enchant_map);
                                }
                                if (item_meta.hasAttributeModifiers()) {
                                    Multimap<Attribute,AttributeModifier> attributes = item_meta.getAttributeModifiers();
                                    item_map.put("Attributes",attributes);
                                }

                                // Map for 'Visual Effects'
                                Map<String,Object> visual_effects = new LinkedHashMap<>();
                                Boolean has_cmd = item_meta.hasCustomModelData();
                                Boolean has_hide_effect = ItemData.has(item,"hide_enchants");

                                if (has_cmd || has_hide_effect) {
                                    if (has_cmd) {
                                        visual_effects.put("Custom Model Data", item_meta.getCustomModelData());
                                    }
                                    if (has_hide_effect) {
                                        visual_effects.put("Hide Enchants", true);
                                    }
                                    item_map.put("Visual Effects",visual_effects);
                                }
                                Damageable item_damage = (Damageable) item_meta;
                                if (item_damage != null) {
                                    if (item_damage.getDamage() != 0) {
                                        item_map.put("Damage",item_damage.getDamage());
                                    }
                                }

                                if (ItemData.has(item)) {

                                    PersistentDataContainer item_data = item_meta.getPersistentDataContainer();
                                    Map<String,Object> plugin_list = new LinkedHashMap<>();

                                    for (NamespacedKey key : ItemData.all(item)) {
                                        Map<String,Object> key_map = new LinkedHashMap<>();

                                        String plugin_name = key.toString().substring(0,key.toString().lastIndexOf(":"));
                                        String namespaced_key = key.toString().replace(plugin_name+":","");
                                        
                                        if (item_data.has(key,PersistentDataType.INTEGER)) {
                                            key_map.put(namespaced_key,item_data.get(key,PersistentDataType.INTEGER));
                                        }
                                        if (item_data.has(key,PersistentDataType.STRING)) {
                                            key_map.put(namespaced_key,item_data.get(key,PersistentDataType.STRING));
                                        }

                                        plugin_list.put(plugin_name,key_map);
                                    }

                                    item_map.put("NamespacedKey",plugin_list); 
                                }

                                list.put(String.valueOf(i),item_map);
                            }
                        }
                    }

                    inventory_map.put("Contents",list);
                    
                    block_data.put("Inventory",inventory_map);
                    data.put(location,block_data);
                    
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(BlockData.default_message());
                        yaml.dump(data,writer);
                    }
                } catch (IOException error) {
                    Shorts.logError(error.toString());
                }
            }
        } else {
            FileManager.create(path,archive);
        }
            
    }

    // Load a 'inventory' from 'File'
    public static void load() {
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
    
                // Map for 'Data'
                Map<String,Map<String,Map<String,Object>>> data = yaml.load(reader);
    
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
    
                        String location = location(block);
    
                        // Map for 'Block Data'
                        Map<String,Map<String,Object>> block_data = data.get(location);
                        
                        if (block_data != null) {
                            Map<String,Object> inventory_map = block_data.get("Inventory");
    
                            if (inventory_map != null) {
    
                                String title = (String) inventory_map.get("Title");
                                Integer size = (Integer) inventory_map.get("Size");
                                
                                Inventory inventory = Shorts.server().createInventory(null,size,title);
    
                                @SuppressWarnings("unchecked")
                                Map<String,Object> list = (Map<String,Object>) inventory_map.get("Contents");
    
                                for (String slot : list.keySet()) {
                                    
                                    // Map for each 'Item'
                                    @SuppressWarnings("unchecked")
                                    Map<String,Object> item_map = (Map<String,Object>) list.get(slot);

                                    String material = (String) item_map.get("Type");
                                    Integer amount = (Integer) item_map.get("Amount");
                                    ItemStack item = new ItemStack(Material.getMaterial(material),amount);
    
                                    if (item != null) {
                                        ItemMeta item_meta = item.getItemMeta();
                                        String display_name = (String) item_map.get("Name");

                                        if (display_name != null) {
                                            item_meta.setDisplayName((String) item_map.get("Name"));
                                        }
    
                                        @SuppressWarnings("unchecked")
                                        List<String> lore = (List<String>) item_map.get("Lore");
                                        
                                        if (lore != null) {
                                            item_meta.setLore((lore));
                                        }

                                        @SuppressWarnings("unchecked")
                                        Map<String,Object> enchantments_list = (Map<String,Object>) item_map.get("Enchantments List");
                                        if (enchantments_list != null) {
                                            for (String key : enchantments_list.keySet()) {
                                                @SuppressWarnings("unchecked")
                                                Map<String,Object> enchantment_data = (Map<String,Object>) enchantments_list.get(key);

                                                if (enchantment_data != null) {
                                                    String enchant_string = (String) enchantment_data.get("Enchantment Key");

                                                    NamespacedKey enchantment_key = (NamespacedKey) NamespacedKey.fromString(enchant_string);
                                                    Integer enchantment_level = (Integer) enchantment_data.get("Enchantment Level");

                                                    item_meta.addEnchant(Enchantment.getByKey(enchantment_key),enchantment_level,true);
                                                }

                                            }
                                        }

                                        @SuppressWarnings("unchecked")
                                        Multimap<Attribute,AttributeModifier> attributes = (Multimap<Attribute,AttributeModifier>) item_map.get("Attributes");
                                        if (attributes != null) {
                                            item_meta.setAttributeModifiers(attributes);
                                        }

                                        // Map for 'Visual Effects'
                                        @SuppressWarnings("unchecked")
                                        Map<String,Object> visual_effect = (Map<String,Object>) item_map.get("Visual Effects");
                                        if (visual_effect != null) {
                                            Integer customModelData = (Integer) visual_effect.get("Custom Model Data");
                                            if (customModelData != null && customModelData != 0) {
                                                item_meta.setCustomModelData(customModelData);
                                            }

                                            Boolean hide_enchants = (Boolean) visual_effect.get("Hide Enchants");
                                            if (hide_enchants != null && hide_enchants == true) {
                                                item_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                                item.setItemMeta(item_meta);
                                                item_meta = item.getItemMeta();
                                                ItemData.set(item,"hide_enchants","true");
                                            }
                                        }

                                        Integer damage = (Integer) item_map.get("Damage");
                                        if (damage != null) {
                                            Damageable item_damage = (Damageable) item_meta;
                                            item_damage.setDamage(damage);
                                        }
                                        
    
                                        item.setItemMeta(item_meta);
    
                                        if (item_map.get("NamespacedKey") != null) {
                                            @SuppressWarnings("unchecked")
                                            Map<String,Object> plugin_list = (Map<String,Object>) item_map.get("NamespacedKey");
                                            
                                            if (plugin_list != null) {
                                                for (String plugin_name : plugin_list.keySet()) {
                                                        
                                                    @SuppressWarnings("unchecked")
                                                    Map<String,Object> key_list = (Map<String,Object>) plugin_list.get(plugin_name);

                                                    if (key_list != null) {
                                                        for (String namespaced : key_list.keySet()) {
                                                            PersistentDataContainer key_data = item_meta.getPersistentDataContainer();
                                                            NamespacedKey key_key = new NamespacedKey(plugin_name,namespaced);

                                                            Object key_info = key_key.getKey().toString().replace(plugin_name+":","");
                                                            
                                                            if (key_info instanceof Integer) {
                                                                key_data.set(key_key,PersistentDataType.INTEGER,(Integer) key_info);
                                                            }
                                                            if (key_info instanceof String) {
                                                                key_data.set(key_key,PersistentDataType.STRING,(String) key_info);
                                                            }
                                                        }
                                                    }
                                                }   
                                            }
                                        }

                                        Integer item_slot = Integer.parseInt(slot);

                                        inventory.setItem(item_slot,item);
                                    }
                                }
    
                                inventory_data.put(location,inventory);
                                inventory_information.put("Title",title);
                                inventory_information.put("Size",size);
                            }
                        }
                    }
                    Shorts.logInfo("&aAll 'Block Inventory' was successfully loaded!");                         
                }
            } catch (IOException error) {
                Shorts.logError(error.toString());
            }
        } else {
            FileManager.create(path,archive);
        }
    }

    // Format 'Location' to a better 'String'
    public static String location(Block block) {

        String formatted = 
            block.getWorld().getName() + "," +
            block.getX() + "," +
            block.getY() + "," +
            block.getZ();

        return formatted;
    }
}
