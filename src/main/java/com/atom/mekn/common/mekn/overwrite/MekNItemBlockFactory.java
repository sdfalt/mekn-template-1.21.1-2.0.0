package com.atom.mekn.common.mekn.overwrite;

import mekanism.api.text.EnumColor;
import mekanism.common.MekanismLang;
import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tier.FactoryTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import java.util.List;


public class MekNItemBlockFactory extends ItemBlockTooltip<BlockTile<?, ?>> {
    private static AttachedSideConfig getSideConfig(MekNBlockFactoryMachine.MekNBlockFactory<?> block) {
        return switch (Attribute.getOrThrow(block.builtInRegistryHolder(), MekNAttributeFactoryType.class).getFactoryType()) {
            case NITROGEN_EXTRACTION -> AttachedSideConfig.ADVANCED_MACHINE;
        };
    }
    public MekNItemBlockFactory(MekNBlockFactoryMachine.MekNBlockFactory<?> block, Properties properties) {
        super(block, true, properties
                .component(MekanismDataComponents.SORTING, false)
                .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                .component(MekanismDataComponents.SIDE_CONFIG, getSideConfig(block))
        );
    }

    @Override
    public FactoryTier getTier() {
        return Attribute.getTier(getBlock(), FactoryTier.class);
    }
    @Override
    protected void addTypeDetails(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        MekNAttributeFactoryType factoryType = Attribute.get(getBlock(), MekNAttributeFactoryType.class);
        if (factoryType != null) {
            tooltip.add(MekanismLang.FACTORY_TYPE.translateColored(EnumColor.INDIGO, EnumColor.GRAY, factoryType.getFactoryType()));
        }
        super.addTypeDetails(stack, context, tooltip, flag);
    }

}
