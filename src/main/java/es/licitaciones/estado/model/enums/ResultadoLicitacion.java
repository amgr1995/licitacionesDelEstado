package es.licitaciones.estado.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResultadoLicitacion {

	ADJUDICADO_PROVISIONALMENTE(1, "Adjudicado Provisionalmente"), 
	ADJUDICADO_DEFINITIVAMENTE(2, "Adjudicado Definitivamente"),
	DESIERTO(3, "Desierto"),
	DESISTIMIENTO(4, "Desistimiento"),
	RENUNCIA(5, "Renuncia"),
	DESIERTO_PROVISIONALMENTE(6, "Desierto Provisionalmente"),
	DESIERTO_DEFINITIVAMENTE(7, "Desierto Definitivamente"),
	ADJUDICADO(8, "Adjudicado"),
	FORMALIZADO(9, "Formalizado"),
	LICITADOR_MEJOR_VALORADO_REQ_DOCUMENTACION(10, "Licitador mejor valorado:Requerimiento de documentacion");

	private Integer code;
	private String nombre;
	
	public static String retrieveByCode(String code) {
        for (ResultadoLicitacion resultadoLicitacion : ResultadoLicitacion.values()) {
            if (resultadoLicitacion.getCode() == Integer.valueOf(code)) {
                return resultadoLicitacion.getNombre();
            }
        }
        return null;
    }
}
