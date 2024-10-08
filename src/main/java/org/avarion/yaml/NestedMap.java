package org.avarion.yaml;

import org.avarion.yaml.exceptions.DuplicateKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

class NestedMap {
	private final Map<String, Object> map = new LinkedHashMap<>();

	@SuppressWarnings("unchecked")
	public void put(@NotNull String key, @Nullable String comment, Object value) throws DuplicateKey {
		String[] keys = key.split("\\.");

		Map<String, Object> current = map;
		for (int i = 0; i < keys.length - 1; i++) {
			final String k = keys[i];
			current = (Map<String, Object>) current.computeIfAbsent(k, x -> new LinkedHashMap<>());
		}
		final String lastKey = keys[keys.length - 1];
		if (current.containsKey(lastKey)) {
			throw new DuplicateKey(key);
		}

		current.put(lastKey, new NestedNode(value, comment));
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public static class NestedNode {
		public final Object value;
		public final @Nullable String comment;

		public NestedNode(@Nullable Object value, @Nullable String comment) {
			this.value = value;
			this.comment = comment;
		}
	}
}
