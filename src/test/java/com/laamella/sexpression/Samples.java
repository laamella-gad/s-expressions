package com.laamella.sexpression;

import com.laamella.sexpression.model.AtomList;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.laamella.sexpression.CharSource.*;

public class Samples {
	@Test
	public void configurationFile() throws IOException {
		List<Object> objects = parseFile("/config.s");
		System.out.println(objects);

	}

	private List<Object> parseFile(String file) throws IOException {
		List<Object> result = new ArrayList<>();
		SExpressionsParser parser = new SExpressionsParser(new SExpressionsParser.Callback.Adapter() {
			@Override
			public void onExpression(AtomList expression) {
				result.add(expression);
			}

			@Override
			public void onOrphanText(String text) {
				result.add(text);
			}
		});
		pushResource(file, UTF8, parser);
		return result;
	}

}
