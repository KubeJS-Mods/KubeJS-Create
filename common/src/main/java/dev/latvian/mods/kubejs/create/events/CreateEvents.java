package dev.latvian.mods.kubejs.create.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface CreateEvents {
    EventGroup GROUP = EventGroup.of("CreateEvents");

    EventHandler SPECIAL_FLUID = CreateEvents.GROUP.startup(SpecialFluidHandlerEvent.ID, () -> SpecialFluidHandlerEvent.class);
    EventHandler SPECIAL_SPOUT = CreateEvents.GROUP.startup(SpecialSpoutHandlerEvent.ID, () -> SpecialSpoutHandlerEvent.class);
    EventHandler BOILER_HEATER = CreateEvents.GROUP.startup(BoilerHeaterHandlerEvent.ID, () -> BoilerHeaterHandlerEvent.class);
}
