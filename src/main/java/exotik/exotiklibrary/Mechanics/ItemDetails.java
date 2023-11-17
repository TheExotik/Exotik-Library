package exotik.exotiklibrary.Mechanics;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemDetails {
    
    // Set the 'Display Name' of the Item
    public static void setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name != null ? Shorts.color(name) : "Default Name");
        item.setItemMeta(meta);
    }

    // Replace the word 'X' with the word 'Y' of the 'Display Name' of the item
    public static void replaceName(ItemStack item, String before, String after) {
        ItemMeta meta = item.getItemMeta();
        String current = meta.getDisplayName();

        meta.setDisplayName(Shorts.color(current.replace(before,after)));
        item.setItemMeta(meta);
    }

    // Set the 'Lore' of the item (as 'String')
    public static void setLore(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore != null ? List.of(Shorts.color(lore)) : List.of("Default Lore"));
        item.setItemMeta(meta);
    }

    // Set the 'Lore' of the item (as 'String[]')
    public static void setLore(ItemStack item, String[] lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore != null ? List.of(Shorts.color(lore)) : List.of("Default Lore"));
        item.setItemMeta(meta);
    }

    // Set the 'Lore' of the item (as 'List')
    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore != null ? Shorts.color(lore) : List.of("Default Lore"));
        item.setItemMeta(meta);
    }

    // Replace a word 'X' to word 'Y' in the 'Lore' of the item
    public static void replaceLore(ItemStack item, String before, String after) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);

                if (line.contains(before)) {
                    line = line.replace(before, after);
                    lore.set(i, line);
                }
            }

            meta.setLore(Shorts.color(lore));
            item.setItemMeta(meta);
        }
    }

    // Consume a 'X' number of items from the current 'ItemStack'
    public static void consume(ItemStack item, Integer amount) {
        int current = item.getAmount();
        int after = current - amount;

        if (after < 0) {
            return;
        } else {
            item.setAmount(after);
        }
    }

}
