package com.skd.storagebridge.functionalstorage.compat.apotheosis;

import com.buuz135.functionalstorage.block.tile.StorageControllerTile;
import com.skd.storagebridge.functionalstorage.compat.ControllerNetworkSearch;

import dev.shadowsoffire.apotheosis.socket.gem.storage.GemCaseTile;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

/**
 * Triggered by the same right-click as {@code LibraryDepositHandler} (see
 * {@link ControllerNetworkSearch#controllerFor}). Moves unsocketed gem stacks
 * from the player's inventory into an Apotheosis Gem Case or Ender Gem Case
 * ({@link GemCaseTile} covers both) reachable through the Controller's storage
 * network.
 *
 * <p>No item-type filtering is done here: the Gem Case's own
 * {@code IItemHandler} already rejects anything that isn't a valid unsocketed
 * gem (returns the stack unchanged), so this handler simply tries to insert
 * every non-empty, non-book stack and keeps whatever the Gem Case actually
 * accepted. One-directional: gems deposited this way are not extracted back
 * out by this mod.</p>
 */
public final class GemCaseDepositHandler {

    private GemCaseDepositHandler() {
    }

    public static void onRightClickController(PlayerInteractEvent.RightClickBlock event) {
        StorageControllerTile<?> controller = ControllerNetworkSearch.controllerFor(event);
        if (controller == null) {
            return;
        }

        GemCaseTile gemCase = ControllerNetworkSearch.findNetworked(event.getLevel(), controller, GemCaseTile.class);
        if (gemCase == null) {
            return;
        }

        IItemHandler gemCaseHandler = gemCase.getItemHandler(null);
        if (gemCaseHandler == null) {
            return;
        }

        Inventory inventory = event.getEntity().getInventory();
        for (int slot = 0; slot < Inventory.INVENTORY_SIZE; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty() || stack.getItem() == Items.ENCHANTED_BOOK) {
                continue; // books are handled by the Apothic-Enchanting Library integration
            }

            int before = stack.getCount();
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(gemCaseHandler, stack.copy(), false);
            int inserted = before - remainder.getCount();
            if (inserted > 0) {
                stack.shrink(inserted);
            }
        }
    }
}
