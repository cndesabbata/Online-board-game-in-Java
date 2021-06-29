package it.polimi.ingsw.messages.serverMessages;

/**
 * Enumeration ErrorType contains all the possible types of {@link ErrorMessage}.
 *
 */
public enum ErrorType {
    RECONNECTION, DUPLICATE_NICKNAME, WRONG_MESSAGE, INVALID_END_TURN,
    WRONG_ACTION, SOCKET_ERROR, PLAYER_NUMBER, SETUP_DRAW,
    SETUP_RESOURCE
}
