package com.example.skadi_beckend.util;

import java.util.regex.Pattern;

public class Validator {

    // Definição de variaveis pattern/validator

    private static final Pattern TELEFONE_VALIDATOR =

            Pattern.compile("^(\\+?55\\s?)?(\\(?\\d{2}\\)?\\s?)?(9?\\d{4}[-.\\s]?\\d{4})$");

    private static final Pattern EMAIL_VALIDATOR =

            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern CPF_VALIDATOR =

            Pattern.compile("^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");

    private static final Pattern CNPJ_VALIDATOR =

            Pattern.compile("^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$");

    private static final Pattern CPF_FORMAT_VALIDATOR =

            Pattern.compile("^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");

    private static final Pattern CNPJ_FORMAT_VALIDATOR =

            Pattern.compile("^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$");


    private static final Pattern SENHA_VALIDATOR =

            Pattern.compile("^(?=.*[0-9])(?=.*[A-Z]).{8,}$");

    public static boolean validarTelefone(String telefone) {

        return telefone != null && TELEFONE_VALIDATOR.matcher(telefone).matches();

    }

    public static boolean validarEmail(String email) {

        return email != null && EMAIL_VALIDATOR.matcher(email).matches();

    }

    public static boolean validarFormatoCpf(String cpf) {

        return cpf != null && CPF_VALIDATOR.matcher(cpf).matches();

    }

    public static boolean validarFormatoCnpj(String cnpj) {

        return cnpj != null && CNPJ_VALIDATOR.matcher(cnpj).matches();

    }


    public static boolean validarSenha(String senha) {

        return senha != null && SENHA_VALIDATOR.matcher(senha).matches();

    }

}
