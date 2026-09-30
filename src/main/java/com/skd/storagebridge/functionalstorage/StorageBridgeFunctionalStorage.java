package com.skd.storagebridge.functionalstorage;

import com.skd.storagebridge.functionalstorage.compat.apotheosis.GemCaseDepositHandler;
import com.skd.storagebridge.functionalstorage.compat.apothic.LibraryDepositHandler;
import com.skd.storagebridge.functionalstorage.compat.sophisticatedstorage.SophisticatedStorageDepositHandler;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(StorageBridgeFunctionalStorage.MOD_ID)
public class StorageBridgeFunctionalStorage {

    public static final String MOD_ID = "storage_bridge_functional_storage";

    public StorageBridgeFunctionalStorage(IEventBus modEventBus) {
        // Gameplay listeners live on the global game bus, not the mod lifecycle bus.
        NeoForge.EVENT_BUS.addListener(LibraryDepositHandler::onRightClickController);
        NeoForge.EVENT_BUS.addListener(SophisticatedStorageDepositHandler::onRightClickController);
        NeoForge.EVENT_BUS.addListener(GemCaseDepositHandler::onRightClickController);
    }
}
