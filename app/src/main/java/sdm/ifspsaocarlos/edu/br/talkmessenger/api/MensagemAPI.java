package sdm.ifspsaocarlos.edu.br.talkmessenger.api;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Mensagem;

public interface MensagemAPI {

    @POST("mensagem")
    Call<ResponseBody> create(@Body RequestBody mensagem);

    @GET("rawmensagens/{idMensagem}/{idRemetente}/{idDestinatario}")
    Call<List<Mensagem>> getMensagens(@Path("idMensagem") String idMensagem,
                                      @Path("idRemetente") String idRemetente,
                                      @Path("idDestinatario") String idDestinatario);
}
