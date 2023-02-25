package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.assets.domain.ports.AssetRepository;
import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.domain.model.Investigation;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationStatus;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;
import org.eclipse.tractusx.traceability.testdata.AssetTestDataFactory;
import org.eclipse.tractusx.traceability.testdata.InvestigationTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartInvestigationCommandTest {

	@InjectMocks
	private StartInvestigationCommand command;

	@Mock
	private InvestigationsRepository repository;
	@Mock
	private AssetRepository assetRepository;
	@Mock
	private Clock clock;

	@Test
	public void testExecuteInvestigationCommand() {
		// Given
		Investigation investigation = InvestigationTestDataFactory.createInvestigationTestData(InvestigationStatus.ACKNOWLEDGED, InvestigationStatus.CLOSED);
		when(assetRepository.getAssetsById(Arrays.asList("asset-1", "asset-2"))).thenReturn(List.of(AssetTestDataFactory.createAssetTestData()));
		when(repository.save(any(Investigation.class))).thenReturn(investigation.getId());
		command = new StartInvestigationCommand(clock, BPN.of("bpn-123"),
			Arrays.asList("asset-1", "asset-2"),
			"Test investigation", repository, assetRepository);

		// When
		command.executeInvestigationCommand();

		// Then
		verify(assetRepository).getAssetsById(Arrays.asList("asset-1", "asset-2"));
		verify(repository).save(any(Investigation.class));

	}
}
