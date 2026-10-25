package com.atom.mekn.common.mekn.register.sounds;

import com.atom.mekn.MekN;
import mekanism.common.registration.impl.SoundEventDeferredRegister;
import mekanism.common.registration.impl.SoundEventRegistryObject;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;

public class SoundRegister {
    public static final SoundEventDeferredRegister SOUND_EVENTS = new SoundEventDeferredRegister(MekN.MODID);
    public static final SoundEventRegistryObject<SoundEvent> MACHINE_WORK = SOUND_EVENTS.register("machine_work");
    public static final SoundEventRegistryObject<SoundEvent> LIXINJI = SOUND_EVENTS.register("centrifuge");
    public static final SoundEventRegistryObject<SoundEvent> JINGHUA = SOUND_EVENTS.register("air_purifier");
    public static final SoundEventRegistryObject<SoundEvent> KEHUAN = SOUND_EVENTS.register("scifi_machine");
    public static void register(IEventBus bus){ SOUND_EVENTS.register(bus);}
}
