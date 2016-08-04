package com.laamella.sexpression.properties;

import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import com.laamella.sexpression.model.SExpression;
import com.laamella.sexpression.visitor.PrinterVisitor;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static com.laamella.sexpression.model.Factory.atom;
import static com.laamella.sexpression.model.Factory.list;


public class SProperties implements Iterable<Map.Entry<String, String>> {
    private Document document;

    public SProperties() {
    }

    public SProperties(String file) throws IOException {
        loadResource(file);
    }

    public SProperties(Reader reader) throws IOException {
        load(reader);
    }

    public SProperties(Properties properties) throws IOException {
        load(properties);
    }

    public void load(Reader reader) throws IOException {
        document = Document.from(reader);
    }

    public void loadResource(String file) throws IOException {
        document = Document.fromResource(file);
    }

    public void load(Properties properties) {
        for (String key : properties.stringPropertyNames()) {
            set(key, properties.getProperty(key));
        }
    }

    public Properties asProperties() {
        Properties properties = new Properties();
        for (Map.Entry<String, String> e : this) {
            properties.put(e.getKey(), e.getValue());
        }
        return properties;
    }

    public Optional<String> set(String searchKey, String newValue) {
        Optional<String> oldValue = eval(new Callback() {
            @Override
            public Optional<String> onProperty(String key, String value, AtomList list) {
                if (key.equals(searchKey)) {
                    list.setNodes(list.asVector().get(0), atom(newValue));
                    return Optional.of(value);
                }
                return Optional.empty();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
        if (!oldValue.isPresent()) {
            // lazy solution
            document.setNodes(document.asVector().append(list(atom(searchKey), atom(newValue))));
        }
        return oldValue;
    }

    public Optional<String> get(String searchKey) {
        return eval(new Callback() {
            @Override
            public Optional<String> onProperty(String key, String value, AtomList list) {
                if (key.equals(searchKey)) {
                    return Optional.of(value);
                }
                return Optional.empty();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        Map<String, String> entries = new HashMap<>();
        eval(new Callback() {
            @Override
            public Optional<String> onProperty(String key, String value, AtomList list) {
                entries.put(key, value);
                return Optional.empty();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
        return entries.entrySet().iterator();
    }


    public void save(Appendable appendable) throws Exception {
        document.visit(new PrinterVisitor(), appendable);
    }

    private Optional<String> eval(Callback callback) {
        for (AtomList atomList : document.asVector().filter(SExpression::isList).map(SExpression::asList)) {
            Optional<String> value = eval("", atomList, callback);
            if (value.isPresent()) {
                return value;
            }
        }
        return Optional.empty();
    }

    private Optional<String> eval(String nesting, AtomList list, Callback callback) {
        SExpression[] values = list.asVector().toJavaArray(SExpression.class);
        if (list.isEmpty()) {
            callback.onError(new IllegalStateException("Found an empty list. Don't know what to do."));
        } else if (list.isAllAtoms()) {
            String key = nesting + values[0].asAtom().value();
            StringBuilder value = new StringBuilder();
            String space = "";
            for (int i = 1; i < values.length; i++) {
                value.append(space).append(values[i].asAtom().value());
                space = " ";
            }
            return callback.onProperty(key, value.toString(), list);
        } else if (values[0].isAtom()) {
            String innerNesting = nesting + values[0].asAtom().value() + ".";
            for (int i = 1; i < values.length; i++) {
                if (values[i].isList()) {
                    Optional<String> v = eval(innerNesting, values[i].asList(), callback);
                    if (v.isPresent()) {
                        return v;
                    }
                } else {
                    callback.onError(new IllegalStateException("Found a list with atoms in the middle of it. Don't know what to do."));
                }
            }
        } else {
            callback.onError(new IllegalStateException("Found ((. Don't know what to do."));
        }
        return Optional.empty();
    }

    interface Callback {
        Optional<String> onProperty(String key, String value, AtomList list);

        void onError(Throwable e);
    }
}

