package com.interview.lib;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant {

    String id;
    String name;
    String image_url;
    boolean closed;
    List<String> categories;
    float latitude;
    float longitude;
    String address;
    String headQuery;
    float distance;

    public Restaurant(String id) {
        this.id = id;
    }

    public Restaurant(String name, String image_url, boolean closed, List<String> categories,
                      float latitude, float longitude, String address, String headQuery,
                      float distance) {
        this.name = name;
        this.image_url = image_url;
        this.closed = closed;
        this.categories = categories;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.headQuery = headQuery;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public boolean isClosed() {
        return closed;
    }

    public List<String> getCategories() {
        return categories;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getHeadQuery() {
        return headQuery;
    }

    public float getDistance() {
        return distance;
    }

    public String getUrl() {
        return image_url;
    }

    public static ArrayList<Logo> logos() {
        return new ArrayList<>(Arrays.asList(
                new Logo("mcdonalds", "mcdonalds.com"),
                new Logo("in n out", "innout.com"),
                new Logo("burger king", "bk.com"),
                new Logo("panda express", "pandaexpress.com"),
                new Logo("starbucks", "starbucks.com"),
                new Logo("rubys", "rubys.com"),
                new Logo("subway", "subway.com"),
                new Logo("mcdonalds", "mcdonalds.com"),
                new Logo("in n out", "innout.com"),
                new Logo("burger king", "bk.com"),
                new Logo("panda express", "pandaexpress.com"),
                new Logo("starbucks", "starbucks.com"),
                new Logo("rubys", "rubys.com"),
                new Logo("subway", "subway.com"),
                new Logo("mcdonalds", "mcdonalds.com"),
                new Logo("in n out", "innout.com"),
                new Logo("burger king", "bk.com"),
                new Logo("panda express", "pandaexpress.com"),
                new Logo("starbucks", "starbucks.com"),
                new Logo("rubys", "rubys.com"),
                new Logo("subway", "subway.com"),
                new Logo("mcdonalds", "mcdonalds.com"),
                new Logo("in n out", "innout.com"),
                new Logo("burger king", "bk.com"),
                new Logo("panda express", "pandaexpress.com"),
                new Logo("starbucks", "starbucks.com"),
                new Logo("rubys", "rubys.com"),
                new Logo("subway", "subway.com"),
                new Logo("mcdonalds", "mcdonalds.com"),
                new Logo("in n out", "innout.com"),
                new Logo("burger king", "bk.com"),
                new Logo("panda express", "pandaexpress.com"),
                new Logo("starbucks", "starbucks.com"),
                new Logo("rubys", "rubys.com"),
                new Logo("subway", "subway.com"),
                new Logo("mcdonalds", "mcdonalds.com"),
                new Logo("in n out", "innout.com"),
                new Logo("burger king", "bk.com"),
                new Logo("panda express", "pandaexpress.com"),
                new Logo("starbucks", "starbucks.com"),
                new Logo("rubys", "rubys.com"),
                new Logo("subway", "subway.com"),
                new Logo("mcdonalds", "mcdonalds.com"),
                new Logo("in n out", "innout.com"),
                new Logo("burger king", "bk.com"),
                new Logo("panda express", "pandaexpress.com"),
                new Logo("starbucks", "starbucks.com"),
                new Logo("rubys", "rubys.com"),
                new Logo("subway", "subway.com"),
                new Logo("mcdonalds", "mcdonalds.com"),
                new Logo("in n out", "innout.com"),
                new Logo("burger king", "bk.com"),
                new Logo("panda express", "pandaexpress.com"),
                new Logo("starbucks", "starbucks.com"),
                new Logo("rubys", "rubys.com"),
                new Logo("subway", "subway.com")
        ));
    }
}
