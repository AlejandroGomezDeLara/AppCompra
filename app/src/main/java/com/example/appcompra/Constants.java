package com.example.appcompra;

import com.example.appcompra.utils.Peticion;

public class Constants {
    //Constantes para la conexion
    public static final int PORT=7777;
    public static final int PORTSERVICE = 7776;

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

    //Pedir categorias recetas
    public static final String CATEGORIAS_RECETAS_PETICION="CR";
    public static final String CATEGORIAS_RECETAS_CORRECTA="CRC";
    public static final String CATEGORIAS_RECETAS_FALLIDA="CRF";

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
    public static final String PRODUCTOS_DESPENSA_CORRECTA="DPC";
    public static final String PRODUCTOS_DESPENSA_FALLIDA="DF";

    //Compartir listas
    public static final String COMPARTIR_LISTA_PETICION="AL";
    public static final String COMPARTIR_LISTA_CORRECTA="ALC";
    public static final String COMPARTIR_LISTA_FALLIDA="ALF";

    //Vincular listas
    public static final String VINCULAR_LISTA_PETICION="VD";
    public static final String VINCULAR_LISTA_CORRECTA="VDC";

    //Pedir ofertas
    public static final String PEDIR_OFERTAS_PETICION="PO";
    public static final String PEDIR_OFERTAS_CORRECTA="POC";

    //Pedir Recomendaciones

    public static final String RECOMENDACIONES_PETICION="RP";
    public static final String RECOMENDACIONES_CORECTA="RPC";

    //Enviar Notificaciones
    public static final String ENVIAR_NOTIFICACIONES="N";



    //tests

    public static final String DUMMY_LOGIN="LC;2;nombre";
    public static final String DUMMY_PRODUCTOS_CATEGORIA_BODEGA="{\"productos\":[{\"id\": \"3\", \"nombre\" : \"coctel\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/38/38706.png\" },{\"id\": \"7\", \"nombre\" : \"cerveza\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/110/110181.png\" },{\"id\": \"56\", \"nombre\" : \"alcohol\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/99/99955.png\" },{\"id\": \"99\", \"nombre\" : \"biberon\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/95/95402.png\" },{\"id\": \"104\", \"nombre\" : \"copa_coctel\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/8/8066.png\" },{\"id\": \"120\", \"nombre\" : \"barril\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/123/123896.png\" },{\"id\": \"124\", \"nombre\" : \"vino\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/120/120241.png\" },{\"id\": \"146\", \"nombre\" : \"champan\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/121/121013.png\" },{\"id\": \"148\", \"nombre\" : \"whisky\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/101/101698.png\" },{\"id\": \"152\", \"nombre\" : \"tequila\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/119/119407.png\" },{\"id\": \"167\", \"nombre\" : \"sidra\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/94/94246.png\" },{\"id\": \"176\", \"nombre\" : \"chupito\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/110/110029.png\" },{\"id\": \"251\", \"nombre\" : \"botella\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/116/116620.png\" },{\"id\": \"283\", \"nombre\" : \"orujo\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/94/94258.png\" }]}";
    public static final String DUMMY_CATEGORIAS="{\"categorias\":[{\"id\": \"1\", \"nombre\" : \"Aceite, especias y salsas\" },{\"id\": \"2\", \"nombre\" : \"Agua y refrescos\" },{\"id\": \"3\", \"nombre\" : \"Aperitivos\" },{\"id\": \"4\", \"nombre\" : \"Arroz, legumbres y pasta\" },{\"id\": \"5\", \"nombre\" : \"Azúcar, caramelos y chocolate\" },{\"id\": \"6\", \"nombre\" : \"Bebé\" },{\"id\": \"7\", \"nombre\" : \"Bodega\" },{\"id\": \"8\", \"nombre\" : \"Cacao, café e infusiones\" },{\"id\": \"9\", \"nombre\" : \"Carne\" },{\"id\": \"10\", \"nombre\" : \"Cereales y galletas\" },{\"id\": \"11\", \"nombre\" : \"Charcutería y quesos\" },{\"id\": \"12\", \"nombre\" : \"Congelados\" },{\"id\": \"13\", \"nombre\" : \"Conservas, caldos y cremas\" },{\"id\": \"14\", \"nombre\" : \"Cuidado del cabello\" },{\"id\": \"15\", \"nombre\" : \"Fitoteria y parafarma\" },{\"id\": \"16\", \"nombre\" : \"Fruta y verdura\" },{\"id\": \"17\", \"nombre\" : \"Huevos,leche y mantequilla\" },{\"id\": \"18\", \"nombre\" : \"Limpieza y hogar\" },{\"id\": \"19\", \"nombre\" : \"Maquillaje\" },{\"id\": \"20\", \"nombre\" : \"Marisco y pescado\" },{\"id\": \"21\", \"nombre\" : \"Mascotas\" },{\"id\": \"22\", \"nombre\" : \"Panadería y pastelería\" },{\"id\": \"23\", \"nombre\" : \"Pizzas y platos preparados\" },{\"id\": \"24\", \"nombre\" : \"Postres y yogures\" },{\"id\": \"25\", \"nombre\" : \"Zumos\" }]}";
    public static final String DUMMY_LISTAS="{\"listas\":[{\"id\":\"1\",\"rol\":\"Participante\", \"nombre\" : \"Cumpleaños Manuel\", \"usuarios\":[{\"nombre\":\"Miguel97\",\"rol\":Administrador},{\"nombre\":\"Samuel\",\"rol\":Espectador},{\"nombre\":\"Lara67\",\"rol\":Participante},{\"nombre\":\"Albertito\",\"rol\":Participante},{\"nombre\":\"Adrianit0\",\"rol\":Participante}],\"url\":\"https://cdn.mos.cms.futurecdn.net/CRkvVQz7XZVMvRkwHGd5DM-768-80.jpg \"},{\"id\":\"3\",\"rol\":\"Espectador\", \"nombre\" : \"Compra del mes\", \"usuarios\":[{\"nombre\":\"Manuelito\",\"rol\":Administrador},{\"nombre\":\"Pedro\",\"rol\":Espectador},{\"nombre\":\"Yasuo\",\"rol\":Participante}],\"url\":\"https://cdn.mos.cms.futurecdn.net/CRkvVQz7XZVMvRkwHGd5DM-768-80.jpg \"}]}";
    public static final String DUMMY_PRODUCTO_LISTA_1="{\"tipos\":[{ \"id\":18, \"nombre\" : \"cafe\" , \"unidades\" : \"999\" , \"marca\" : \"null\", \"cadena\" : \"mercadona\" , \"comprado\" : \"true\" , \"receta\": \"null\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/128/128985.png\", \"cantidad\": \"null\" }],\"comerciales\":[{ \"id\":1, \"nombre\" : \"Estrella Galicia Especial\" , \"unidades\" : \"3\" , \"marca\" : \"Estrella Galicia\", \"cadena\" : \"mercadona\" , \"comprado\" : \"false\" , \"receta\": \"null\", \"urlimagen\": \"null\", \"cantidad\": \"330ml\" },{ \"id\":2, \"nombre\" : \"Salchichas Frankfurt\" , \"unidades\" : \"1\" , \"marca\" : \"Campofrio\", \"cadena\" : \"dia\" , \"comprado\" : \"false\" , \"receta\": \"null\", \"urlimagen\": \"null\", \"cantidad\": \"560gr\" }]}";
    public static final String DUMMY_PRODUCTO_LISTA_2="{\"tipos\":[{ \"id\":1, \"nombre\" : \"cafe\" , \"unidades\" : \"1\" , \"marca\" : \"null\", \"cadena\" : \"mercadona\" , \"comprado\" : \"true\" , \"receta\": \"null\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/128/128985.png\", \"cantidad\": \"null\" }],\"comerciales\":[{ \"id\":1, \"nombre\" : \"Filete de cerdo\" , \"unidades\" : \"3\" , \"marca\" : \"Hacendado\", \"cadena\" : \"mrcadona\" , \"comprado\" : \"true\" , \"receta\": \"null\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/123/123281.png\", \"cantidad\": \"500ml\" },{ \"id\":2, \"nombre\" : \"Salchichas\" , \"unidades\" : \"1\" , \"marca\" : \"Campofrio\", \"cadena\" : \"dia\" , \"comprado\" : \"false\" , \"receta\": \"null\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/127/127141.png\", \"cantidad\": \"560gr\" },{ \"id\":213, \"nombre\" : \"Huevo\" , \"unidades\" : \"1\" , \"marca\" : \"Campofrio\", \"cadena\" : \"supersol\" , \"comprado\" : \"false\" , \"receta\": \"null\", \"urlimagen\": \"https://image.flaticon.com/icons/png/512/129/129998.png\", \"cantidad\": \"null\"}]}";
    public static final String DUMMY_LISTA_ACEPTADA="CLC;4";
    public static final String DUMMY_COMPARTIR_LISTA_ACEPTADA="ALC";


    public static final String PETICIONES_INDIRECTAS_ENVIADAS ="NC" ;
    public static final String BORRAR_LISTA_ACEPTADA = "SLC";
    public static final String BORRAR_LISTA_FALLIDA = "SLF";
    public static final String NOTIFICACIONES_CORRECTA = "NC";
    public static final String NOTIFICACIONES_PROCESADA_CORRECTA ="NPC" ;

    //Comprobar si estoy logueado
    public static final String COMPROBAR_LOGUEO="LD";
    public static final String COMPROBAR_LOGUEO_CORRECTA ="LDC" ;
    public static final String COMPROBAR_LOGUEO_FALLIDA ="LDF" ;

    //Loguout
    public static final String LOGOUT="LO";

    //Recetas
    public static final String RECETAS_CATEGORIA_PETICION ="REC" ;
    public static final String RECETAS_CATEGORIA_CORRECTA ="RECC" ;
    public static final String RECETAS_CATEGORIA_FALLIDA ="RECF" ;

    public static final String RECETA_ALEATORIA_PETICION ="RA" ;
    public static final String RECETA_ALEATORIA_CORRECTA ="RAC";

    public static final String INTERIOR_RECETA_PETICION="IRE";
    public static final String INTERIOR_RECETA_CORRECTA="IREC";

    //Notificaciones
    public static final String PEDIR_NOTIFICACIONES ="MN" ;
    public static final String PEDIR_NOTIFICACIONES_CORRECTA ="MNC" ;

}

