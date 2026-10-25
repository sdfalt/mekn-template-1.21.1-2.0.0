package com.atom.mekn.common.mekn.chemicals.chemical;
import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.chemicals.ChemicalConstants;
import mekanism.api.chemical.Chemical;
import mekanism.common.registration.impl.ChemicalDeferredRegister;
import mekanism.common.registration.impl.DeferredChemical;
import net.neoforged.bus.api.IEventBus;


public class ModChemicals {

    public static final ChemicalDeferredRegister CHEMICALS = new ChemicalDeferredRegister(MekN.MODID);

    public static final DeferredChemical<Chemical> NITROGEN = CHEMICALS.register(ChemicalConstants.NITROGEN);
    public static final DeferredChemical<Chemical> MACHINE_OIL_VAPOR = CHEMICALS.register(ChemicalConstants.MACHINE_OIL);
    public static final DeferredChemical<Chemical> DIRTY_ZIRCON_SLURRY = CHEMICALS.register(ChemicalConstants.DIRTY_ZIRCON_SLURRY);
    public static final DeferredChemical<Chemical> CLEAN_ZIRCON_SLURRY = CHEMICALS.register(ChemicalConstants.CLEAN_ZIRCON_SLURRY);
    public static final DeferredChemical<Chemical> DIRTY_BERYLLIUM_SLURRY = CHEMICALS.register("dirty_beryllium_slurry", 0xFF8B9C6E);
    public static final DeferredChemical<Chemical> CLEAN_BERYLLIUM_SLURRY = CHEMICALS.register("clean_beryllium_slurry", 0xFF9EB87C);
    public static final DeferredChemical<Chemical> LOW_ENRICHED_URANIUM = CHEMICALS.register(ChemicalConstants.LOW_ENRICHED_URANIUM);
    public static final DeferredChemical<Chemical> HIGHLY_ENRICHED_URANIUM = CHEMICALS.register(ChemicalConstants.HIGHLY_ENRICHED_URANIUM);
    public static final DeferredChemical<Chemical> URANIUM_OXYFLUORIDE = CHEMICALS.register(ChemicalConstants.URANIUM_OXYFLUORIDE);
    public static final DeferredChemical<Chemical> HYDROFLUORIC_ACID = CHEMICALS.register(ChemicalConstants.HYDROFLUORIC_ACID);
    public static final DeferredChemical<Chemical> DIRTY_MAGNESIUM_SLURRY = CHEMICALS.register("dirty_magnesium_slurry", 0xFFA0A0A0);
    public static final DeferredChemical<Chemical> CLEAN_MAGNESIUM_SLURRY = CHEMICALS.register("clean_magnesium_slurry", 0xFFC0C0C0);
    public static final DeferredChemical<Chemical> ENRICHED_MAGNESIUM = CHEMICALS.register("enriched_magnesium", 0xFFB0B0B0);
    public static final DeferredChemical<Chemical> NITRIC_ACID = CHEMICALS.register(ChemicalConstants.NITRIC_ACID);
    public static final DeferredChemical<Chemical> AMMONIA = CHEMICALS.register(ChemicalConstants.AMMONIA);
    public static final DeferredChemical<Chemical> NITRIC_OXIDE = CHEMICALS.register(ChemicalConstants.NITRIC_OXIDE);
    public static final DeferredChemical<Chemical> NITROGEN_DIOXIDE = CHEMICALS.register(ChemicalConstants.NITROGEN_DIOXIDE);


    public static void register(IEventBus bus){CHEMICALS.register(bus);}


}
