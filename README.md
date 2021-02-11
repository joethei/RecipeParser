# RecipeParser
![t](https://shields.io/github/v/release/joethei/RecipeParser?include_prereleases)

Parse Recipes from Websites and files that use the json-ld format

## Getting started

```xml
<dependency>
    <groupId>xyz.joethei</groupId>
    <artifactId>receipe-parser</artifactId>
    <version>${version}</version>
</dependency>
```

```java
var url = //Website url / path to text file containing recipe data
Recipe recipe = RecipeParser.parse(url);
```