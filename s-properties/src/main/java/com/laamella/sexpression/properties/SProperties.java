package com.laamella.sexpression.properties;

import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import com.laamella.sexpression.visitor.Visitor;
import javaslang.collection.Vector;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

public class SProperties //implements Iterable<Map.Entry<String, String>> 
{
    private Document document;

    public SProperties() {
    }

    public SProperties(String file) throws IOException {
        loadResource(file);
    }

    public SProperties(Reader reader) throws IOException {
        load(reader);
    }

//	public SProperties(Properties properties) throws IOException {
//		load(properties);
//	}

    public void load(Reader reader) throws IOException {
        document = Document.from(reader);
    }

    public void loadResource(String file) throws IOException {
        document = Document.fromResource(file);
    }

//	public void load(Properties properties) {
//		for (String key : properties.stringPropertyNames()) {
//			set(key, properties.getProperty(key));
//		}
//	}

//    public Properties asProperties() {
//        Properties properties = new Properties();
//        for (Map.Entry<String, String> e : this) {
//            properties.put(e.getKey(), e.getValue());
//        }
//        return properties;
//    }

//	private void set(String key, String value) {
//		store.put(key, value);
//	}

//    public Optional<String> get(String key) {
//        Vector<String> keyparts = Vector.of(key.split("\\."));
//
//        try {
//            return document.visit(new Visitor.Adapter<Void, Optional<String>>() {
//
//                @Override
//                public EnterDecision enter(AtomList atomList, Void arg) throws Exception {
//                    return super.enter(atomList, arg);
//                }
//
//            }, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//	public String get(String key, String def) {
//		return store.getOrDefault(key, def);
//	}

//	@Override
//	public Iterator<Map.Entry<String, String>> iterator() {
//		return store.entrySet().iterator();
//	}


    // TODO make better
    public void print() {
//        LiteralPrinterVisitor visitor = new LiteralPrinterVisitor();
        // TODO should have
    }

}
