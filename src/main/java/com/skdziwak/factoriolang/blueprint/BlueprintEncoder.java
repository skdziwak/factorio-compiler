package com.skdziwak.factoriolang.blueprint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;

public class BlueprintEncoder {
    public static String encode(Blueprint blueprint) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String blueprintJsonString = objectMapper.writeValueAsString(new BlueprintWrapper(blueprint));
        byte[] blueprintJsonBytes = blueprintJsonString.getBytes(StandardCharsets.UTF_8);
        byte[] compressedBytes = compress(blueprintJsonBytes);
        return "0" + new String(Base64.getEncoder().encode(compressedBytes));
    }

    private static byte[] compress(final byte[] data) throws IOException {
        final Deflater deflater = new Deflater();
        deflater.setLevel(9);
        deflater.setInput(data);

        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length))
        {
            deflater.finish();
            final byte[] buffer = new byte[4096];
            while (!deflater.finished())
            {
                final int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }

            return outputStream.toByteArray();
        }
    }

    private record BlueprintWrapper(Blueprint blueprint) { }
}
