package com.sindercube.aileron.registry;

import com.sindercube.aileron.Aileron;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class AileronAttributes {

    public static void init() {}


    public static final RegistryEntry<EntityAttribute> MAX_SMOKESTACKS = registerReference("max_smokestacks",
            new ClampedEntityAttribute("attribute.aileron.max_smokestacks", 0, 0, 1024)
                    .setTracked(true)
    );

    public static final RegistryEntry<EntityAttribute> ALTITUDE_DRAG_REDUCTION = registerReference("altitude_drag_reduction",
            new ClampedEntityAttribute("attribute.aileron.altitude_drag_reduction", 0, 0, 1024)
                    .setTracked(true)
    );


    public static RegistryEntry<EntityAttribute> registerReference(String path, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Aileron.of(path), attribute);
    }

}
