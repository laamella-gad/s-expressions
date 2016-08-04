; Sample config file
; At the lowest level, we have a key->value mapping
(key value)
(keys-dont-have-spaces but the values can have spaces. All    whitespace    is turned into a single space.)
(but! "when you quote a value, spaces    are     retained")
(my-value-is-an-empty-string)

; We can also have binary data
(greeting |SGVsbG8gd29ybGQh|)

; Here we have a hierarchy.
(application
  (window
     (width 320)
     ; So VGA!
     (height 200)
     (x 100)
     (y 100))
  (name Hello World!))

; Nice huh?