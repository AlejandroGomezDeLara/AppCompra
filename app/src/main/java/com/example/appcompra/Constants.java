package com.example.appcompra;

public class Constants {
    //Constantes para la conexion
    public static final String IP_SERVER="192.168.1.136";
    public static final int PORT=7777;

    //Caracteres para el servidor
    public static final String SEPARATOR=";";

    //Login
    public static final String LOGIN_PETICION ="L";
    public static final String LOGIN_RESPUESTA_CORRECTA="LC";
    public static final String LOGIN_RESPUESTA_FALLIDA="LF";

    //Registro
    public static final String REGISTER_PETICION ="R";
    public static final String REGISTER_RESPUESTA_CORRECTA="RC";
    public static final String REGISTER_RESPUESTA_FALLIDA="RF";

    //Pedir productos de una categoria
    public static final String PRODUCTOS_CATEGORIA_PETICION="PC";
    public static final String PRODUCTOS_CATEGORIA_RESPUESTA_CORRECTA="PCC";
    public static final String PRODUCTOS_CATEGORIA_RESPUESTA_FALLIDA="PCF";

    //Pedir categorias
    public static final String CATEGORIAS_PETICION="C";
    public static final String CATEGORIAS_RESPUESTA_CORRECTA="CC";
    public static final String CATEGORIAS_RESPUESTA_FALLIDA="CF";

    //Pedir listas
    public static final String LISTAS_PETICION="LL";
    public static final String LISTAS_RESPUESTA_CORRECTA ="LLC";
    public static final String LISTAS_RESPUESTA_FALLIDA ="LLF";

    //Crear nuevas listas
    public static final String CREACION_NUEVA_LISTA="CL";
    public static final String CREACION_NUEVA_LISTA_CORRECTA="CLC";
    public static final String CREACION_NUEVA_LISTA_FALLIDA="CLF";

    //Borrar lista
    public static final String BORRAR_LISTA_PETICION="SL";

    //Productos de una lista
    public static final String PRODUCTOS_LISTA_PETICION="PPL";
    public static final String PRODUCTOS_LISTA_CORRECTA="PPLC";
    public static final String PRODUCTOS_LISTA_FALLIDA="PPLF";

    //Productos de la despensa
    public static final String PRODUCTOS_DESPENSA_PETICION="DP";
    public static final String PRODUCTOS_DESPENSA_CORRECTA="DC";
    public static final String PRODUCTOS_DESPENSA_FALLIDA="DF";

    //Editar listas
    public static final String EDITAR_LISTAS="EL";
    // EL;ID;IDLISTA;{tp:[
    //                  {id,remove},
    //                  {id,add},
    //                  {id,mark}
    //                ],
    //                pc:[
    //                  {id,remove},
    //                  {id,add},
    //                  {id,mark}
    //                ]
    //               }


    //tests

    public static final String DUMMY_LOGIN="LC;2;nombre";
    public static final String DUMMY_PRODUCTOS_CATEGORIA="LC;2;nombre";
    public static final String DUMMY_CATEGORIAS="LC;2;nombre";
    public static final String DUMMY_LISTAS="LC;2;nombre";
    public static final String DUMMY_="LC;2;nombre";

}
