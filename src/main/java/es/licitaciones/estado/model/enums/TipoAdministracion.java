package es.licitaciones.estado.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TipoAdministracion {

	ADMINISTRACION_GENERAL_ESTADO(1, "Administración General del Estado"), 
	COMUNIDAD_AUTONOMA(2, "Comunidad Autónoma"), 
	ADMINISTRACION_LOCAL(3, "Administración Local"),
	ENTIDAD_DERECHO_PUBLICO(4, "Entidad de Derecho Público"), 
	OTRAS_ENTIDADES_SECTOR_PUBLICO(5, "Otras Entidades del Sector Público");

	private Integer code;
	private String nombre;
	
	public static String retrieveByCode(String code) {
        for (TipoAdministracion tipoAdministracion : TipoAdministracion.values()) {
            if (tipoAdministracion.getCode() == Integer.valueOf(code)) {
                return tipoAdministracion.getNombre();
            }
        }
        return null;
    }
}
