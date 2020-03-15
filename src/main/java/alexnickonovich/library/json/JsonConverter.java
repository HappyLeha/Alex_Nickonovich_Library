package alexnickonovich.library.json;
import alexnickonovich.library.interfaces.JsonSerializable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.util.ArrayList;

public class JsonConverter {
    public static String convertToJson(JsonSerializable item) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        String json = mapper.writeValueAsString(item);
        return json;
    }
    public static<T extends JsonSerializable> T convertFromJson(String json,Class<T> type) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        JsonSerializable item=mapper.readValue(json,type);
        return (T)item;
    }
    public static<T extends JsonSerializable> String convertToListJson(ArrayList<T> list) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        String json=mapper.writeValueAsString(list);
        return json;
    }
    public static<T extends JsonSerializable> ArrayList<T> convertFromListJson(String json,Class<T> type) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        TypeFactory factory = mapper.getTypeFactory();
        CollectionType listType = factory.constructCollectionType(ArrayList.class,type);
        ArrayList<T> list = mapper.readValue(json, listType);
        return list;
    }
}
