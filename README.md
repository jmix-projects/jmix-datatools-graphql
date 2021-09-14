# jmix-datatools-graphql

Adds graphql entity restore endpoint.

### Usage:

#### Add starter to your build.gradle

```groovy
implementation 'io.jmix.datatools:jmix-datatools-graphql-starter:<version>'
```

Module `jmix-datatools-graphql` will be added only if both of `jmix-datatools` and `jmix-graphql` modules present, e.g.
added using their starters:

```groovy
implementation 'io.jmix.graphql:jmix-graphql-starter'
implementation 'io.jmix.datatools:jmix-datatools-starter'
```

#### Use mutation:

```
restoreEntities(className: String!, ids: [String]!):Int
```
 
