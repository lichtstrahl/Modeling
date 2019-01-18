package iv.root.modeling.network.dto;

import com.google.gson.annotations.SerializedName;

public class ResultDTO {
    @SerializedName("random")
    RandomDTO random;

    public RandomDTO getRandom() {
        return random;
    }
}
