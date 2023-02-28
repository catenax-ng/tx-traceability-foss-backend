package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.domain.model.Investigation;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationId;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;
import org.eclipse.tractusx.traceability.investigations.domain.service.InvestigationsReadService;
import org.eclipse.tractusx.traceability.investigations.domain.service.NotificationsService;

public record SendInvestigationHandler(
	InvestigationsRepository repository,
	InvestigationsReadService investigationsReadService,
	NotificationsService notificationsService,
	BPN bpn, Long id) implements InvestigationHandler {

	@Override
	public InvestigationId executeInvestigation() {
		InvestigationId investigationId = new InvestigationId(id);

		Investigation investigation = investigationsReadService.loadInvestigation(investigationId);

		investigation.send(bpn);

		InvestigationId persistedInvestigationId = repository.update(investigation);

		investigation.getNotifications().forEach(notificationsService::updateAsync);
		return persistedInvestigationId;
	}
}
