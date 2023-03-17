package http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {


        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        @Override
        public void write(JsonWriter jsonWriter, LocalTime localTime) throws IOException {
            jsonWriter.value(localTime.format(formatter));
        }

        @Override
        public LocalTime read(final JsonReader jsonReader) throws IOException {
            return LocalTime.parse(jsonReader.nextString(), formatter);
        }

}
