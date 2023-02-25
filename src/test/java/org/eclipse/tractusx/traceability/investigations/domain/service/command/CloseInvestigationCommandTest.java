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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloseInvestigationCommandTest {

	@InjectMocks
	private CloseInvestigationCommand command;

	@Mock
	private InvestigationsReadService investigationsReadService;
	@Mock
	private InvestigationsRepository investigationRepository;
	@Mock
	private NotificationsService notificationsService;

	@Test
	void executeInvestigationCommand() {

		// Given
		final long id = 1L;
		final String reason = "TEST_REASON";
		final BPN bpn = new BPN("bpn123");
		command = new CloseInvestigationCommand(investigationsReadService, investigationRepository, notificationsService,
			bpn, id, reason);
		InvestigationId investigationId = new InvestigationId(id);
		Investigation investigation = InvestigationTestDataFactory.createInvestigationTestData(InvestigationStatus.ACKNOWLEDGED, InvestigationStatus.RECEIVED);
		when(investigationsReadService.loadInvestigation(investigationId)).thenReturn(investigation);
		when(investigationRepository.update(investigation)).thenReturn(investigationId);

		// When
		InvestigationId result = command.executeInvestigationCommand();

		// Then
		verify(investigationsReadService).loadInvestigation(investigationId);
		verify(investigationRepository).update(investigation);
		verify(notificationsService).updateAsync(any());
		assertEquals(investigationId, result);
	}

}


