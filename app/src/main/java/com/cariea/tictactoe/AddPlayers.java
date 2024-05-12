package com.cariea.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import java.util.UUID;

public class AddPlayers extends AppCompatActivity {

    public static AddPlayers instance;
    public static String playerOneName;
    public static String IP = ":52301";

    private OkHttpClient client;

    public static WebSocket ws;

    public static String playerTwoName;

    public static String currentPlayer;

    private PlayEventListener playEventListener;
    public static AddPlayers getInstance() {
        return instance;
    }
    public void setPlayEventListener(PlayEventListener listener) {
        this.playEventListener = listener;
    }
    void initWebSocket(String playerOneName){
        client = new OkHttpClient();
        String clientUUID = UUID.randomUUID().toString();
        Request request = new Request.Builder().url("ws://"+IP+"/ws")
                .header("id", clientUUID).header("name", playerOneName.toString())
                .build();
        currentPlayer = clientUUID;
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);

            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);

                String event = text.substring(0, 4);



                runOnUiThread(() -> {
                    if(event.equals("ping")) {
                        Toast.makeText(getApplicationContext(), text.substring(4,text.length()), Toast.LENGTH_SHORT).show();
                    }
                    if(event.equals("init")){
                        Toast.makeText(getApplicationContext(), "Game is starting", Toast.LENGTH_SHORT).show();
                        Integer playerOneNameLength = Integer.parseInt(text.substring(4, 5));
                        currentPlayer = "playerTwo";
                        MainActivity.playerTurn = 1;
                        Intent intent = new Intent(AddPlayers.this, MainActivity.class);
                        intent.putExtra("currentPlayer", currentPlayer);
                        intent.putExtra("playerOne", text.substring(5, 5 + playerOneNameLength));
                        intent.putExtra("playerTwo",playerOneName);

                        startActivity(intent);
                    }
                    if (event.equals("play")) {

                        String piece = text.substring(4,5);
                        String position = text.substring(5,6);
                        String player = text.substring(6,text.length());
                        Integer positionInt = Integer.parseInt(position);
                        if (playEventListener != null) {
                            playEventListener.onPlayEventReceived(piece, positionInt,player);
                        }
                    }

                    if(event.equals("winn")){
                        String player = text.substring(4,5);

                        if (playEventListener != null) {
                            playEventListener.onWinEventReceived(player);
                        }
                    }

                    if(event.equals("draw")){
                        if (playEventListener != null) {
                            playEventListener.onDrawEventReceived();
                        }
                    }


                });
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                // Server is closing the connection
                webSocket.close(1000, null);
            }
        };

        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);

        instance = this;
        EditText playerOneEditText = findViewById(R.id.playerOne);
        Button startGameButton = findViewById(R.id.startGameButton);


        startGameButton.setOnClickListener(view -> {
            String getPlayerOneName = playerOneEditText.getText().toString();
            if (getPlayerOneName.isEmpty()) {
                Toast.makeText(AddPlayers.this, "Por favor ingresa el nombre del jugador", Toast.LENGTH_SHORT).show();
            } else {
                playerOneName = getPlayerOneName;
                initWebSocket(playerOneName);
                try {

                    Intent intent = new Intent(AddPlayers.this, ActivePlayers.class);
                    intent.putExtra("playerOne", playerOneName);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(AddPlayers.this, "Error al conectarse al servidor", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
