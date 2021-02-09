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

### Simple schema

A simple schema definition defines a minimal contract,
just enough to know the basic structure of a file. 

It uses the following data types:

- atoms `sample-atom` `125135` `15-5-2029`
- lists `(1 2 3)` `(cheese onion)`
- mappings `((name Piet) (age 15))`

And the schema is defined as follows:

- atoms are implicitly defined by lists and mappings
- lists
  - `(list Coordinate x y)` for a list called "Coordinate" of two atoms named "x" and "y" like `(15 99)`
  - `(list Name 'name name)` for a list named "Name" of one constant atom, and one atom named "name" like `(name Pietje)`   
  - `(list ages)` for a list of any amount of atoms named "ages" like `(46 32 11 19)`
  - `(list order price tracking-info...)`  for a list with two atoms named "order" and "price" and any amount of atoms following it, named "tracking-info" like `("bag of peanuts" 15.99 "entered" "packed" "sent")`
- mappings
  - `(map (name) (age))` for a map with two (optional) keys with an atom value like `((name Piet) (age 15))`

Combined, these become:

- `(map person (name) (address (list street number)))` for a map named "person" with two (optional) keys, where name has an atom value and adress has a list value like `(person (name Piet) (address Hoofdstraat 15`))
- `(list `