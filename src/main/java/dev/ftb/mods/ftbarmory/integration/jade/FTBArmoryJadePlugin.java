package dev.ftb.mods.ftbarmory.integration.jade;

import dev.ftb.mods.ftbarmory.FTBArmory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.JadeIds;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.harvest.ToolTier;

@WailaPlugin
public class FTBArmoryJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addHarvestPlugin(registry -> registry.insertTierAfter(
                JadeIds.JADE("pickaxe"),
                BuiltInRegistries.ITEM.getKey(Items.NETHERITE_PICKAXE),
                ToolTier.item(BuiltInRegistries.ITEM.getValue(FTBArmory.id("adamantite_pickaxe")))));
    }
}
