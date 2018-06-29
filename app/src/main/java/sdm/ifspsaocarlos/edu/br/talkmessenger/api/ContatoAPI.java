package sdm.ifspsaocarlos.edu.br.talkmessenger.api;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;

public interface ContatoAPI {
    @POST("contato")
    Call<ResponseBody> create(@Body RequestBody contato);

    @GET("rawcontatos")
    Call<List<Contato>> getContatos();
}
