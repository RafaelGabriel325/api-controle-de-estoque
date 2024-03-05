package br.com.controleestoque.shared.constant;

public class PathsConstants {
    // AUTH
    public static final String AUTH_BASE = "/api/auth";
    public static final String AUTH_SIGN_IN = "/sign-in";
    public static final String AUTH_REFRESH_TOKEN = "/refresh-token/{username}";
    public static final String AUTH_BY_ID = "/user/{id}";
    public static final String AUTH_ALL = "/user/";
    public static final String AUTH_CREATE = "/user/create";
    public static final String AUTH_UPDATE = "/user/update/{id}";
    public static final String AUTH_DELETE = "/user/delete/{id}";
    public static final String AUTH_PERMISSION_BY_ID = "/permission/{id}";
    public static final String AUTH_PERMISSION_ALL = "/permission/";
    public static final String AUTH_PERMISSION_CREATE = "/permission/create";
    public static final String AUTH_PERMISSION_UPDATE = "/permission/update/{id}";
    public static final String AUTH_PERMISSION_DELETE = "/permission/delete/{id}";


    // PESSOA
    public static final String PESSOA_BASE = "/api/pessoa";
    public static final String PESSOA_BY_ID = "/{id}";
    public static final String PESSOA_ALL = "/";
    public static final String PESSOA_CREATE = "/create";
    public static final String PESSOA_UPDATE = "/update/{id}";
    public static final String PESSOA_DELETE = "/delete/{id}";

    // PRODUTO
    public static final String PRODUTO_BASE = "/api/produto";
    public static final String PRODUTO_BY_ID = "/{id}";
    public static final String PRODUTO_ALL = "/";
    public static final String PRODUTO_CREATE = "/create";
    public static final String PRODUTO_UPDATE = "/update/{id}";
    public static final String PRODUTO_DELETE = "/delete/{id}";

    // TIPO_PRODUTO
    public static final String TIPO_PRODUTO_BASE = "/api/tipo-produto";
    public static final String TIPO_PRODUTO_BY_ID = "/{id}";
    public static final String TIPO_PRODUTO_ALL = "/";
    public static final String TIPO_PRODUTO_CREATE = "/create";
    public static final String TIPO_PRODUTO_UPDATE = "/update/{id}";
    public static final String TIPO_PRODUTO_DELETE = "/delete/{id}";
}