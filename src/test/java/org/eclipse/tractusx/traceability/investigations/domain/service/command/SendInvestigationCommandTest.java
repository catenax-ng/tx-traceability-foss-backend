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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
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
		InvestigationId investigationId = new InvestigationId(1L);
		Investigation investigation = InvestigationTestDataFactory.createInvestigationTestData(InvestigationStatus.ACKNOWLEDGED, InvestigationStatus.RECEIVED);

		command = new SendInvestigationCommand(repository, investigationsReadService,
			notificationsService, bpn, 1L);
		when(investigationsReadService.loadInvestigation(investigationId)).thenReturn(investigation);
		when(repository.update(investigation)).thenReturn(investigationId);

		// When
		command.executeInvestigationCommand();

		// Then
		verify(investigationsReadService).loadInvestigation(investigationId);
		verify(repository).update(investigation);
		verify(notificationsService).updateAsync(any());
	}
}
