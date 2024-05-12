package com.cariea.tictactoe;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.cariea.tictactoe.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class MainActivity extends AppCompatActivity implements PlayEventListener{

    ActivityMainBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    public static int[] boxPositions = {0,0,0,0,0,0,0,0,0}; //9 zero
    public static int playerTurn;
    private int totalSelectedBoxes = 1;

    public static String currentPlayer = "playerOne";
    @Override
    public void onPlayEventReceived(String piece, int position,String player) {

        // Obtener la imagen correspondiente según el jugador actual
        int imageResource = (playerTurn == 1) ? R.drawable.ximage : R.drawable.oimage;


        // Actualizar la posición del tablero con el turno del jugador actual
        boxPositions[position] = playerTurn;
        totalSelectedBoxes++;
        boolean fuel = true;
        for(int i = 0; i < boxPositions.length; i++){
            if(boxPositions[i] == 0){
                fuel = false;
                break;
            }
        }

        if(fuel){
            onDrawEventReceived();
        }
        Log.d("MainActivity",boxPositions.toString());
        // Actualizar la imagen en el ImageView correspondiente a la posición del tablero
        ImageView imageView;
        switch (position) {
            case 0:
                imageView = binding.image1;
                break;
            case 1:
                imageView = binding.image2;
                break;
            case 2:
                imageView = binding.image3;
                break;
            case 3:
                imageView = binding.image4;
                break;
            case 4:
                imageView = binding.image5;
                break;
            case 5:
                imageView = binding.image6;
                break;
            case 6:
                imageView = binding.image7;
                break;
            case 7:
                imageView = binding.image8;
                break;
            case 8:
                imageView = binding.image9;
                break;
            default:
                return;
        }
        imageView.setImageResource(imageResource);
        setBoardEnabled(true);

        // Cambiar al turno del otro jugador para el próximo movimiento
        changePlayerTurn(playerTurn == 1 ? 2 : 1);
    }

    public void onWinEventReceived(String player){

        ResultDialog resultDialog;
        if(player.equals("1")){
            resultDialog = new ResultDialog(MainActivity.this, binding.playerOneName.getText().toString()
                    + " is a Winner!", MainActivity.this);

        }else{
            resultDialog = new ResultDialog(MainActivity.this, binding.playerTwoName.getText().toString()
                    + " is a Winner!", MainActivity.this);
        }
        resultDialog.setCancelable(false);
        resultDialog.show();

    }

    public void onDrawEventReceived(){
        ResultDialog resultDialog = new ResultDialog(MainActivity.this, "Match Draw", MainActivity.this);
        resultDialog.setCancelable(false);
        resultDialog.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AddPlayers addPlayers = AddPlayers.getInstance();
        addPlayers.setPlayEventListener(this);

        combinationList.add(new int[] {0,1,2});
        combinationList.add(new int[] {3,4,5});
        combinationList.add(new int[] {6,7,8});
        combinationList.add(new int[] {0,3,6});
        combinationList.add(new int[] {1,4,7});
        combinationList.add(new int[] {2,5,8});
        combinationList.add(new int[] {2,4,6});
        combinationList.add(new int[] {0,4,8});

        String getPlayerOneName = getIntent().getStringExtra("playerOne");
        String getPlayerTwoName = getIntent().getStringExtra("playerTwo");
        String getCurrentPlayer = getIntent().getStringExtra("currentPlayer");

        if (getCurrentPlayer.equals("playerTwo")) {
            currentPlayer = "playerTwo";
            setBoardEnabled(false);
        }

        binding.playerOneName.setText(getPlayerOneName);
        binding.playerTwoName.setText(getPlayerTwoName);


        binding.image1.setOnClickListener(view -> {
            if (isBoxSelectable(0)){
                Log.d("MainActivity", "Box 1 is selectable");
                try {
                    performAction((ImageView) view, 0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        binding.image2.setOnClickListener(view -> {
            if (isBoxSelectable(1)){
                Log.d("MainActivity", "Box 7 is selectable");
                try {
                    performAction((ImageView) view, 1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.image3.setOnClickListener(view -> {
            if (isBoxSelectable(2)){
                Log.d("MainActivity", "Box 7 is selectable");
                try {
                    performAction((ImageView) view, 2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.image4.setOnClickListener(view -> {
            if (isBoxSelectable(3)){
                Log.d("MainActivity", "Box 7 is selectable");
                try {
                    performAction((ImageView) view, 3);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.image5.setOnClickListener(view -> {
            if (isBoxSelectable(4)){
                Log.d("MainActivity", "Box 7 is selectable");
                try {
                    performAction((ImageView) view, 4);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.image6.setOnClickListener(view -> {
            if (isBoxSelectable(5)){
                Log.d("MainActivity", "Box 7 is selectable");
                try {
                    performAction((ImageView) view, 5);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.image7.setOnClickListener(view -> {
            if (isBoxSelectable(6)){
                Log.d("MainActivity", "Box 7 is selectable");
                try {
                    performAction((ImageView) view, 6);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.image8.setOnClickListener(view -> {
            if (isBoxSelectable(7)){
                Log.d("MainActivity", "Box 7 is selectable");
                try {
                    performAction((ImageView) view, 7);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.image9.setOnClickListener(view -> {
            if (isBoxSelectable(8)){
                Log.d("MainActivity", "Box 7 is selectable");
                try {
                    performAction((ImageView) view, 8);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


    private void setBoardEnabled(boolean enabled) {
        binding.image1.setEnabled(enabled);
        binding.image2.setEnabled(enabled);
        binding.image3.setEnabled(enabled);
        binding.image4.setEnabled(enabled);
        binding.image5.setEnabled(enabled);
        binding.image6.setEnabled(enabled);
        binding.image7.setEnabled(enabled);
        binding.image8.setEnabled(enabled);
        binding.image9.setEnabled(enabled);
    }

    private void performAction(ImageView  imageView, int selectedBoxPosition) throws IOException {




        boxPositions[selectedBoxPosition] = playerTurn;
        Log.d("MainActivity",boxPositions.toString());

        if (playerTurn == 1) {

            imageView.setImageResource(R.drawable.ximage);

            AddPlayers.ws.send("playX"+selectedBoxPosition+"playerOne");
            setBoardEnabled(false);
            if (checkResults()) {
                ResultDialog resultDialog = new ResultDialog(MainActivity.this, binding.playerOneName.getText().toString()
                        + " is a Winner!", MainActivity.this);

                AddPlayers.ws.send("winn1");
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else if(totalSelectedBoxes == 9) {
                ResultDialog resultDialog = new ResultDialog(MainActivity.this, "Match Draw", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        } else {
            imageView.setImageResource(R.drawable.oimage);

            AddPlayers.ws.send("playO"+selectedBoxPosition+"playerTwo");
            setBoardEnabled(false);
            if (checkResults()) {
                ResultDialog resultDialog = new ResultDialog(MainActivity.this, binding.playerTwoName.getText().toString()
                        + " is a Winner!", MainActivity.this);
                AddPlayers.ws.send("winn2");
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else if(totalSelectedBoxes == 9) {
                ResultDialog resultDialog = new ResultDialog(MainActivity.this, "Match Draw", MainActivity.this);
                AddPlayers.ws.send("draw");
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }


    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == 1) {
            binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerTwoLayout.setBackgroundResource(R.drawable.white_box);
        } else {
            binding.playerTwoLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerOneLayout.setBackgroundResource(R.drawable.white_box);
        }
    }

    private boolean checkResults(){
        boolean response = false;
        for (int i = 0; i < combinationList.size(); i++){
            final int[] combination = combinationList.get(i);

            if (boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                response = true;
            }
        }
        return response;
    }

    private boolean isBoxSelectable(int boxPosition) {
        return boxPositions[boxPosition] == 0;
    }

    public void restartMatch(String currentPlayer){
        boxPositions = new int[] {0,0,0,0,0,0,0,0,0}; //9 zero
        playerTurn = 1;
        totalSelectedBoxes = 1;

        binding.image1.setImageResource(R.drawable.white_box);
        binding.image2.setImageResource(R.drawable.white_box);
        binding.image3.setImageResource(R.drawable.white_box);
        binding.image4.setImageResource(R.drawable.white_box);
        binding.image5.setImageResource(R.drawable.white_box);
        binding.image6.setImageResource(R.drawable.white_box);
        binding.image7.setImageResource(R.drawable.white_box);
        binding.image8.setImageResource(R.drawable.white_box);
        binding.image9.setImageResource(R.drawable.white_box);

        binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
        binding.playerTwoLayout.setBackgroundResource(R.drawable.white_box);

        if (currentPlayer.equals("playerTwo")) {
            setBoardEnabled(false);
        } else {
            setBoardEnabled(true);
        }

    }



}