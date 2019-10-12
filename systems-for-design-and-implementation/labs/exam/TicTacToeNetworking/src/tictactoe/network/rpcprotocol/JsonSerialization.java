package tictactoe.network.rpcprotocol;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class JsonSerialization
{
    private class ListOfJson<T> implements ParameterizedType
    {
        private Class<?> wrapped;

        public ListOfJson(Class<T> wrapper)
        {
            this.wrapped = wrapper;
        }

        @Override
        public Type[] getActualTypeArguments()
        {
            return new Type[] { wrapped };
        }

        @Override
        public Type getRawType()
        {
            return List.class;
        }

        @Override
        public Type getOwnerType()
        {
            return null;
        }
    }

    private Gson gson;

    JsonSerialization()
    {
        this.gson = new Gson();
    }

    public <T> String toJSON(T src, Class<T> type)
    {
        //System.out.println("toJSON(T src, Class<T> type)" + type.getName());
        return gson.toJson(src, type);
    }

    public <T> T fromJSON(Reader json, Class<T> type) {
        //System.out.println("fromJSON(Reader json, Class<T> type)" + type.getName());
        return gson.fromJson(json, type);
    }

    public <T> T fromJSON(String json, Class<T> type) {
        //System.out.println("fromJSON(String json, Class<T> type)" + type.getName());
        return gson.fromJson(json, type);
    }

    public <T> T convertToCustomObject(Object object, Class<T> clazz)
    {
        String json = toJSON((LinkedTreeMap) object, LinkedTreeMap.class);
        //System.out.println("convertToCustomObject - " + clazz.getName() + " JSON: " + json);
        return fromJSON(json, clazz);
    }

    public <T> T convertToCustomObject(String json, Class<T> clazz)
    {
        //System.out.println("convertToCustomObject - " + clazz.getName() + " JSON: " + json);
        return fromJSON(json, clazz);
    }

    public <T> List<T> convertToCustomList(Object object, Class<T> type)
    {
        String json = toJSON((LinkedTreeMap) object, LinkedTreeMap.class);
        //System.out.println("convertToCustomList - " + type.getName() + " JSON: " + json);
        return gson.fromJson(json, new ListOfJson<>(type));
    }

    public <T> List<T> convertToCustomList(String json, Class<T> type)
    {
        //System.out.println("convertToCustomList - " + type.getName() + " JSON: " + json);
        return gson.fromJson(json, new ListOfJson<>(type));
    }
}
