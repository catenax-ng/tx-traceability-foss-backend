package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.domain.model.Investigation;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationId;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;
import org.eclipse.tractusx.traceability.investigations.domain.service.InvestigationsReadService;
import org.eclipse.tractusx.traceability.investigations.domain.service.NotificationsService;

public record CloseInvestigationCommand(
	InvestigationsReadService investigationsReadService,
	InvestigationsRepository investigationRepository,
	NotificationsService notificationsService,
	BPN bpn, Long id,
	String reason) implements InvestigationCommand {

	@Override
	public InvestigationId executeInvestigationCommand() {
		InvestigationId investigationId = new InvestigationId(id);

		Investigation investigation = investigationsReadService.loadInvestigation(investigationId);

		investigation.close(bpn, reason);

		InvestigationId persistedInvestigationId = investigationRepository.update(investigation);

		investigation.getNotifications().forEach(notificationsService::updateAsync);
		return persistedInvestigationId;
	}
}
