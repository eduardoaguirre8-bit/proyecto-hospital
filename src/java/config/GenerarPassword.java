package config;

import org.mindrot.jbcrypt.BCrypt;

/** Ejecútala para obtener el hash BCrypt de una clave y guardarlo en la tabla usuarios. */
public class GenerarPassword {
    public static void main(String[] args) {
        String clave = args.length > 0 ? args[0] : "123456";
        System.out.println(BCrypt.hashpw(clave, BCrypt.gensalt(10)));
    }
}
