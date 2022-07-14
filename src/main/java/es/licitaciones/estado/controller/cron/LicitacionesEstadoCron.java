package es.licitaciones.estado.controller.cron;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.licitaciones.estado.service.BidProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LicitacionesEstadoCron {

	@Value("${plataforma.licitaciones.url:}")
	private String urlLicitacionesDiarias;

	private final BidProcessService bidProcessService;

	@Scheduled(cron = "0 0 */1 * * *")
//	@Scheduled(cron="* */1 * * * ?")
	public void jobListenLicitacion() {
		log.info("jobListenLicitacion {}", urlLicitacionesDiarias);

		bidProcessService.procesarDirectorio(urlLicitacionesDiarias);
	}
}
