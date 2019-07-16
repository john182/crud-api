package com.crud.user.util;

public class UtilErros {

    public static String getMessageError(Exception e) {
        while (e.getCause() != null) {
            e = (Exception) e.getCause();
        }
        String retorno = e.getMessage();
        return retorno;
    }

    public static String getMessageError(Throwable e) {
        while (e.getCause() != null) e = e.getCause();
        String retorno = e.getMessage();
        return retorno;
    }
}
