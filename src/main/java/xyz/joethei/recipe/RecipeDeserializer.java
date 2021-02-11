/*
 * Copyright (c) 2020. Johannnes Theiner <kontakt@joethei.xyz>
 */

package xyz.joethei.recipe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.sisyphsu.dateparser.DateParserUtils;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author Johannes Theiner
 * @version 0.1
 * @since 2020-09-29
 **/
public class RecipeDeserializer extends StdDeserializer<Recipe> {

    private static final long serialVersionUID = -7183438859870030590L;

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
            String unescaped = StringEscapeUtils.unescapeHtml4(stringBuilder.toString());
            builder.recipeInstructions(unescaped);
        }else if(instructions.isTextual()) {
            String unescaped = StringEscapeUtils.unescapeHtml4(instructions.asText());
            builder.recipeInstructions(unescaped);
        }

        builder.description(getIfExists(node, "description"));
        builder.recipeYield(getIfExists(node, "recipeYield"));
        builder.totalTime(getIfExists(node, "totalTime"));
        builder.cookTime(getIfExists(node, "cookTime"));
        builder.prepTime(getIfExists(node, "prepTime"));

        if (node.get("datePublished") != null) {
            var date = getIfExists(node, "datePublished");
            date.ifPresent(s -> {
                LocalDateTime localDate = DateParserUtils.parseDateTime(s);
                builder.datePublished(localDate);
            });
        }
        if (node.get("nutrition") != null && !node.get("nutrition").asText().isEmpty()) {
            builder.nutrition(mapper.readValue(node.get("nutrition").asText(), Nutrition.class));
        }

        return builder.build();
    }
}