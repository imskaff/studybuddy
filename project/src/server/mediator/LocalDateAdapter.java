package server.mediator;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

// Adapter used by Gson to convert LocalDate objects
// to JSON Strings and back to LocalDate again.
public class LocalDateAdapter extends TypeAdapter<LocalDate>
{
  // Java object -> JSON
  @Override public void write(JsonWriter jsonWriter, LocalDate localDate)
      throws IOException
  {
    jsonWriter.value(localDate.toString());
  }

  // JSON -> Java object
  @Override public LocalDate read(JsonReader jsonReader) throws IOException
  {
    return LocalDate.parse(jsonReader.nextString());
  }
}
