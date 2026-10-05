package com.skd.storagebridge.functionalstorage.compat;

import java.util.LinkedHashSet;
import java.util.Set;

import com.buuz135.functionalstorage.block.tile.StorageControllerExtensionTile;
import com.buuz135.functionalstorage.block.tile.StorageControllerTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Shared helpers for every {@code compat.*} integration in this mod
 * (Apothic-Enchanting Library, Sophisticated Storage chests/barrels,
 * Apotheosis Gem Case).
 *
 * <p>The trigger is a right-click on a Functional Storage Controller (plain or
 * framed) or on a Controller Extension linked to one, with the main hand and
 * not sneaking (sneaking opens the Controller's GUI), on any face. Functional
 * Storage's own insertion still runs afterwards; this mod only adds the targets
 * it cannot reach.</p>
 *
 * <p>Functional Storage has no physical network: drawers and extensions are
 * linked to the Controller with the Linking Tool, within its linking range. A
 * target is "reachable" when it touches the Controller itself or any block
 * linked to it ({@code getConnectedDrawers().getConnectedDrawers()}).</p>
 */
public final class ControllerNetworkSearch {

    private ControllerNetworkSearch() {
    }

    /**
     * Returns the Functional Storage Controller targeted by this click if it is
     * a valid deposit gesture, or {@code null} otherwise (client side, other
     * block, off-hand, sneaking, or an extension that isn't linked).
     */
    public static StorageControllerTile<?> controllerFor(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) {
            return null;
        }
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return null;
        }
        if (event.getEntity().isShiftKeyDown()) {
            return null; // sneak-clicks open Functional Storage's Controller GUI
        }

        BlockEntity clicked = level.getBlockEntity(event.getPos());
        if (clicked instanceof StorageControllerTile<?> controller) {
            return controller;
        }
        if (clicked instanceof StorageControllerExtensionTile<?> extension) {
            // Extensions forward their clicks to the linked Controller, so do we.
            BlockPos controllerPos = extension.getControllerPos();
            if (controllerPos != null && level.isLoaded(controllerPos)
                    && level.getBlockEntity(controllerPos) instanceof StorageControllerTile<?> controller) {
                return controller;
            }
        }
        return null; // not a Controller at all, stay silent to avoid acting on unrelated clicks
    }

    public static <T extends BlockEntity> T findNetworked(Level level, StorageControllerTile<?> controller, Class<T> targetType) {
        Set<BlockPos> members = new LinkedHashSet<>();
        members.add(controller.getBlockPos());
        for (Long linked : controller.getConnectedDrawers().getConnectedDrawers()) {
            members.add(BlockPos.of(linked));
        }

        for (BlockPos member : members) {
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = member.relative(direction);
                if (!level.isLoaded(neighbor)) {
                    continue;
                }
                BlockEntity blockEntity = level.getBlockEntity(neighbor);
                if (targetType.isInstance(blockEntity)) {
                    return targetType.cast(blockEntity);
                }
            }
        }
        return null;
    }
}
