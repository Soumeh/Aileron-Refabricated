package com.sindercube.aileron.registry;

import com.sindercube.aileron.Aileron;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class AileronAttributes {

    public static void init() {}


    public static final RegistryEntry<EntityAttribute> SMOKESTACK_CHARGES = registerReference("smokestack_charges",
            new ClampedEntityAttribute("attribute.aileron.smokestack_charges", 0, 0, 1024)
                    .setTracked(true)
    );

    public static final RegistryEntry<EntityAttribute> CLOUD_DRAG_REDUCTION = registerReference("cloud_drag_reduction",
            new ClampedEntityAttribute("attribute.aileron.cloud_drag_reduction", 0, 0, 1024)
                    .setTracked(true)
    );


    public static RegistryEntry<EntityAttribute> registerReference(String path, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Aileron.of(path), attribute);
    }

}
