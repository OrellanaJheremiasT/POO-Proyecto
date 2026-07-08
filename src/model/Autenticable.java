package model;

/**
 * Interface que define el comportamiento de autenticación.
 * Cualquier entidad que requiera login debe implementar
 * autenticar(), getPasswordHash() y estaActivo().
 */


public interface Autenticable {
    boolean autenticar(String password);
    String getPasswordHash();
    boolean estaActivo();
}
