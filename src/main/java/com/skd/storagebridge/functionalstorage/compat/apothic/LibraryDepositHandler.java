package com.skd.storagebridge.functionalstorage.compat.apothic;

import com.buuz135.functionalstorage.block.tile.StorageControllerTile;
import com.skd.storagebridge.functionalstorage.compat.ControllerNetworkSearch;

import dev.shadowsoffire.apothic_enchanting.library.EnchLibraryTile;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Triggered by right-clicking a Functional Storage Controller (see
 * {@link ControllerNetworkSearch#controllerFor}). Moves every
 * {@code minecraft:enchanted_book} stack in the player's inventory into an
 * Apothic-Enchanting Library ({@code apothic_enchanting:library} /
 * {@code apothic_enchanting:ender_library}) reachable through the Controller's
 * linked drawers.
 *
 * <p>Runs before Functional Storage's own insertion, so books go to the
 * Library first. One-directional: the Library consumes every book it accepts
 * and never gives anything back.</p>
 */
public final class LibraryDepositHandler {

    private LibraryDepositHandler() {
    }

    public static void onRightClickController(PlayerInteractEvent.RightClickBlock event) {
        StorageControllerTile<?> controller = ControllerNetworkSearch.controllerFor(event);
        if (controller == null) {
            return;
        }

        EnchLibraryTile library = ControllerNetworkSearch.findNetworked(event.getLevel(), controller, EnchLibraryTile.class);
        if (library == null) {
            return;
        }

        IItemHandler libraryHandler = library.getItemHandler(null);
        if (libraryHandler == null || libraryHandler.getSlots() < 1) {
            return;
        }

        Inventory inventory = event.getEntity().getInventory();
        for (int slot = 0; slot < Inventory.INVENTORY_SIZE; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty() || stack.getItem() != Items.ENCHANTED_BOOK) {
                continue;
            }
            while (!stack.isEmpty()) {
                ItemStack remainder = libraryHandler.insertItem(0, stack.copyWithCount(1), false);
                if (!remainder.isEmpty()) {
                    break; // Library rejected/full for this enchantment set, stop draining this stack
                }
                stack.shrink(1);
            }
        }
    }
}
