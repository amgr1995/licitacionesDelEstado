package es.licitaciones.estado.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "LICITACIONES")
public class Licitaciones implements Serializable {

	private static final long serialVersionUID = 330365217512296036L;

	@Id
	@Column(name = "IDENTIFICADOR")
	private Long identificador;

	@Column(name = "LINK_LICITACION")
	private String linkLicitacion;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_ACTUALIZACION")
	private LocalDateTime fechaActualizacion;

	@Column(name = "VIGENTE_ANULADA_ARCHIVADA")
	private String vigenteAnuladaArchivada;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "PRIMERA_PUBLICACION")
	private LocalDateTime primeraPublicacion;

	@Column(name = "ESTADO")
	private String estado;

	@Column(name = "NUMERO_EXPEDIENTE")
	private String numeroExpediente;

	@Column(name = "OBJETO_DEL_CONTRATO")
	private String objetoDelContrato;

	@Column(name = "VALOR_ESTIMADO_CONTRATO")
	private BigDecimal valorEstimadoContrato;

	@Column(name = "PRESUPUESTO_BASE_SIN_IMPUESTOS")
	private BigDecimal presupuestoBaseSinImpuestos;

	@Column(name = "PRESUPUESTO_BASE_CON_IMPUESTOS")
	private BigDecimal presupuestoBaseConImpuestos;

	@Column(name = "CPV")
	private String cpv;

	@Column(name = "TIPO_CONTRATO")
	private String tipoContrato;

	@Column(name = "LUGAR_EJECUCION")
	private String lugarEjecucion;

	@Column(name = "ORGANO_CONTRATACION")
	private String organoContratacion;

	@Column(name = "ID_OC_PLACSP")
	private String idOcPlacsp;

	@Column(name = "NIF_OC")
	private String nifOc;

	@Column(name = "DIR3")
	private String dir3;

	@Column(name = "ENLACE_AL_PERFIL_CONTRATANTE_DEL_OC")
	private String enlaceAlPerfilContratanteDelOc;

	@Column(name = "TIPO_ADMINISTRACION")
	private String tipoAdministracion;

	@Column(name = "CODIGO_POSTAL")
	private String codigoPostal;

	@Column(name = "TIPO_PROCEDIMIENTO")
	private String tipoProcedimiento;

	@Column(name = "SISTEMA_CONTRATACION")
	private String sistemaContratacion;

	@Column(name = "TRAMITACION")
	private String tramitacion;

	@Column(name = "FORMA_PRESENTACION_OFERTA")
	private String formaPresentacionOferta;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_PRESENTACION_OFERTAS")
	private LocalDateTime fechaPresentacionOfertas;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_PRESENTACION_SOLICITUDES_PARTICIPACION")
	private LocalDateTime fechaPresentacionSolicitudesParticipacion;

	@Column(name = "DIRECTIVA_APLICACION")
	private String directivaAplicacion;

	@Column(name = "SUBCONTRATACION_PERMITIDA")
	private String subcontratacionPermitida;

	@Column(name = "SUBCONTRATACION_PERMITIDA_PORCENTAJE")
	private BigDecimal subcontratacionPermitidaPorcentaje;

	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_CREACION_REGISTRO_BBDD")
	private LocalDateTime fechaCreacionRegistroBBDD;

	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_ACTUALIZACION_REGISTRO_BBDD")
	private LocalDateTime fechaActualizacionRegistroBBDD;
}
