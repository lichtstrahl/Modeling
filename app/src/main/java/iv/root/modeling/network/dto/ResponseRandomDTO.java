package iv.root.modeling.network.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseRandomDTO {
    @SerializedName("jsonrpc")
    String verJSON;
    @SerializedName("id")
    int id;
    @SerializedName("result")
    ResultDTO result;


    public String getVerJSON() {
        return verJSON;
    }

    public int getId() {
        return id;
    }

    public ResultDTO getResult() {
        return result;
    }
}
