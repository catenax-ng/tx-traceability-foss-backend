package org.eclipse.tractusx.traceability.investigations.domain.service.command;

import org.eclipse.tractusx.traceability.assets.domain.model.Asset;
import org.eclipse.tractusx.traceability.assets.domain.ports.AssetRepository;
import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.domain.model.*;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record StartInvestigationCommand(Clock clock, BPN bpn,
										List<String> assetIds, String description,
										InvestigationsRepository repository,
										AssetRepository assetRepository) implements InvestigationCommand {

	@Override
	public InvestigationId executeInvestigationCommand() {
		Investigation investigation = Investigation.startInvestigation(clock.instant(), bpn, description);

		Map<String, List<Asset>> assetsByManufacturer = assetRepository.getAssetsById(assetIds).stream().collect(Collectors.groupingBy(Asset::getManufacturerId));

		assetsByManufacturer.entrySet().stream()
			.map(it -> new Notification(
				UUID.randomUUID().toString(),
				null,
				bpn.value(),
				it.getKey(),
				null,
				null,
				description,
				InvestigationStatus.RECEIVED,
				it.getValue().stream().map(Asset::getId).map(AffectedPart::new).collect(Collectors.toList())
			)).forEach(investigation::addNotification);

		return repository.save(investigation);
	}
}
