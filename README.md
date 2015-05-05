# precook

Utilities for pre-rendering ClojureScript-based pages in JVM. This helps for example to run a static site developed using [Om](https://github.com/omcljs/om) and [bidi](https://github.com/juxt/bidi) in a robot-friendly way.

[![Clojars Project](http://clojars.org/precook/latest-version.svg)](http://clojars.org/precook)

## Usage

See `examples` and the example profile in `project.clj`. To run the example:

    lein with-profile +example ring uberjar
    lein -jar target/precook-0.1.0-SNAPSHOT-standalone.jar

## License

Copyright © 2015 [Sander Dijkhuis](http://sanderdijkhuis.nl/)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
