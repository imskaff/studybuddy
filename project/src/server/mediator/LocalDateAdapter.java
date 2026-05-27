package server.mediator;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Gson {@link TypeAdapter} that serialises and deserialises {@link LocalDate} values
 * as date strings (e.g. {@code "2000-01-31"}).
 * 
 * Register this adapter with a {@code GsonBuilder} to enable JSON
 * conversion of {@code LocalDate} fields in request and response objects.
 * 
 * @author Kamila Jeremicz
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate>
{
    /**
     * Writes a {@link LocalDate} to JSON as a string.
     *
     * @param jsonWriter the Gson writer to write to
     * @param localDate  the date to serialise
     * @throws IOException if writing fails
     */
    @Override public void write(JsonWriter jsonWriter, LocalDate localDate)
        throws IOException
    {
        jsonWriter.value(localDate.toString());
    }

    /**
     * Reads a {@link LocalDate} from a string.
     *
     * @param jsonReader the Gson reader to read from
     * @return the parsed {@link LocalDate}
     * @throws IOException if reading or parsing fails
     */
    @Override public LocalDate read(JsonReader jsonReader) throws IOException
    {
        return LocalDate.parse(jsonReader.nextString());
    }
}
