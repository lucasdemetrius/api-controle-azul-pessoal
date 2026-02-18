package com.controleazulpessoal.finance_api.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

    public static boolean verificarSenha(String senha, String hashSenha) {
        return BCrypt.checkpw(senha, hashSenha);
    }

}
