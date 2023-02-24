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
package org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.model;

import org.eclipse.tractusx.traceability.investigations.domain.model.AffectedPart;
import org.eclipse.tractusx.traceability.investigations.domain.model.Notification;

import java.util.List;
import java.util.stream.Collectors;

public class EDCNotificationFactory {

	private EDCNotificationFactory(){
	}

	public static EDCNotification createQualityInvestigation(String senderEDC, Notification notification){
		EDCNotificationHeader header = new EDCNotificationHeader(
			notification.getId(),
			notification.getSenderBpnNumber(),
			senderEDC,
			notification.getReceiverBpnNumber(),
			NotificationType.QMINVESTIGATION.getValue(),
			"MINOR",
			null,
			notification.getInvestigationStatus().name(),
			null
		);

		EDCNotificationContent content = new EDCNotificationContent(
			notification.getDescription(),
			extractAssetIds(notification)
		);

		return new EDCNotification(header, content);
	}

	private static List<String> extractAssetIds(Notification notification) {
		return notification.getAffectedParts().stream()
			.map(AffectedPart::assetId)
			.collect(Collectors.toList());
	}
}

