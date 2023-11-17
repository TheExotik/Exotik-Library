package exotik.exotiklibrary.Logics;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import exotik.exotiklibrary.Mechanics.ItemData;
import exotik.exotiklibrary.Mechanics.ItemDetails;
import exotik.exotiklibrary.Mechanics.Shorts;

public class ItemCreation{
    // Create a new 'ItemStack' with a custom 'Display Name' and 'Item ID'
    public static ItemStack create(Material material, String name, String id) {
        ItemStack item = new ItemStack(material);
        ItemDetails.setName(item, name);
        ItemData.set(item,"Item_ID",id);
        
        return item;
    }

    // Create a new 'ItemStack' with a custom 'Display Name', 'Lore' and 'Item ID'
    public static ItemStack create(Material material, String name, List<String> lore, String id) {
        ItemStack item = new ItemStack(material);
        ItemDetails.setName(item, name);
        ItemDetails.setLore(item, lore);
        ItemData.set(item,"item_id",id);

        return item;
    }
    // Create a new 'ItemStack' with a custom 'Display Name', 'Lore', 'Item ID' and 'Size' for inventory
    public static ItemStack create(Material material, String name, List<String> lore, String id, Integer size) {
        ItemStack item = new ItemStack(material);
        ItemDetails.setName(item, name);
        ItemDetails.setLore(item, lore);
        ItemData.set(item,"item_id",id);
        ItemData.set(item,"inventory_size",size);

        return item;
    }

    // Create a 'Shaped' recipe to 'Item'
    public static ShapedRecipe shaped(ItemStack item, String recipeID, String[] shape, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(Shorts.plugin, recipeID);

        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape(shape);

        for (int i = 0; i < ingredients.length; i += 2) {
            char symbol = (char) ingredients[i];
            Material input = (Material) ingredients[i +1];
            recipe.setIngredient(symbol, input);
        }
        
        Bukkit.addRecipe(recipe);
        return recipe;
    }
}
