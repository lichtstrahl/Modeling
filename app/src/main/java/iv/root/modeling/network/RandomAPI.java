package iv.root.modeling.network;

import io.reactivex.Single;
import iv.root.modeling.network.body.IntegerBody;
import iv.root.modeling.network.dto.ResponseRandomDTO;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RandomAPI {
    @POST("/json-rpc/1/invoke")
    Single<ResponseRandomDTO> randomInteger(@Body IntegerBody body);

}
