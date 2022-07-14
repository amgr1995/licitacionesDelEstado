package es.licitaciones.estado.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TipoContrato {

	SUMINISTROS(1, "Suministros"), 
	SERVICIOS(2, "Servicios"), 
	OBRAS(3, "Obras"),
	GESTION_SERVICIOS_PUBLICOS(21, "Gestión de Servicios Públicos"), 
	CONCESION_DE_SERVICIOS(22, "Concesión de Servicios"),
	CONCESION_DE_OBRAS_PUBLICAS(31, "Concesión de Obras Públicas"),
	CONCESION_DE_OBRAS(32, "Concesión de Obras"),
	COLABORACION_ENTRE_SECTOR_PUBLICO_Y_PRIVADO(40, "Colaboración entre el sector público y sector privado"),
	ADMINISTRATIVO_ESPECIAL(7, "Administrativo especial"),
	PRIVADO(8, "Privado"),
	PATRIMONIAL(50, "Patrimonial");

	private Integer code;
	private String nombre;
	
	public static String retrieveByCode(String code) {
        for (TipoContrato tipoContrato : TipoContrato.values()) {
            if (tipoContrato.getCode() == Integer.valueOf(code)) {
                return tipoContrato.getNombre();
            }
        }
        return null;
    }
}
