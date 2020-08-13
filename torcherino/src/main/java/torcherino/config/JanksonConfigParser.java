package torcherino.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.DeserializerFunction;
import blue.endless.jankson.api.Marshaller;
import blue.endless.jankson.api.SyntaxError;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.apache.logging.log4j.Marker;
import torcherino.Torcherino;

public final class JanksonConfigParser
{
    private final Jankson _jankson;

    private JanksonConfigParser(final Jankson jankson)
    {
        _jankson = jankson;
    }

    public <F> F load(final Class<F> configClass, final Supplier<F> defaultConfig, final Path configPath, final Marker marker)
    {
        final Path folder = configPath.getParent();
        if (Files.notExists(folder))
        {
            try
            {
                Files.createDirectories(folder);
            }
            catch (final IOException e)
            {
                throw new RuntimeException(MessageFormat.format("[{0}] Cannot create directories required for config.", marker.getName()), e);
            }
        }
        if (!Files.exists(configPath))
        {
            final F config = defaultConfig.get();
            if (save(config, configPath, marker))
            {
                throw new RuntimeException(MessageFormat.format("[{0}] Failed to save initial config, look at logs for more info.", marker.getName()));
            }
            return config;
        }

        try (final InputStream configStream = Files.newInputStream(configPath))
        {
            final JsonObject uConfig = _jankson.load(configStream);
            final JsonObject dConfig = (JsonObject) _jankson.toJson(defaultConfig.get());
            final JsonObject delta = uConfig.getDelta(dConfig); // returns keys overridden from default
            if (delta.size() > 0)
            {
                final F config = _jankson.fromJson(uConfig, configClass);
                save(config, configPath, marker);
                Torcherino.LOGGER.info(MessageFormat.format("[{0}] New config keys found, saved merged config.", marker.getName()));
            }
            return _jankson.fromJson(uConfig, configClass);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(MessageFormat.format("[{0}] IO error occurred when loading config.", marker.getName()), e);
        }
        catch (final SyntaxError e)
        {
            throw new RuntimeException(MessageFormat.format("[{0}] Syntax error occurred when loading config.", marker.getName()), e);
        }
    }

    /**
     * @param config Config to save.
     * @param configPath Path to save config to.
     * @param marker Marker for log4j logging in case error occurs.
     * @return TRUE when config failed to save.
     */
    public <F> boolean save(final F config, final Path configPath, final Marker marker)
    {
        return save(_jankson.toJson(config), configPath, marker);
    }

    private boolean save(final JsonElement config, final Path configPath, final Marker marker)
    {
        try (final BufferedWriter configStream = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE))
        {
            configStream.write(config.toJson(true, true));
        }
        catch (final IOException e)
        {
            Torcherino.LOGGER.warn(marker, MessageFormat.format("[{0}] IO error occurred whilst saving config.", marker.getName()), e);
            return true;
        }
        return false;
    }

    public final static class Builder
    {
        private final Jankson.Builder _builder;

        private Builder()
        {
            _builder = new Jankson.Builder();
        }

        public static Builder create()
        {
            return new Builder();
        }

        public <A, B> Builder deSerializer(final Class<A> from, final Class<B> to, final DeserializerFunction<A, B> deserializer, final BiFunction<B, Marshaller, JsonElement> serializer)
        {
            _builder.registerSerializer(to, serializer);
            _builder.registerDeserializer(from, to, deserializer);
            return this;
        }

        public JanksonConfigParser build()
        {
            return new JanksonConfigParser(_builder.build());
        }
    }
}