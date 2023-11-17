package exotik.exotiklibrary.Items.Blocks;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import exotik.exotiklibrary.Logics.ItemCreation;

public class IronChest {
    
    // 'ItemStack' of 'Iron Chest'
    public static ItemStack chest = ItemCreation.create(
        Material.IRON_BLOCK,
        "&6Iron Chest",
        List.of(
            "&7Esse bloco de ferro armazena",
            "&7itens dentro dele.",
            "&7Espaço: &e54 &7slots"
        ),
        "iron_chest",
        54
    );

    // Method to 'Register' the 'Iron Chest' and 'Recipe' 
    public IronChest() {
        String[] shape = {
            "III",
            "C C",
            "III"
        };

        char i = 'I';
        char c = 'C';
        ItemCreation.shaped(
            chest, 
            "iron_chest_recipe", 
            shape, 
            i, Material.IRON_INGOT, 
            c, Material.CHEST
        );
    }


}
