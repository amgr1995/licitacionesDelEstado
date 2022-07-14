package es.licitaciones.estado.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PresentacionOferta {

	ELECTRONICA(1, "Electrónica"), 
	MANUAL(2, "Manual"),
	MANUAL_Y_O_ELECTRONICA(3, "Manual y/o Electrónica");

	private Integer code;
	private String nombre;
	
	public static String retrieveByCode(String code) {
        for (PresentacionOferta presentacionOferta : PresentacionOferta.values()) {
            if (presentacionOferta.getCode() == Integer.valueOf(code)) {
                return presentacionOferta.getNombre();
            }
        }
        return null;
    }
}
