/*
 * Copyright (c) 2020. Johannnes Theiner <kontakt@joethei.xyz>
 */

package xyz.joethei.recipe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Johannes Theiner
 * @version 0.1
 * @since 2020-09-27
 **/

@Value.Immutable
@JsonSerialize(as = ImmutableRecipe.class)
@JsonDeserialize(as = ImmutableRecipe.class, using = RecipeDeserializer.class)
@JsonInclude
public interface Recipe {

    String name();

    Optional<String> description();

    Optional<String> recipeYield();

    Optional<String> totalTime();

    Optional<String> cookTime();

    Optional<String> prepTime();

    String recipeInstructions();

    Optional<LocalDateTime> datePublished();

    List<String> recipeIngredient();

    Optional<Nutrition> nutrition();
}