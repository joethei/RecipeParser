/*
 * Copyright (c) 2020. Johannnes Theiner <kontakt@joethei.xyz>
 */

package xyz.joethei.recipe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * @author Johannes Theiner
 * @version 0.1
 * @since 2020-09-29,
 *
 **/
@Value.Immutable
@JsonSerialize(as = ImmutableNutrition.class)
@JsonInclude
public interface Nutrition {

    Optional<String> calories();
    Optional<String> carbohydrateContent();
    Optional<String> cholesterolContent();
    Optional<String> fatContent();
    Optional<String> fiberContent();
    Optional<String> proteinContent();
    Optional<String> saturatedFatContent();
    Optional<String> servingSize();
    Optional<String> sodiumContent();
    Optional<String> sugarContent();
    Optional<String> transFatContent();
    Optional<String> unsaturatedFatContent();
}