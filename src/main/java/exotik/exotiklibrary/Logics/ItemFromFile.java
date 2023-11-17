package exotik.exotiklibrary.Logics;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.Yaml;

import exotik.exotiklibrary.Mechanics.ItemData;
import exotik.exotiklibrary.Mechanics.Shorts;

public class ItemFromFile {

    private static String path = "Items And Blocks";
    private static String archive = "Items";
    private static File file = FileManager.file(path,archive);
    private static Yaml yaml = FileManager.yaml();

    // Create the 'File' with 'Default' Items
    public static void create() {
        if (!file.exists()) {
            FileManager.create(path,archive);
            try (FileReader reader = new FileReader(file)) {

                // Map for 'Data'
                Map<String,Map<String,Object>> data = yaml.load(reader);
                if (data == null) {
                    data = new LinkedHashMap<>();
                }

                // Map for 'Super Sword' item
                Map<String,Object> super_sword = new LinkedHashMap<>();

                // Map for 'Super Sword' recipe
                Map<String,Object> super_sword_recipe = new LinkedHashMap<>();

                super_sword.put("Name","&6Super Sword");
                super_sword.put("Type","NETHERITE_SWORD");
                super_sword.put("Lore",List.of(
                    "&7This Sword can kill many mobs",
                    "&7You just have to hit them &a:)"
                    ));
                super_sword.put("Item ID","super_sword");

                super_sword_recipe.put("Permission","exotiklibrary.craft.super_sword");
                super_sword_recipe.put("Type","CRAFT");
                super_sword_recipe.put("Slot 1","AIR"); super_sword_recipe.put("Slot 2","NETHERITE_INGOT"); super_sword_recipe.put("Slot 3","AIR");
                super_sword_recipe.put("Slot 4","AIR"); super_sword_recipe.put("Slot 5","NETHERITE_BLOCK"); super_sword_recipe.put("Slot 6","AIR");
                super_sword_recipe.put("Slot 7","AIR"); super_sword_recipe.put("Slot 8","NETHER_STAR");     super_sword_recipe.put("Slot 9","AIR");

                super_sword.put("Recipe",super_sword_recipe);

                // Map for 'Super Sword' enchantments
                Map<String,Object> super_sword_enchantments = new LinkedHashMap<>();
                Map<String,Object> super_sword_enchantments_data = new LinkedHashMap<>();

                // Sharpness
                super_sword_enchantments_data.put("Enchantment Key","sharpness");
                super_sword_enchantments_data.put("Enchantment Level",20);

                super_sword_enchantments.put("sharpness",super_sword_enchantments_data);

                super_sword.put("Enchantments List",super_sword_enchantments);

                super_sword.put("Enabled",true);

                data.put("Default Super Sword",super_sword);

                // Map for 'Super Ingot' item
                Map<String,Object> super_ingot = new LinkedHashMap<>();

                // Map for 'Super Ingot' recipe
                Map<String,Object> super_ingot_recipe = new LinkedHashMap<>();

                super_ingot.put("Name","&5Super Ingot");
                super_ingot.put("Type","IRON_INGOT");
                super_ingot.put("Lore",List.of(
                    "&7A mystic ingot that can be used",
                    "&7to create amazing stuffs...",
                    "&7Only a real &cGod &7can hold it."
                ));
                super_ingot.put("Item ID","super_ingot");
                
                super_ingot_recipe.put("Permission","exotiklibrary.smelt.super_ingot");
                super_ingot_recipe.put("Type","SMELT");
                super_ingot_recipe.put("Input","NETHER_STAR");
                super_ingot_recipe.put("Delay",5);
                super_ingot_recipe.put("EXP Gain",30);

                super_ingot.put("Recipe",super_ingot_recipe);

                // Map for 'Super Ingot' visual effect
                Map<String,Object> super_ingot_visual = new LinkedHashMap<>();
                super_ingot_visual.put("Custom Model Data", 1);
                super_ingot_visual.put("Hide Enchants", true);

                super_ingot.put("Visual Effects",super_ingot_visual);
                super_ingot.put("Enabled",true);
                
                data.put("Default Super Ingot",super_ingot);

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(default_message());
                    yaml.dump(data,writer);
                }

            } catch (IOException error) {
                Shorts.logError(error.toString());
            }
        }
    }

    // Load all 'Items' created in 'File'
    public static void load() {
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {

                // Map for 'Data'
                Map<String,Map<String,Object>> data = yaml.load(reader);
                if (data != null) {

                    for (String key : data.keySet()) {
                        // Map for 'Item'
                        Map<String,Object> item_data = data.get(key);

                        Boolean enabled = (Boolean) item_data.get("Enabled");

                        if (enabled == true) {
                            String name = (String) item_data.get("Name");
                            String type = (String) item_data.get("Type");
                            ItemStack item = new ItemStack(Material.getMaterial(type));
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(Shorts.color(name));
                            
                            @SuppressWarnings("unchecked")
                            List<String> lore = (List<String>) item_data.get("Lore");
                            if (lore != null) {
                                meta.setLore(Shorts.color(lore));
                            }

                            // Map for 'Enchantments'
                            @SuppressWarnings("unchecked")
                            Map<String,Object> enchantments_map = (Map<String,Object>) item_data.get("Enchantments List");

                            if (enchantments_map != null) {
                                for (String enchantment_key : enchantments_map.keySet()) {
                                    @SuppressWarnings("unchecked")
                                    Map<String,Object> enchantments_data = (Map<String,Object>) enchantments_map.get(enchantment_key);

                                    if (enchantments_data != null) {
                                        String enchantment_name = (String) enchantments_data.get("Enchantment Key");
                                        Integer enchantment_level = (Integer) enchantments_data.get("Enchantment Level");
    
                                        NamespacedKey enchantment_namespaced = (NamespacedKey.fromString(enchantment_name));
                                        meta.addEnchant(Enchantment.getByKey(enchantment_namespaced),enchantment_level,true);
                                    }
                                }
                            }

                            // Map for 'Visual Effects'
                            @SuppressWarnings("unchecked")
                            Map<String,Object> visual_effect_map = (Map<String,Object>) item_data.get("Visual Effects");

                            if (visual_effect_map != null) {
                                Integer customModelData = (Integer) visual_effect_map.get("Custom Model Data");
                                Boolean hideEnchants = (Boolean) visual_effect_map.get("Hide Enchants");

                                if (customModelData != 0) {
                                    meta.setCustomModelData(customModelData);
                                }
                                if (hideEnchants == true) {
                                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    item.setItemMeta(meta);

                                    ItemData.set(item,"hide_enchants","true");

                                    meta = item.getItemMeta();
                                }
                                
                            }

                            item.setItemMeta(meta);

                            String id = (String) item_data.get("Item ID");
                            ItemData.set(item,"item_id",id);
                            
                            @SuppressWarnings("unchecked")
                            Map<String,Object> recipe = (Map<String,Object>) item_data.get("Recipe");

                            String recipe_type = (String) recipe.get("Type");

                            if (recipe_type.equalsIgnoreCase("Craft")) {
                                String[] ingredients = {
                                    (String) recipe.get("Slot 1"),
                                    (String) recipe.get("Slot 2"),
                                    (String) recipe.get("Slot 3"),
                                    (String) recipe.get("Slot 4"),
                                    (String) recipe.get("Slot 5"),
                                    (String) recipe.get("Slot 6"),
                                    (String) recipe.get("Slot 7"),
                                    (String) recipe.get("Slot 8"),
                                    (String) recipe.get("Slot 9")
                                };

                                String[] shape = {
                                    "ABC",
                                    "DEF",
                                    "GHI"
                                };

                                String[] chars = {"A","B","C","D","E","F","G","H","I"};
                                NamespacedKey recipe_key = new NamespacedKey(Shorts.plugin,"recipe_" + id);
                                ShapedRecipe recipe_shaped = new ShapedRecipe(recipe_key,item);

                                recipe_shaped.shape(shape);
                                
                                for (int i = 0; i < ingredients.length; i += 1) {
                                    char symbol = (char) chars[i].charAt(0);
                                    Material material = Material.getMaterial(ingredients[i]);
                                    recipe_shaped.setIngredient(symbol,material);
                                }

                                Bukkit.addRecipe(recipe_shaped);
                            }
                        
                            if (recipe_type.equalsIgnoreCase("Smelt")) {
                                Material ingredient = Material.getMaterial((String)recipe.get("Input"));
                                Integer delay = (Integer) recipe.get("Delay");
                                Integer exp = (Integer) recipe.get("EXP Gain");
                                NamespacedKey recipe_key = new NamespacedKey(Shorts.plugin,"recipe_" + id);

                                Bukkit.addRecipe(new FurnaceRecipe(recipe_key,item,ingredient,exp,delay));
                            }
                        
                        }
                    }
                }

            } catch (IOException error) {
                Shorts.logError(error.toString());
            }
        } else {
            create();
        }
    }


    // Default 'Message' to this 'File'
    public static String default_message() {
        StringBuilder text = new StringBuilder();
        text.append(FileManager.default_message());
        text.append("#\n");
        text.append("#   Here you can follow this 'Default' item\n");
        text.append("#   To create any kind of item you want.\n");
        text.append("#   Must follow theses examples!\n");
        text.append("#\n");
        text.append("#   For now, you can only create 'Simple' items\n");
        text.append("#   Custom Items with mechanics will be\n");
        text.append("#   Implemented soon!\n");
        text.append("#\n");
        text.append(Shorts.break_line + "\n");

        return text.toString();
    }
    
}
