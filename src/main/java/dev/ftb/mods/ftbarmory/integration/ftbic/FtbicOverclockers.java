package dev.ftb.mods.ftbarmory.integration.ftbic;

import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import dev.ftb.mods.ftbic.FTBICConfig;
import dev.ftb.mods.ftbic.block.entity.machine.BasicMachineBlockEntity;
import dev.ftb.mods.ftbic.block.entity.machine.UpgradeInventory;
import dev.ftb.mods.ftbic.item.FTBICItems;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public final class FtbicOverclockers {
    public static final int FAMILY_LIMIT = 4;

    private FtbicOverclockers() {}

    public static void init() {
        register(Metal.ADAMANTITE, 1.6, 1.6);
        register(Metal.AETERNIUM, 1.8, 1.6);
        register(Metal.AURICHALCUM, 2.0, 1.5);
    }

    private static void register(Metal metal, double speed, double energy) {
        Compat.add(ModItems.ITEMS.registerItem(
                metal.id() + "_overclocker_upgrade",
                props -> new MetalOverclockerItem(props, speed, energy),
                props -> props.fireResistant()));
    }

    public static void apply(BasicMachineBlockEntity machine) {
        UpgradeInventory upgrades = machine.upgradeInventory;
        List<MetalOverclockerItem> installed = new ArrayList<>();
        for (int slot = 0; slot < upgrades.getSlots(); slot++) {
            ItemStack stack = upgrades.getStackInSlot(slot);
            if (stack.getItem() instanceof MetalOverclockerItem overclocker) {
                for (int i = 0; i < stack.getCount(); i++) {
                    installed.add(overclocker);
                }
            }
        }
        if (installed.isEmpty()) {
            return;
        }
        installed.sort(Comparator.comparingDouble((MetalOverclockerItem item) -> item.speed)
                .reversed());
        int metalUsed = Math.min(FAMILY_LIMIT, installed.size());
        int basic = upgrades.countUpgrades(FTBICItems.OVERCLOCKER_UPGRADE.get());
        int basicDropped = basic - Math.min(basic, FAMILY_LIMIT - metalUsed);
        if (basicDropped > 0) {
            machine.progressSpeed /= Math.pow(FTBICConfig.MACHINES.OVERCLOCKER_SPEED.get(), basicDropped);
            machine.energyUse /= Math.pow(FTBICConfig.MACHINES.OVERCLOCKER_ENERGY_USE.get(), basicDropped);
        }
        for (int i = 0; i < metalUsed; i++) {
            MetalOverclockerItem overclocker = installed.get(i);
            machine.progressSpeed *= overclocker.speed;
            machine.energyUse *= overclocker.energy;
        }
    }
}
