package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.domain.model.Investigation;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationId;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationStatus;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;
import org.eclipse.tractusx.traceability.investigations.domain.service.InvestigationsReadService;
import org.eclipse.tractusx.traceability.testdata.InvestigationTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelInvestigationCommandTest {

	@InjectMocks
	private CancelInvestigationCommand command;
	@Mock
	private InvestigationsRepository repository;
	@Mock
	private InvestigationsReadService readService;


	@Test
	public void testExecuteInvestigationCommand() {
		// Given
		BPN bpn = new BPN("bpn123");
		Long id = 1L;
		Investigation investigation = InvestigationTestDataFactory.createInvestigationTestData(InvestigationStatus.CREATED, InvestigationStatus.CREATED);
		command = new CancelInvestigationCommand(repository, readService, bpn, id);
		when(readService.loadInvestigation(any())).thenReturn(investigation);
		when(repository.update(any(Investigation.class))).thenReturn(new InvestigationId(id));

		// when
		InvestigationId result = command.executeInvestigationCommand();

		// Then
		verify(readService).loadInvestigation(new InvestigationId(id));
		verify(repository).update(investigation);
		assertEquals(result, new InvestigationId(id));
		assertEquals(investigation.getInvestigationStatus(), InvestigationStatus.CANCELED);
	}

}
