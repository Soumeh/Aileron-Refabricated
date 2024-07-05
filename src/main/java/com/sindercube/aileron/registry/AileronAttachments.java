package com.sindercube.aileron.registry;

import com.mojang.serialization.Codec;
import com.sindercube.aileron.Aileron;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class AileronAttachments {

    public static void init() {}


    public static final AttachmentType<Integer> SMOKESTACKS = register("smokestacks",
            Codec.intRange(0, 1024)
    );


    public static <T> AttachmentType<T> register(String path, Codec<T> codec) {
        return AttachmentRegistry.createPersistent(Aileron.of(path), codec);
    }

}
