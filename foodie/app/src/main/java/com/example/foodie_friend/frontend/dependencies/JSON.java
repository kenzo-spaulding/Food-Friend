package com.example.foodie_friend.frontend.dependencies;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class JSON {
    JsonObject json;
    Gson gson;

    public JSON(){
        json = new JsonObject();
        gson = new Gson();
    }

    public JsonObject getJson() {
        return json;
    }

    public JSON(String json){
        File file = new File(json);
        if (file.exists())
            this.json = (new JSON(file)).getJson();
        else {
                this.json = toJson(json);
            }
    }
    public JSON(URI uri){
        this();
        this.json = toJson(uri);
    }

    public JSON(URL url){
        this();
        this.json = toJson(url);
    }

    public JSON(File file){
        this();
        this.json = toJson(file);
    }

    private JsonObject toJson(String str){
        JsonElement element = JsonParser.parseString(str);
        return element.getAsJsonObject();
    }

    private JsonObject toJson(File file){
        String jsonStr = toString(file);
        return toJson(jsonStr);
    }

    private JsonObject toJson(URL url){
        String jsonStr = toString(url);
        return toJson(jsonStr);
    }

    private JsonObject toJson(URI uri){
        String jsonStr = toString(uri);
        return toJson(jsonStr);
    }

    private String toString(File file){
        try(Reader reader = new FileReader(file.getAbsolutePath())){
            json = gson.fromJson(reader, JsonObject.class);
            return json.getAsString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "{}";
    }

    private String toString(URL url){
        InputStream stream = null;
        try {
            stream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if (stream != null)
                try {
                    stream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
        }
        return "{}";
    }

    private String toString(URI uri){
        try {
            return toString(uri.toURL());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return "{}";
    }

    @Override
    public String toString(){
        return json.getAsString();
    }

    public static void main(String[] args) throws URISyntaxException, MalformedURLException {
        //Should return an error message
        String urlString = "http://dummy.restapiexample.com/api/v1/employees";
        URI uri = new URI(urlString);
        URL url = uri.toURL();

        JSON jsonString = new JSON(urlString);
        System.out.println("urlString:\n");
        System.out.println(jsonString.getJson());
        System.out.println("\n\n\n");

        JSON jsonURI = new JSON(uri);
        System.out.println("urlString:\n");
        System.out.println(jsonURI.getJson());
        System.out.println("\n\n\n");

        JSON jsonURL = new JSON(url);
        System.out.println("urlString:\n");
        System.out.println(jsonURL.getJson());
        System.out.println("\n\n\n");
    }
}

