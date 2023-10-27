package com.maid.gardeningfriend;

/**
 * contiene una funcion que identifica el elem clickeado
 * para despues identificar sobre que elem se clickeo
 * la opcion de "eliminar" o "añadir sobre el area de CULTIVOS
 */
public interface PanelAdminInterface {
    void eliminarBtn(int position);
    void editarBtn(int position);
}
