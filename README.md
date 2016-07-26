# s-expressions library for Java

S-Expressions are a very simple, very flexible data format.
It describes tree structures, or in this case: lists of lists or strings.

## Goals

Have a simple, efficient format for...

* data transfer
* configuration files
* object serialization

Additionally, make the API flexible enough to support other use cases, like implementing a Lisp.

## Examples

### Sample for serialization

TODO

### Sample for configuration files

A simple configuration file reader is supplied.
It can read the following kind of property files:

```lisp
; Sample config file
; At the lowest level, we have a key->value mapping
(key value)
(keys-dont-have-spaces but the values can have spaces. All    whitespace    is turned into a single space.)
(but! "when you quote a value, spaces    are     retained")
(my-value-is-an-empty-string)

; Here we have a hierarchy.
(application
  (window
     (width 320)
     (height 200)
     (x 100)
     (y 100))
  (name Hello World!))
```

It can be read as if it were a .properties file:

```java
SProperties properties = new SProperties();
properties.load("/config.s");

properties.get("application.window.height").ifPresent(System.out::println);

for (Map.Entry<String, String> e : properties) {
    System.out.println(e.getKey() + "->" + e.getValue());
}
```

### Sample for data storage

TODO

## Current features

* reading s-expressions
* streaming data
* writing s-expressions
* one line comments by starting a line with a semicolon
* quoting by putting text between " "

Coming features can be found in the issue tracker.
