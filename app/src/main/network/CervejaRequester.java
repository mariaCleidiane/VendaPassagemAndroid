package br.usjt.cervejap4.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.usjt.cervejap4.model.Cerveja;

/**
 * Created by asbonato on 2/6/15.
 * Vai fazer a requisição na API REST
 */
public class PassagemAerea {

    OkHttpClient client = new OkHttpClient();

    public ArrayList<Cerveja> get(String url, String destino, String pCor, String pPais) throws IOException {

        ArrayList<Cerveja> lista = new ArayList<>();

        //acentuacao nao funciona se mandar via get, mesmo usando URLEncode.encode(String,UTF-8)
        RequestBody formBody = new FormEncodingBuilder()
                .add("estilo", pEstilo)
                .add("cor", pCor)
                .add("pais", pPais)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        String jsonStr = response.body().string();

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));

        try {
            JSONArray root = new JSONArray(jsonStr);
            JSONObject item = null;
            for (int i = 0; i < root.length(); i++ ) {
                item = (JSONObject)root.get(i);

                String nome = item.getString("nome");
                String imagem = item.getString("imagem");
                double preco = item.getDouble("preco");
                String estilo = item.getString("estilo");
                String cor = item.getString("cor");
                String pais = item.getString("pais");

                lista.add(new Cerveja(nome, estilo, cor, pais, imagem, preco));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        finally {
            if(lista.size() == 0)
                lista.add(new Cerveja(Cerveja.NAO_ENCONTRADA, pEstilo, pCor, pPais, "garrafa_vazia", 0.0));
            //Log.v("CervejaRequester", jsonStr);
        }
        return lista;
    }
    public Bitmap getImage(String url) throws IOException {

        Bitmap img = null;

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        InputStream is = response.body().byteStream();

        img = BitmapFactory.decodeStream(is);

        is.close();

        return img;
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}
