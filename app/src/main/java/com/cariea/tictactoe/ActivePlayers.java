package com.cariea.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStreamReader;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class ActivePlayers extends AppCompatActivity {


    public ActivePlayers() {

    }

    public interface PlayerDataListener {
        void onPlayerDataReceived(List<Player> players);
    }

    interface GameInitializedListener {
        void onGameInitialized();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_players);

        LinearLayout playerListLayout = findViewById(R.id.playerListLayout);

        obtenerJugadoresActivosDesdeServidor(new PlayerDataListener() {
            @Override
            public void onPlayerDataReceived(List<Player> activePlayers) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivePlayers.this, "Active Players: " + activePlayers.size(), Toast.LENGTH_SHORT).show();

                        for (Player player : activePlayers) {
                            if (!player.getId().equals(AddPlayers.currentPlayer)) {


                                Button button = new Button(ActivePlayers.this);
                                button.setText(player.getName());
                                button.setTag(player.getId());
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        initGame(AddPlayers.currentPlayer, player, new GameInitializedListener() {

                                            @Override
                                            public void onGameInitialized() {
                                                Intent intent = new Intent(ActivePlayers.this, MainActivity.class);
                                                intent.putExtra("playerOne", AddPlayers.playerOneName);
                                                intent.putExtra("playerTwo", player.getName());
                                                intent.putExtra("currentPlayer", "playerOne");
                                                MainActivity.playerTurn = 1;
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                });
                                playerListLayout.addView(button);
                            }
                        }
                    }
                });
            }
        });
    }

    private static List<Player> obtenerJugadoresActivosDesdeServidor(final PlayerDataListener listener) {
        final List<Player> activePlayers = new ArrayList<>(); // Declarar activePlayers como final

        new AsyncTask<Void, Void, List<Player>>() {
            @Override
            protected List<Player> doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://"+AddPlayers.IP+"/players");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    List<Player> parsedPlayers = parsearJSON(response.toString());
                    activePlayers.addAll(parsedPlayers); // Agregar los jugadores obtenidos a activePlayers

                    // Cerrar la conexión
                    connection.disconnect();
                    listener.onPlayerDataReceived(activePlayers);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return activePlayers;
            }
        }.execute();
        return activePlayers;
    }

    private static List<Player> parsearJSON(String jsonString) {
        List<Player> players = new ArrayList<>();

        try {
            // Convertir la cadena JSON a un array de objetos JSON
            JSONArray jsonArray = new JSONArray(jsonString);
            // Iterar sobre cada objeto JSON en el array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPlayer = jsonArray.getJSONObject(i);

                // Obtener los valores de cada campo del jugador del objeto JSON
                String id = jsonPlayer.getString("ID");
                String name = jsonPlayer.getString("Name");

                Player player = new Player(id, name);
                players.add(player);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return players;
    }

    private static void initGame(String playerOneId, Player player, final GameInitializedListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://"+AddPlayers.IP+"/game");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    Log.d("ActivePlayers", "playerOneId: " + playerOneId);
                    Log.d("ActivePlayers", "playerTwoId: " + player.getId());
                    connection.setRequestProperty("playerOneId", playerOneId);
                    connection.setRequestProperty("playerTwoId", player.getId());
                    connection.setRequestProperty("playerOneName", AddPlayers.playerOneName);
                    connection.setRequestProperty("playerTwoName", player.getName());

                    connection.setRequestMethod("GET");


                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("ActivePlayers", response.toString());
                    reader.close();

                    // Notificar al listener que el juego se ha inicializado
                    listener.onGameInitialized();

                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return null;
            }
        }.execute();

    }

}