# s-expressions library for Java

S-Expressions are a very simple, very flexible data format.
It describes tree structures, or in this case: lists of lists or strings.

TODO some examples here

What is supported right now:

* reading s-expressions
* streaming data
* writing s-expressions
* one line comments by starting a line with a semicolon
* quoting by putting text between " "

What is on the road map:

* using quoting while writing
* optional retaining of comments while writing
* optional retaining of formatting while writing
* serializing and deserializing of object graphs to s-expressions
* optional hexadecimal notation: #03#
* optional hexadecimal unicode notation: #0344#
* optional base64 notation: |YWJj|
* more things from http://people.csail.mit.edu/rivest/Sexp.txt

What is nice to have:

* verbatim encoding
* the ' quote

Any ideas?