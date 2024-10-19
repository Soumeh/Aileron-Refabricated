package com.sindercube.eleron.registry;

import com.sindercube.eleron.Eleron;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class EleronAttributes {

    public static void init() {}


    public static final RegistryEntry<EntityAttribute> MAX_SMOKESTACKS = registerReference("max_smokestacks",
            new ClampedEntityAttribute("attribute.eleron.max_smokestacks", 0, 0, 1024)
                    .setTracked(true)
    );

    public static final RegistryEntry<EntityAttribute> ALTITUDE_DRAG_REDUCTION = registerReference("altitude_drag_reduction",
            new ClampedEntityAttribute("attribute.eleron.altitude_drag_reduction", 0, 0, 1024)
                    .setTracked(true)
    );


    public static RegistryEntry<EntityAttribute> registerReference(String path, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Eleron.of(path), attribute);
    }

}
