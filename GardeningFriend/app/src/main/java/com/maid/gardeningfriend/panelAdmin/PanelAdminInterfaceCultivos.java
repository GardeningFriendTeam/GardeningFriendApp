package com.maid.gardeningfriend.panelAdmin;

/**
 * contiene una funcion que identifica el elem clickeado
 * para despues identificar sobre que elem se clickeo
 * la opcion de "eliminar" o "añadir sobre el area de CULTIVOS
 */
public interface PanelAdminInterfaceCultivos {
    void eliminarBtn(int position);
    void editarBtn(int position);
}
