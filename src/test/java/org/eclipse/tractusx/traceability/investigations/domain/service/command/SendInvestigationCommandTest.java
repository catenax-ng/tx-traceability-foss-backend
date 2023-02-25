package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.domain.model.Investigation;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationId;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationStatus;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;
import org.eclipse.tractusx.traceability.investigations.domain.service.InvestigationsReadService;
import org.eclipse.tractusx.traceability.investigations.domain.service.NotificationsService;
import org.eclipse.tractusx.traceability.testdata.InvestigationTestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

class SendInvestigationCommandTest {

	@InjectMocks
	private SendInvestigationCommand command;
	@Mock
	private InvestigationsRepository repository;
	@Mock
	private InvestigationsReadService investigationsReadService;
	@Mock
	private NotificationsService notificationsService;


	@Test
	void testExecuteInvestigationCommand() {
		// Given
		final BPN bpn = new BPN("bpn123");
		final Long id = 1L;
		InvestigationId investigationId = new InvestigationId(id);
		Investigation investigation = InvestigationTestDataFactory.createInvestigationTestData(InvestigationStatus.ACKNOWLEDGED, InvestigationStatus.RECEIVED);

		when(investigationsReadService.loadInvestigation(investigationId)).thenReturn(investigation);
		when(repository.update(investigation)).thenReturn(investigationId);

		command = new SendInvestigationCommand(repository, investigationsReadService,
			notificationsService, bpn, id);

		// When
		command.executeInvestigationCommand();

		// Then
		verify(investigationsReadService).loadInvestigation(investigationId);
		verify(repository).update(investigation);
		verify(notificationsService).updateAsync(any());
	}
}
