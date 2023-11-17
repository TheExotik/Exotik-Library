package exotik.exotiklibrary.Events;

import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import exotik.exotiklibrary.ExotikLibrary;
import exotik.exotiklibrary.Logics.BlockData;
import exotik.exotiklibrary.Logics.BlockInventory;
import exotik.exotiklibrary.Mechanics.ItemData;

public class BlockEvents implements Listener {

    // Basically will add 'Information' inside 'Block'
    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = player.getEquipment().getItemInMainHand();
        
        if (ItemData.has(item,"item_id")) {
            BlockData.add(block,"block_id",ItemData.asString(item,"item_id"));
        }
        if (ItemData.has(item,"inventory_size")) {
            BlockInventory.create(block,ItemData.asInteger(item,"inventory_size"),item.getItemMeta().getDisplayName());
        }

    }

    // Open 'Block Inventories' 
    @EventHandler
    public static void onClickedBlock(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Action action = event.getAction();
        Player player = event.getPlayer();

        if (!player.isSneaking()) {
            if (block != null && action == Action.RIGHT_CLICK_BLOCK) {
                if (BlockData.has(block)) {
                    event.setCancelled(true);
                    BlockInventory.open(block,player);
                }
            }
        }
    }
    
    // Drop 'Block Inventory' contents
    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (BlockData.has(block)) {
            BlockInventory.drop(block,block.getLocation());
            BlockData.clear(block);
        }
    }

    // Cancel the function of piston if block has 'Block Data'
    @EventHandler
    public static void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (BlockData.has(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
    // Cancel the function of piston if block has 'Block Data'
    @EventHandler
    public static void onPistonRetract(BlockPistonRetractEvent event) {
        
        for (Block block : event.getBlocks()) {
            if (BlockData.has(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    // Cancel the explosion if block has 'Block Data'
    @EventHandler
    public static void onBlockExplosion(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        Boolean can = false;

        if (entity instanceof TNTPrimed) {
            can = ExotikLibrary.config.getBoolean("Blocks.CanBeExploded.byTNT");
        } else if (entity instanceof Creeper) {
            can = ExotikLibrary.config.getBoolean("Blocks.CanBeExploded.byCreeper");
        } else if (entity instanceof Ghast || entity instanceof Fireball) {
            can = ExotikLibrary.config.getBoolean("Blocks.CanBeExploded.byFireball");
        }

        // If is 'False' in 'Config.yml' file -> Cancel the explosion for 'Block'
        // Else -> Keep explosion and clear the 'Block Data' for 'Block'
        if (can == false) {
            event.blockList().removeIf(each -> BlockData.has(each));
        } else {
            event.blockList().forEach(BlockData::clear);
        }
    }
    
}
