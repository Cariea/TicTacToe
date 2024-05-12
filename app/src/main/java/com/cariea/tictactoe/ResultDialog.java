package com.cariea.tictactoe;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultDialog extends Dialog {

    private final String message;
    private final MainActivity mainActivity;

    public ResultDialog(@NonNull Context context, String message, MainActivity mainActivity) {
        super(context);
        this.message = message;
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_dialog);

        TextView messageText = findViewById(R.id.messageText);
        Button startAgainButton = findViewById(R.id.startAgainButton);

        messageText.setText(message);

        startAgainButton.setOnClickListener(view -> {
            MainActivity.playerTurn = 1;
            mainActivity.restartMatch(MainActivity.currentPlayer);
            dismiss();
        });
    }
}