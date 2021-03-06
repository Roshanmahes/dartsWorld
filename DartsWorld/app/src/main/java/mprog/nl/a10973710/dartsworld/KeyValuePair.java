/*
 * Created by Roshan Mahes on 12-6-2017.
 */

package mprog.nl.a10973710.dartsworld;

/**
 * Default class with properties.
 */

class KeyValuePair {
    private String key;
    private String value;

    KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getId() { return key; }

    public void setId(String id) { this.key = id; }

    String getValue() { return value; }
}