package es.licitaciones.estado.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.purl.atompub.tombstones._1.DeletedEntryType;
import org.springframework.stereotype.Service;
import org.w3._2005.atom.EntryType;
import org.w3._2005.atom.FeedType;
import org.w3._2005.atom.LinkType;

import es.licitaciones.estado.model.DatosResultados;
import es.licitaciones.estado.model.DatosSeleccionables;
import es.licitaciones.estado.model.entity.Licitaciones;
import es.licitaciones.estado.model.entity.ResultadoLicitaciones;
import es.licitaciones.estado.repository.LicitacionesRepository;
import es.licitaciones.estado.repository.ResultadoLicitacionesRepository;
import ext.place.codice.common.caclib.ContractFolderStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidProcessService {

	private static final String NEXT = "next";
	private static final String REGEX = "/";
	private static final String ATOM = ".atom";
	private static final String LICITACIONES_DEL_ESTADO = "licitacionesDelEstado";
	private static final String HTTP = "http";
	private static final String HTTPS = "https";

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static Unmarshaller atomUnMarshaller;
	private final LicitacionesRepository licitacionesRepository;
	private final ResultadoLicitacionesRepository resultadoLicitacionesRepository;

	public void procesarDirectorio(String ficheroDeEntrada) {

		HashSet<String> entriesProcesadas = new HashSet<String>();
		HashMap<String, GregorianCalendar> entriesDeleted = new HashMap<String, GregorianCalendar>();
		int numeroEntries = 0;
		int numeroFicherosProcesados = 0;

		FeedType res = null;
		InputStreamReader inStream = null;

		try {
			JAXBContext jc = JAXBContext.newInstance(
					"org.w3._2005.atom:org.dgpe.codice.common.caclib:org.dgpe.codice.common.cbclib:ext.place.codice.common.caclib:ext.place.codice.common.cbclib:org.purl.atompub.tombstones._1");
			atomUnMarshaller = jc.createUnmarshaller();

			log.info("Comienza el procesamiento de los ficheros");

			List<DatosSeleccionables> buscadorDatosSeleecionables = Arrays.asList(DatosSeleccionables.values());
			List<DatosResultados> buscadorDatosResultados = Arrays.asList(DatosResultados.values());

			File ficheroRISP = null;
			URL resource = null;
			if (ficheroDeEntrada.contains(HTTP)) {
				resource = new URL(ficheroDeEntrada.replace(HTTPS, HTTP));
				ficheroRISP = File.createTempFile(LICITACIONES_DEL_ESTADO, ATOM);
				FileUtils.copyURLToFile(resource, ficheroRISP);
			} else {
				ficheroRISP = new File(ficheroDeEntrada);
			}

			String directorioPath = ficheroRISP.getParent();
			boolean existeFicheroRisp = ficheroRISP.exists() && ficheroRISP.isFile();

			if (existeFicheroRisp) {
				log.info("Directorio originen de ficheros RISP-PLACSP: " + directorioPath);
				log.info("Fichero raiz: " + ficheroRISP.getName());
			} else {
				log.error("No se puede acceder al fichero " + ficheroDeEntrada.replace(HTTPS, HTTP));
			}

			File[] lista_ficherosRISP = ficheroRISP.getParentFile().listFiles();
			log.info("Número previsto de ficheros a procesar: " + lista_ficherosRISP.length);

			// calculo de cada salto
			double saltoBar = 1.00 / lista_ficherosRISP.length;
			double saltoAcumuladoBar = 0;

			while (existeFicheroRisp) {
				log.info("Procesando fichero: " + ficheroRISP.getName());

				saltoAcumuladoBar += saltoBar;
				log.info("Ratio de archivos procesados: " + saltoAcumuladoBar * 100.00 + " %");

				res = null;

				inStream = new InputStreamReader(new FileInputStream(ficheroRISP), StandardCharsets.UTF_8);
				res = ((JAXBElement<FeedType>) atomUnMarshaller.unmarshal(inStream)).getValue();

				if (res.getAny() != null) {
					for (int indice = 0; indice < res.getAny().size(); indice++) {
						DeletedEntryType deletedEntry = ((JAXBElement<DeletedEntryType>) res.getAny().get(indice))
								.getValue();
						if (!entriesDeleted.containsKey(deletedEntry.getRef())) {
							entriesDeleted.put(deletedEntry.getRef(), deletedEntry.getWhen().toGregorianCalendar());
						}
					}
				}

				numeroEntries += res.getEntry().size();
				for (EntryType entry : res.getEntry()) {

					if (!entriesProcesadas.contains(entry.getId().getValue())) {
						GregorianCalendar fechaDeleted = null;
						if (entriesDeleted.containsKey(entry.getId().getValue())) {
							fechaDeleted = entriesDeleted.get(entry.getId().getValue());
						}
						procesarEntry(entry, fechaDeleted, buscadorDatosSeleecionables);
						procesarEntryResultados(entry, buscadorDatosResultados);
						entriesProcesadas.add(entry.getId().getValue());
					}
				}

				for (LinkType linkType : res.getLink()) {
					existeFicheroRisp = false;
					if (linkType.getRel().toLowerCase().compareTo(NEXT) == 0) {

						if (!ficheroDeEntrada.contains(HTTP)) {
							String[] tempArray = linkType.getHref().split(REGEX);
							String nombreSiguienteRIPS = tempArray[tempArray.length - 1];
							ficheroRISP = new File(directorioPath + REGEX + nombreSiguienteRIPS);
						} else {

							resource = new URL(linkType.getHref().replace(HTTPS, HTTP));
							ficheroRISP.delete();
							ficheroRISP = File.createTempFile(LICITACIONES_DEL_ESTADO, ATOM);
							FileUtils.copyURLToFile(resource, ficheroRISP);
						}
						existeFicheroRisp = ficheroRISP.exists() && ficheroRISP.isFile();
					}
				}
				inStream.close();
				numeroFicherosProcesados++;
			}

			log.info("Número de ficheros procesados " + numeroFicherosProcesados);
			log.info("Número de elementos entry existentes: " + numeroEntries);
			log.info("Licitaciones insertadas en el fichero: " + entriesProcesadas.size());

			log.info("Fin del proceso de almacenamiento de licitaciones");

		} catch (JAXBException e) {
			String auxError = "Error al procesar el fichero ATOM. No se puede continuar con el proceso.";
			log.error(auxError);
			log.error(e.getMessage());
		} catch (FileNotFoundException e) {
			String auxError = "Error al buscar el fichero de entrada. No se pudo encontrar el fichero.";
			log.error(auxError);
			log.debug(e.getMessage());
		} catch (Exception e) {
			String auxError = "Error inesperado, revise la configuración y el log...";
			e.printStackTrace();
			log.error(auxError);
			log.debug(e.getMessage());
		}
	}

	private void procesarEntry(EntryType entry, GregorianCalendar fechaDeleted,
			List<DatosSeleccionables> buscadorDatosSeleecionables) {
		ContractFolderStatusType contractFolder = ((JAXBElement<ContractFolderStatusType>) entry.getAny().get(0))
				.getValue();

		Licitaciones licitaciones = new Licitaciones();
		licitaciones.setFechaCreacionRegistroBBDD(LocalDateTime.now());
		licitaciones.setFechaActualizacionRegistroBBDD(LocalDateTime.now());
		licitaciones.setIdentificador(
				Long.valueOf(entry.getId().getValue().substring(entry.getId().getValue().lastIndexOf(REGEX) + 1)));
		licitaciones.setLinkLicitacion(entry.getLink().get(0).getHref());

		GregorianCalendar updated = entry.getUpdated().getValue().toGregorianCalendar();
		licitaciones
				.setFechaActualizacion(LocalDateTime.ofInstant(updated.toInstant(), updated.getTimeZone().toZoneId()));

		if (fechaDeleted == null || fechaDeleted.compareTo(updated) < 0) {
			licitaciones.setVigenteAnuladaArchivada("VIGENTE");
		} else {
			if (((fechaDeleted.getTimeInMillis() - updated.getTimeInMillis()) / 1000 / 3660 / 24 / 365) > 5) {
				licitaciones.setVigenteAnuladaArchivada("ARCHIVADA");
			} else {
				licitaciones.setVigenteAnuladaArchivada("ANULADA");
			}
		}

		for (DatosSeleccionables dato : buscadorDatosSeleecionables) {
			Object datoCodice = dato.valorCodice(contractFolder);

			try {
				if (!Objects.isNull(datoCodice)) {
					if ("Primera publicación".equals(dato.getTitulo())) {
						licitaciones.setPrimeraPublicacion(
								LocalDateTime.ofInstant(((GregorianCalendar) datoCodice).toInstant(),
										((GregorianCalendar) datoCodice).getTimeZone().toZoneId()));
						if (licitaciones.getPrimeraPublicacion().getYear() < 1900) {
							licitaciones.setPrimeraPublicacion(null);
						}

					} else if ("Estado".equals(dato.getTitulo())) {
						licitaciones.setEstado((String) datoCodice);

					} else if ("Número de expediente".equals(dato.getTitulo())) {
						licitaciones.setNumeroExpediente((String) datoCodice);

					} else if ("Objeto del Contrato".equals(dato.getTitulo())) {
						licitaciones.setObjetoDelContrato((String) datoCodice);

					} else if ("Valor estimado del contrato".equals(dato.getTitulo())) {
						licitaciones.setValorEstimadoContrato((BigDecimal) datoCodice);

					} else if ("Presupuesto base sin impuestos".equals(dato.getTitulo())) {
						licitaciones.setPresupuestoBaseSinImpuestos((BigDecimal) datoCodice);

					} else if ("Presupuesto base con impuestos".equals(dato.getTitulo())) {
						licitaciones.setPresupuestoBaseConImpuestos((BigDecimal) datoCodice);

					} else if ("CPV".equals(dato.getTitulo())) {
						licitaciones.setCpv((String) datoCodice);

					} else if ("Tipo de contrato".equals(dato.getTitulo())) {
						licitaciones.setTipoContrato((String) datoCodice);

					} else if ("Lugar de ejecución".equals(dato.getTitulo())) {
						licitaciones.setLugarEjecucion((String) datoCodice);

					} else if ("Órgano de Contratación".equals(dato.getTitulo())) {
						licitaciones.setOrganoContratacion((String) datoCodice);

					} else if ("ID OC en PLACSP".equals(dato.getTitulo())) {
						licitaciones.setIdOcPlacsp((String) datoCodice);

					} else if ("NIF OC".equals(dato.getTitulo())) {
						licitaciones.setNifOc((String) datoCodice);

					} else if ("DIR3".equals(dato.getTitulo())) {
						licitaciones.setDir3((String) datoCodice);

					} else if ("Enlace al Perfil de Contratante del OC".equals(dato.getTitulo())) {
						licitaciones.setEnlaceAlPerfilContratanteDelOc((String) datoCodice);

					} else if ("Tipo de Administración".equals(dato.getTitulo())) {
						licitaciones.setTipoAdministracion((String) datoCodice);

					} else if ("Código Postal".equals(dato.getTitulo())) {
						licitaciones.setCodigoPostal((String) datoCodice);

					} else if ("Tipo de procedimiento".equals(dato.getTitulo())) {
						licitaciones.setTipoProcedimiento((String) datoCodice);

					} else if ("Sistema de contratación".equals(dato.getTitulo())) {
						licitaciones.setSistemaContratacion((String) datoCodice);

					} else if ("Tramitación".equals(dato.getTitulo())) {
						licitaciones.setTramitacion((String) datoCodice);

					} else if ("Forma de presentación de la oferta".equals(dato.getTitulo())) {
						licitaciones.setFormaPresentacionOferta((String) datoCodice);

					} else if ("Fecha de presentación de ofertas".equals(dato.getTitulo())) {
						licitaciones.setFechaPresentacionOfertas(
								LocalDateTime.ofInstant(((GregorianCalendar) datoCodice).toInstant(),
										((GregorianCalendar) datoCodice).getTimeZone().toZoneId()));
						if (licitaciones.getFechaPresentacionOfertas().getYear() < 1900) {
							licitaciones.setFechaPresentacionOfertas(null);
						}

					} else if ("Fecha de presentación de solicitudes de participacion".equals(dato.getTitulo())) {
						licitaciones.setFechaPresentacionSolicitudesParticipacion(
								LocalDateTime.ofInstant(((GregorianCalendar) datoCodice).toInstant(),
										((GregorianCalendar) datoCodice).getTimeZone().toZoneId()));
						if (licitaciones.getFechaPresentacionSolicitudesParticipacion().getYear() < 1900) {
							licitaciones.setFechaPresentacionSolicitudesParticipacion(null);
						}

					} else if ("Directiva de aplicación".equals(dato.getTitulo())) {
						licitaciones.setDirectivaAplicacion((String) datoCodice);

					} else if ("Subcontratación permitida".equals(dato.getTitulo())) {
						licitaciones.setSubcontratacionPermitida((String) datoCodice);

					} else if ("Subcontratación permitida porcentaje".equals(dato.getTitulo())) {
						licitaciones.setSubcontratacionPermitidaPorcentaje((BigDecimal) datoCodice);

					}
				}
			} catch (Exception e) {
				log.error("Error al castear el objeto. ERROR -> {}", e.getMessage());
			}
		}
		Optional<Licitaciones> licitacion = licitacionesRepository.findById(licitaciones.getIdentificador());
		if (licitacion.isPresent()) {
			licitaciones.setFechaActualizacionRegistroBBDD(LocalDateTime.now());
			licitaciones.setFechaCreacionRegistroBBDD(licitacion.get().getFechaCreacionRegistroBBDD());
		}
		licitacionesRepository.save(licitaciones);
	}

	private void procesarEntryResultados(EntryType entry, List<DatosResultados> buscadorDatosResultados) {
		ContractFolderStatusType contractFolder = ((JAXBElement<ContractFolderStatusType>) entry.getAny().get(0))
				.getValue();

		if (contractFolder.getTenderResult() != null) {
			for (int indice = 0; indice < contractFolder.getTenderResult().size(); indice++) {

				ResultadoLicitaciones resultadoLicitacion = new ResultadoLicitaciones();
				resultadoLicitacion.setFechaCreacionRegistroBBDD(LocalDateTime.now());
				resultadoLicitacion.setFechaActualizacionRegistroBBDD(LocalDateTime.now());
				resultadoLicitacion.setIdentificador(Long
						.valueOf(entry.getId().getValue().substring(entry.getId().getValue().lastIndexOf(REGEX) + 1)));

				for (DatosResultados dato : buscadorDatosResultados) {
					Object datoCodice = dato.valorCodice(contractFolder, indice);

					try {
						if (!Objects.isNull(datoCodice)) {
							if ("Número de expediente".equals(dato.getTitulo())) {
								resultadoLicitacion.setNumeroExpediente((String) datoCodice);

							} else if ("Lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setLote((String) datoCodice);

							} else if ("Resultado licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setResultadoLicitacion((String) datoCodice);

							} else if ("Fecha del acuerdo licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setFechaAcuerdoLicitacion(
										LocalDateTime.ofInstant(((GregorianCalendar) datoCodice).toInstant(),
												((GregorianCalendar) datoCodice).getTimeZone().toZoneId()));
								if (resultadoLicitacion.getFechaAcuerdoLicitacion().getYear() < 1900) {
									resultadoLicitacion.setFechaAcuerdoLicitacion(null);
								}

							} else if ("Número de ofertas recibidas por licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setNumeroOfertasRecibidasPorLicitacion((BigDecimal) datoCodice);

							} else if ("Precio de la oferta más baja por licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setPrecioOfertaMasBajaPorLicitacion((BigDecimal) datoCodice);

							} else if ("Precio de la oferta más alta por licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setPrecioOfertaMasAltaPorLicitacion((BigDecimal) datoCodice);

							} else if ("Se han excluído ofertas por ser anormalmente bajas por licitación/lote"
									.equals(dato.getTitulo())) {
								resultadoLicitacion.setExcluidoOfertasPorAnormalmenteBajas((Boolean) datoCodice);

							} else if ("Número del contrato licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setNumeroContrato((String) datoCodice);

							} else if ("Fecha formalización del contrato licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setFechaFormalizacionContrato(
										LocalDateTime.ofInstant(((GregorianCalendar) datoCodice).toInstant(),
												((GregorianCalendar) datoCodice).getTimeZone().toZoneId()));

								if (resultadoLicitacion.getFechaFormalizacionContrato().getYear() < 1900) {
									resultadoLicitacion.setFechaFormalizacionContrato(null);
								}

							} else if ("Fecha entrada en vigor del contrato de licitación/lote"
									.equals(dato.getTitulo())) {
								resultadoLicitacion.setFechaEntradaEnVigorContrato(
										LocalDateTime.ofInstant(((GregorianCalendar) datoCodice).toInstant(),
												((GregorianCalendar) datoCodice).getTimeZone().toZoneId()));

								if (resultadoLicitacion.getFechaEntradaEnVigorContrato().getYear() < 1900) {
									resultadoLicitacion.setFechaEntradaEnVigorContrato(null);
								}

							} else if ("Adjudicatario licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setAdjudicatarioLicitacion((String) datoCodice);

							} else if ("Tipo de identificador de adjudicatario por licitación/lote"
									.equals(dato.getTitulo())) {
								resultadoLicitacion.setTipoIdentificadorAdjudicatario((String) datoCodice);

							} else if ("Identificador Adjudicatario de la licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setIdentificadorAdjudicatario((String) datoCodice);

							} else if ("El adjudicatario es o no PYME de la licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setAdjudicatarioEsONoPyme((Boolean) datoCodice);

							} else if ("Importe adjudicación sin impuestos licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setImporteAdjudicacionSinImpuestos((BigDecimal) datoCodice);

							} else if ("Importe adjudicación con impuestos licitación/lote".equals(dato.getTitulo())) {
								resultadoLicitacion.setImporteAdjudicacionConImpuestos((BigDecimal) datoCodice);

							}
						}
					} catch (Exception e) {
						log.error("Error al castear el objeto. ERROR -> {}", e.getMessage());
					}

					Optional<ResultadoLicitaciones> resultado = resultadoLicitacionesRepository
							.findById(resultadoLicitacion.getIdentificador());
					if (resultado.isPresent()) {
						resultadoLicitacion.setFechaActualizacionRegistroBBDD(LocalDateTime.now());
						resultadoLicitacion
								.setFechaActualizacionRegistroBBDD(resultado.get().getFechaCreacionRegistroBBDD());
					}
					resultadoLicitacionesRepository.save(resultadoLicitacion);
				}

			}
		}
	}
}
