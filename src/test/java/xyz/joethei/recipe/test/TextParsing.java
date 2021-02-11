/*
 * Copyright (c) 2020. Johannnes Theiner <kontakt@joethei.xyz>
 */

package xyz.joethei.recipe.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import xyz.joethei.recipe.Recipe;
import xyz.joethei.recipe.RecipeParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Johannes Theiner
 * @version 0.1
 * @since 2020-09-29
 **/
public class TextParsing {

    @Test
    public void validParsing() throws IOException, URISyntaxException {
        Recipe recipe = RecipeParser.parse(Path.of(Objects.requireNonNull(getClass().getClassLoader().getResource("recipe.json")).toURI()));
        assertNotNull(recipe);
        assertEquals("Tomatenspaghetti", recipe.name());
        assertEquals(Arrays.asList("300 g Spaghetti", "40 g Butter", "400 g passierte Tomaten"), recipe.recipeIngredient());
        assertEquals(Optional.empty(), recipe.cookTime());
        assertEquals(Optional.of("P0DT0H15M"), recipe.totalTime());
    }

    @Test
    public void parse() throws URISyntaxException, IOException {
        var recipe = RecipeParser.parse(Path.of(Objects.requireNonNull(getClass().getClassLoader().getResource("recipe1.json")).toURI()));
        assertNotNull(recipe);
        assertEquals("Tomatensuppe", recipe.name());
    }
}