package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationId;
import org.springframework.stereotype.Component;

@Component
public class InvestigationCommandInvoker {
	private InvestigationCommand command;

	public void setCommand(InvestigationCommand command) {
		this.command = command;
	}

	public InvestigationId handleInvestigation() {
		return command.executeInvestigationCommand();
	}
}
