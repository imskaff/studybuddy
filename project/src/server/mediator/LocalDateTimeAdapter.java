package server.mediator;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Gson {@link TypeAdapter} that serialises and deserialises {@link LocalDateTime}
 * values as date-time strings (e.g. {@code "2026-06-01T10:00:00"}).
 * 
 * Register this adapter with a {@code GsonBuilder} alongside
 * {@link LocalDateAdapter} to support full date-time fields in request and
 * response objects.
 * 
 * @author Kamila Jeremicz
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime>
{
    /**
     * Writes a {@link LocalDateTime} to JSON as a string.
     *
     * @param jsonWriter    the Gson writer to write to
     * @param localDateTime the date-time to serialise
     * @throws IOException if writing fails
     */
    @Override public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException
    {
        jsonWriter.value(localDateTime.toString());
    }

    /**
     * Reads a {@link LocalDateTime} from a string.
     *
     * @param jsonReader the Gson reader to read from
     * @return the parsed {@link LocalDateTime}
     * @throws IOException if reading or parsing fails
     */
    @Override public LocalDateTime read(JsonReader jsonReader) throws IOException
    {
        return LocalDateTime.parse(jsonReader.nextString());
    }
}
