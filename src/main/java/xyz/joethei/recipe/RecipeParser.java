/*
 * Copyright (c) 2020. Johannnes Theiner <kontakt@joethei.xyz>
 */

package xyz.joethei.recipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Johannes Theiner
 * @version 0.1
 * @since 2020-09-27
 **/
public class RecipeParser {

    @Nullable
    public static Recipe parse(@NotNull final String data) throws IOException {
        if (data.lines().anyMatch(s -> s.startsWith("https://") || s.startsWith("http://"))) {
            Optional<String> url = data.lines().filter(s -> s.startsWith("https://") || s.startsWith("http://")).findFirst();
            if (url.isPresent()) return parseWebsite(url.get());
        }

        if (data.startsWith("{"))
            return parseContent(data);

        return null;
    }

    @Nullable
    public static Recipe parse(@NotNull final Path path) throws IOException {
        return parseContent(Files.readString(path));
    }


    @Nullable
    private static Recipe parseWebsite(@NotNull final String url) throws IOException {
        Document doc = Jsoup.parse(new URL(url), 1000 * 20);
        for (var element : doc.getElementsByTag("script")) {
            if (element.data().contains("\"@type\"") && element.data().contains("Recipe")) {
                return parseContent(element.data());
            }
        }
        return null;
    }

    @Nullable
    private static Recipe parseContent(@NotNull final String content) throws IOException {
        var node = new ObjectMapper().readTree(content);
        if (node.isObject()) return parseObject(content);

        if (node.isArray()) {
            AtomicReference<JsonNode> obj = new AtomicReference<>();
            node.elements().forEachRemaining(jsonNode -> {
                if (jsonNode.get("@type").asText().equals("Recipe")) {
                    obj.set(jsonNode);
                }
            });
            return parseObject(obj.get().asText());
        }
        return null;
    }

    @Nullable
    private static Recipe parseObject(@NotNull final String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, Recipe.class);
    }
}