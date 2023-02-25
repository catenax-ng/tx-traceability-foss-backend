package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.domain.model.Investigation;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationId;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;
import org.eclipse.tractusx.traceability.investigations.domain.service.InvestigationsReadService;

public record CancelInvestigationCommand(
	InvestigationsRepository repository,
	InvestigationsReadService investigationsReadService,
	BPN bpn, Long id) implements InvestigationCommand {

	@Override
	public InvestigationId executeInvestigationCommand() {
		InvestigationId investigationId = new InvestigationId(id);
		Investigation investigation = investigationsReadService.loadInvestigation(investigationId);
		investigation.cancel(bpn);
		return repository.update(investigation);
	}
}
