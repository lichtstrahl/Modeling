package iv.root.modeling.network.body;

import com.google.gson.annotations.SerializedName;

import iv.root.modeling.BuildConfig;

public class IntegerParams {
    @SerializedName("apiKey")
    String api_key;
    @SerializedName("n")
    int count;
    @SerializedName("min")
    int min;
    @SerializedName("max")
    int max;
    @SerializedName("base")
    int base;

    public IntegerParams(int count, int min, int max) {
        this.api_key = BuildConfig.API_KEY;
        this.count = count;
        this.min = min;
        this.max = max;
        this.base = 10;
    }

    public String getApi_key() {
        return api_key;
    }

    public int getCount() {
        return count;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getBase() {
        return base;
    }
}

/*
"apiKey" : "1eb4c8e8-1b3a-4b25-ba00-e1886c51b697",
    "n":5,
    "min":0,
    "max":9,
    "base":10
 */