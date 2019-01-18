package iv.root.modeling.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RandomDTO {
    @SerializedName("data")
    List<Integer> data;

    public List<Integer> getData() {
        return data;
    }
}
