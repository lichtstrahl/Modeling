package iv.root.modeling.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseQRNS {
    @SerializedName("length")
    int length;
    @SerializedName("data")
    List<Integer> data;
    @SerializedName("type")
    String type;

    public List<Integer> getData() {
        return data;
    }

    public int getLength() {
        return length;
    }

    public String getType() {
        return type;
    }
}
