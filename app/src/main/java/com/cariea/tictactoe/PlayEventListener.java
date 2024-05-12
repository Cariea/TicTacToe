package com.cariea.tictactoe;

public interface PlayEventListener {
    void onPlayEventReceived(String piece, int position,String player);

    void onWinEventReceived(String player);

    void onDrawEventReceived();
}
