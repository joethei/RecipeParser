/*
 * Copyright (c) 2020. Johannnes Theiner <kontakt@joethei.xyz>
 */

package xyz.joethei.recipe;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Johannes Theiner
 * @version 0.1
 * @since 2020-09-29
 **/
public class RecipeDeserializer extends StdDeserializer<Recipe> {

    public RecipeDeserializer() {
        this(null);
    }

    public RecipeDeserializer(Class<?> vc) {
        super(vc);
    }

    private Optional<String> getIfExists(JsonNode node, String field) {
        if (node.get(field) != null) {
            return Optional.of(node.get(field).asText());
        }
        return Optional.empty();
    }

    @Override
    public Recipe deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);


        var builder = ImmutableRecipe.builder();
        var mapper = new ObjectMapper();

        builder.name(node.get("name").asText());
        node.get("recipeIngredient").elements().forEachRemaining(jsonNode -> builder.addRecipeIngredient(jsonNode.asText()));

        var instructions = node.get("recipeInstructions");
        if(instructions.isArray()) {
            StringBuilder stringBuilder = new StringBuilder();
            instructions.elements().forEachRemaining(jsonNode -> stringBuilder.append(jsonNode.asText()));
            builder.recipeInstructions(stringBuilder.toString());
        }else if(instructions.isTextual()) {
            builder.recipeInstructions(instructions.asText());
        }

        builder.description(getIfExists(node, "description"));
        builder.recipeYield(getIfExists(node, "recipeYield"));
        builder.totalTime(getIfExists(node, "totalTime"));
        builder.cookTime(getIfExists(node, "cookTime"));
        builder.prepTime(getIfExists(node, "prepTime"));

        if (node.get("datePublished") != null) {
            var date = getIfExists(node, "datePublished");
            date.ifPresent(s -> {
                var localDate = LocalDate.parse(s);
                builder.datePublished(localDate);
            });
        }
        if (node.get("nutrition") != null && !node.get("nutrition").asText().isEmpty()) {
            builder.nutrition(mapper.readValue(node.get("nutrition").asText(), Nutrition.class));
        }

        return builder.build();
    }
}