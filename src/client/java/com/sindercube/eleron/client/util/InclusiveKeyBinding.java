package com.sindercube.eleron.client.util;

import com.google.common.collect.Maps;
import com.sindercube.eleron.client.mixin.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InclusiveKeyBinding extends KeyBinding {

	public static final Map<InputUtil.Key, List<InclusiveKeyBinding>> KEY_TO_MULTIPLE_BINDINGS = Maps.newHashMap();
	public static final Map<String, InclusiveKeyBinding> UNIQUE_KEYS_BY_ID = Maps.newHashMap();

	public static InclusiveKeyBinding create(String translationKey, InputUtil.Type type, int code, String category) {
		InputUtil.Key key = type.createFromCode(code);
		KeyBinding oldKeybind = KeyBindingAccessor.getKeyToBindings().get(key);
		return new InclusiveKeyBinding(translationKey, type, code, category, oldKeybind);
	}

	protected InclusiveKeyBinding(String translationKey, InputUtil.Type type, int code, String category, KeyBinding oldKeybind) {
		super(translationKey, type, code, category);
		InputUtil.Key key = ((KeyBindingAccessor)this).getBoundKey();

		KeyBindingAccessor.getKeyToBindings().put(key, oldKeybind);
		if (!KEY_TO_MULTIPLE_BINDINGS.containsKey(key)) KEY_TO_MULTIPLE_BINDINGS.put(key, new ArrayList<>());
		KEY_TO_MULTIPLE_BINDINGS.get(key).add(this);

		KeyBindingAccessor.getKeysById().remove(translationKey);
		UNIQUE_KEYS_BY_ID.put(translationKey, this);
	}

}
