/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.traceability.investigations.domain.service;

import org.eclipse.tractusx.traceability.assets.domain.ports.AssetRepository;
import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationId;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;
import org.eclipse.tractusx.traceability.investigations.domain.service.command.*;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

@Service
public class InvestigationsPublisherService {

	private final NotificationsService notificationsService;
	private final InvestigationsRepository repository;
	private final InvestigationsReadService investigationsReadService;
	private final AssetRepository assetRepository;
	private final Clock clock;
	private final InvestigationCommandInvoker investigationCommandInvoker;


	public InvestigationsPublisherService(NotificationsService notificationsService, InvestigationsRepository repository, InvestigationsReadService investigationsReadService, AssetRepository assetRepository, Clock clock, InvestigationCommandInvoker investigationCommandInvoker) {
		this.notificationsService = notificationsService;
		this.repository = repository;
		this.investigationsReadService = investigationsReadService;
		this.assetRepository = assetRepository;
		this.clock = clock;
		this.investigationCommandInvoker = investigationCommandInvoker;
	}

	/**
	 * Starts a new investigation with the given BPN, asset IDs and description.
	 *
	 * @param bpn         the BPN to use for the investigation
	 * @param assetIds    the IDs of the assets to investigate
	 * @param description the description of the investigation
	 * @return the ID of the newly created investigation
	 */
	public InvestigationId startInvestigation(BPN bpn, List<String> assetIds, String description) {
		StartInvestigationCommand startInvestigationCommand = new StartInvestigationCommand(clock, bpn, assetIds,
			description, repository, assetRepository);
		this.investigationCommandInvoker.setCommand(startInvestigationCommand);
		return this.investigationCommandInvoker.handleInvestigation();
	}

	/**
	 * Cancels an ongoing investigation with the given BPN and ID.
	 *
	 * @param bpn the BPN associated with the investigation
	 * @param id  the ID of the investigation to cancel
	 */
	public void cancelInvestigation(BPN bpn, Long id) {
		CancelInvestigationCommand cancelInvestigationCommand = new CancelInvestigationCommand(repository,
			investigationsReadService,
			bpn,
			id);
		this.investigationCommandInvoker.setCommand(cancelInvestigationCommand);
		this.investigationCommandInvoker.handleInvestigation();
	}

	/**
	 * Sends an ongoing investigation with the given BPN and ID to the next stage.
	 *
	 * @param bpn the BPN associated with the investigation
	 * @param id  the ID of the investigation to send
	 */
	public void sendInvestigation(BPN bpn, Long id) {
		SendInvestigationCommand sendInvestigationCommand = new SendInvestigationCommand(repository,
			investigationsReadService,
			notificationsService,
			bpn,
			id);
		this.investigationCommandInvoker.setCommand(sendInvestigationCommand);
		this.investigationCommandInvoker.handleInvestigation();
	}

	/**
	 * Closes an ongoing investigation with the given BPN, ID and reason.
	 *
	 * @param bpn    the BPN associated with the investigation
	 * @param id     the ID of the investigation to close
	 * @param reason the reason for closing the investigation
	 */
	public void closeInvestigation(BPN bpn, Long id, String reason) {
		CloseInvestigationCommand closeInvestigationCommand = new CloseInvestigationCommand(investigationsReadService,
			repository,
			notificationsService,
			bpn,
			id,
			reason);
		this.investigationCommandInvoker.setCommand(closeInvestigationCommand);
		this.investigationCommandInvoker.handleInvestigation();
	}
}
