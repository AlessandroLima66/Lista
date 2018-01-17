package br.com.alessandrolima.lista;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.alessandrolima.lista.adapter.VersoesAdapter;
import br.com.alessandrolima.lista.model.Android;


/**
 * A simple {@link Fragment} subclass.
 */
public class VersoesAndroidFragment extends Fragment {


    public VersoesAndroidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_versoes_android, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new BuscarDados().execute("http://www.mocky.io/v2/58af1fb21000001e1cc94547");



    }

    private class BuscarDados extends AsyncTask<String, Void, List<Android>> {
        ProgressDialog dialog = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Carregando...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected List<Android> doInBackground(String... params) {
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            try {
                // Setup HttpURLConnection class to send and receive data
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Realiza a leitura dos dados do servidor
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    List<Android> data = new ArrayList<>();

                    try {

                        JSONObject json = new JSONObject(result.toString());
                        JSONArray jArray = json.getJSONArray("android");

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jsonData = jArray.getJSONObject(i);
                            Android android = new Android();
                            android.setApi(jsonData.getString("api"));
                            android.setNome(jsonData.getString("nome"));
                            android.setUrlImagem(jsonData.getString("urlImagem"));
                            android.setVersao(jsonData.getString("versao"));
                            data.add(android);
                        }
                    } catch (JSONException e) {
                        return null;
                    }
                    return data;

                } else {

                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(List<Android> s) {
            super.onPostExecute(s);

            setUpRecyclerView(s);

            dialog.dismiss();
        }

    }

    private void setUpRecyclerView(List<Android> s) {
        RecyclerView rvVersoes = getView().findViewById(R.id.rvVersoes);
        VersoesAdapter adapter = new VersoesAdapter(getContext(), s);
        rvVersoes.setAdapter(adapter);
        rvVersoes.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
