package com.anningui.modifyjs.kubejs.event;

import dev.latvian.mods.kubejs.bindings.event.ClientEvents;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface MJSModelEvents {
    EventGroup GROUP = EventGroup.of("MJSModelEvents");
    EventHandler REGISTER_ADDER = GROUP.startup("registerAdder", () -> ModelRegisterAdditional.class);
}
