package com.bignerdranch.android.adam_chess_app;

public class InvalidChessMoveException extends Exception {

    public InvalidChessMoveException() {

    }

    public InvalidChessMoveException(String ivcmexc) {
        super(ivcmexc);
    }
}
