package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationId;

public interface InvestigationCommand {
	InvestigationId executeInvestigationCommand();
}
