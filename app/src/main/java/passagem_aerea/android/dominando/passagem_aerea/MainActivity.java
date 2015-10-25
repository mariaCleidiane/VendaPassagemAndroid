package passagem_aerea.android.dominando.passagem_aerea;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import passagem_aerea.android.dominando.passagem_aerea.Main.PassagemRequester;
import passagem_aerea.android.dominando.passagem_aerea.Main.ModelPassagem;


public class MainActivity extends AppCompatActivity {

    Spinner paises;
    Spinner estados;
    String origem, destino;
    Button ok;
    OkHttpClient client = new OkHttpClient();;
    final String servidor = "localhost:8080/JSP_ProjetoIntegrado_2.30_Final";
    ProgressBar mProgress;
    PassagemRequester requester;
    Intent intent;
    ArrayList<ModelPassagem> passagens;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();

        paises = (Spinner) findViewById(R.id.spinSO);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.paises, android.R.layout.simple_spinner_item);

        paises.setAdapter(adapter);

        estados = (Spinner) findViewById(R.id.spinSO);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.paises, android.R.layout.simple_spinner_item);

        estados.setAdapter(adapter1);

        try {
            String meuJson = getJson("http://localhost:8080/JSP_ProjetoIntegrado_2.30_Final/ControllerJSON");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJson(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void setupViews() {
        origem = "";
        destino = "";

        ok = (Button) findViewById(R.id.botao_enviar);

    }

    private class OrigemSelecionado implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            origem = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class OrigemDestino implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            destino = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public final static String ORIGEM = "br.usjt.ORIGEM";
    public final static String DESTINO = "br.usjt.DESTINO";

    public void sendMessage(View view) {

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String pOrigem = this.origem.equals("Origem: ") ? "" : origem;
        String pDestino = this.destino.equals("destino: ") ? "" : destino;
        intent.putExtra(ORIGEM, pOrigem);
        intent.putExtra(DESTINO, pDestino);


        startActivity(intent);


    }

    // constante static para identificar o parametro
    public final static String PASSAGEM = "br.usjt.PASSAGEM";
    //será chamado quando o usuário clicar em enviar
    public void consultarCervejas(View view) {
        final String pOrigem = this.origem.equals("Escolha a origem")?"":origem;
        final String pDestino = this.destino.equals("Escolha o destino")?"":destino;

        requester = new PassagemRequester();
        if(requester.isConnected(this)) {
            intent = new Intent(this, DisplayMessageActivity.class);

            mProgress.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        passagens = requester.get("http://" + servidor + "/produto.json", pOrigem, pDestino);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                intent.putExtra(PASSAGEM, passagens);
                                mProgress.setVisibility(View.INVISIBLE);
                                startActivity(intent);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Toast toast = Toast.makeText(this, "Rede indisponível!", Toast.LENGTH_LONG);
            toast.show();
        }
    }


}
