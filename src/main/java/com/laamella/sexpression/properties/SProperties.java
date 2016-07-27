package com.laamella.sexpression.properties;

import com.laamella.sexpression.SExpressionsParser;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.SExpression;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static com.laamella.sexpression.CharSource.*;

public class SProperties implements Iterable<Map.Entry<String, String>> {
	private final Map<String, String> store = new HashMap<>();
	private final SExpressionsParser parser = new SExpressionsParser(new SPropertiesCallback());

	public SProperties() {
	}

	public SProperties(String file) throws IOException {
		loadResource(file);
	}

	public SProperties(Reader reader) throws IOException {
		load(reader);
	}

	public void load(Reader reader) throws IOException {
		push(reader, parser);
	}

	public void loadResource(String file) throws IOException {
		pushResource(file, UTF8, parser);
	}

	public Optional<String> get(String key) {
		return Optional.ofNullable(store.get(key));
	}

	public String get(String key, String def) {
		return store.getOrDefault(key, def);
	}

	@Override
	public Iterator<Map.Entry<String, String>> iterator() {
		return store.entrySet().iterator();
	}

	private class SPropertiesCallback extends SExpressionsParser.Callback.Adapter {
		@Override
		public void onExpression(AtomList expression) {
			eval("", expression);
		}

		private void eval(String nesting, AtomList list) {
			SExpression[] values = list.values.toArray(new SExpression[list.values.size()]);
			if (list.isEmpty()) {
				//
			} else if (list.isAllAtoms()) {
				String key = nesting + values[0].toAtom().value;
				StringBuilder value = new StringBuilder();
				String space = "";
				for (int i = 1; i < values.length; i++) {
					value.append(space).append(values[i].toAtom().value);
					space = " ";
				}
				store.put(key, value.toString());
			} else if (values[0].isAtom()) {
				String innerNesting = nesting + values[0].toAtom().value + ".";
				for (int i = 1; i < values.length; i++) {
					if (values[i].isList()) {
						eval(innerNesting, values[i].toList());
					} else {
						// not sure what semantics should be here
					}
				}
			} else {
				throw new IllegalStateException("Found ((. Don't know what to do.");
			}
		}
	}
}
