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

package org.eclipse.tractusx.traceability.investigations.domain.model;

import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.investigations.adapters.rest.model.InvestigationData;
import org.eclipse.tractusx.traceability.investigations.adapters.rest.model.InvestigationReason;
import org.eclipse.tractusx.traceability.investigations.domain.model.exception.InvestigationIllegalUpdate;
import org.eclipse.tractusx.traceability.investigations.domain.model.exception.InvestigationStatusTransitionNotAllowed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Investigation {

	public static final Comparator<Investigation> COMPARE_BY_NEWEST_INVESTIGATION_CREATION_TIME = (o1, o2) -> {
		Instant o1CreationTime = o1.createdAt;
		Instant o2CreationTime = o2.createdAt;

		if (o1CreationTime.equals(o2CreationTime)) {
			return 0;
		}

		if (o1CreationTime.isBefore(o2CreationTime)) {
			return 1;
		}

		return -1;
	};

	private InvestigationId investigationId;
	private BPN bpn;
	private InvestigationStatus investigationStatus;
	private InvestigationSide investigationSide;
	private String description;
	private Instant createdAt;
	private List<String> assetIds;
	private Map<String, Notification> notifications;
	private List<Notification> notificationList;
	private String sendTo;

	private String closeReason;
	private String acceptReason;
	private String declineReason;


	public Investigation() {
	}

	public Investigation(InvestigationId investigationId,
						 BPN bpn,
						 InvestigationStatus investigationStatus,
						 InvestigationSide investigationSide,
						 String closeReason,
						 String acceptReason,
						 String declineReason,
						 String description,
						 Instant createdAt,
						 List<String> assetIds,
						 List<Notification> notifications
	) {
		this.investigationId = investigationId;
		this.bpn = bpn;
		this.investigationStatus = investigationStatus;
		this.investigationSide = investigationSide;
		this.closeReason = closeReason;
		this.acceptReason = acceptReason;
		this.declineReason = declineReason;
		this.description = description;
		this.createdAt = createdAt;
		this.assetIds = assetIds;
		this.notifications = notifications.stream()
			.collect(Collectors.toMap(Notification::getId, Function.identity()));
		this.sendTo = notifications.stream()
			.findFirst()
			.map(Notification::getReceiverBpnNumber)
			.orElse(null);
	}

	public static Investigation startInvestigation(Instant createDate, BPN bpn, String description) {
		return new Investigation(null,
			bpn,
			InvestigationStatus.CREATED,
			InvestigationSide.SENDER,
			null,
			null,
			null,
			description,
			createDate,
			new ArrayList<>(),
			new ArrayList<>()
		);
	}

	public static Investigation receiveInvestigation(Instant createDate, BPN bpn, String description) {
		return new Investigation(
			null,
			bpn,
			InvestigationStatus.RECEIVED,
			InvestigationSide.RECEIVER,
			null,
			null,
			null,
			description,
			createDate,
			new ArrayList<>(),
			new ArrayList<>()
		);
	}

	/**
	 * Creates a new investigation with the specified properties and status set to "Received".
	 *
	 * @param createDate  the date when the investigation was created
	 * @param bpn         the BPN associated with the investigation
	 * @param description a description of the investigation
	 * @return the new investigation object
	 */
	public static Investigation createReceivedInvestigation(Instant createDate, BPN bpn, String description) {
		Investigation investigation = new Investigation();
		investigation.setBpn(bpn);
		investigation.setInvestigationStatus(InvestigationStatus.RECEIVED);
		investigation.setInvestigationSide(InvestigationSide.RECEIVER);
		investigation.setDescription(description);
		investigation.setCreatedAt(createDate);
		investigation.setAssetIds(new ArrayList<>());
		investigation.setNotificationList(new ArrayList<>());
		return investigation;
	}

	public List<String> getAssetIds() {
		return Collections.unmodifiableList(assetIds);
	}

	public InvestigationStatus getInvestigationStatus() {
		return investigationStatus;
	}

	public InvestigationSide getInvestigationSide() {
		return investigationSide;
	}

	public String getDescription() {
		return description;
	}

	public InvestigationData toData() {
		return new InvestigationData(
			investigationId.value(),
			investigationStatus.name(),
			description,
			bpn.value(),
			createdAt.toString(),
			Collections.unmodifiableList(assetIds),
			investigationSide,
			new InvestigationReason(
				closeReason,
				acceptReason,
				declineReason
			),
			sendTo
		);
	}

	public boolean hasIdentity() {
		return investigationId != null;
	}

	public String getBpn() {
		return bpn.value();
	}

	public void cancel(BPN callerBpn) {
		validateBPN(callerBpn);

		changeStatusTo(InvestigationStatus.CANCELED);

		this.closeReason = "canceled";
	}

	// TODO: Investigation Receiver
	public void close(BPN callerBpn, String reason) {
		validateBPN(callerBpn);

		changeStatusTo(InvestigationStatus.CLOSED);

		this.closeReason = reason;
	}

	public void send(BPN callerBpn) {
		validateBPN(callerBpn);

		changeStatusTo(InvestigationStatus.SENT);
	}

	public void acknowledge(BPN callerBpn) {
		validateBPN(callerBpn);

		changeStatusTo(InvestigationStatus.ACKNOWLEDGED);
	}

	public void accept(BPN callerBpn, String reason) {
		validateBPN(callerBpn);

		changeStatusTo(InvestigationStatus.ACCEPTED);

		this.acceptReason = reason;
	}

	public void decline(BPN callerBpn, String reason) {
		validateBPN(callerBpn);

		changeStatusTo(InvestigationStatus.DECLINED);

		this.declineReason = reason;
	}

	private void validateBPN(BPN callerBpn) {
		if (!callerBpn.equals(this.bpn)) {
			throw new InvestigationIllegalUpdate("%s bpn has no permissions to update investigation with %s id.".formatted(callerBpn.value(), investigationId.value()));
		}
	}

	private void changeStatusTo(InvestigationStatus to) {
		notifications.values()
			.forEach(notification -> notification.changeStatusTo(to));

		boolean transitionAllowed = investigationStatus.transitionAllowed(to);

		if (!transitionAllowed) {
			throw new InvestigationStatusTransitionNotAllowed(investigationId, investigationStatus, to);
		}

		this.investigationStatus = to;
	}

	public InvestigationId getId() {
		return investigationId;
	}

	public Instant getCreationTime() {
		return createdAt;
	}

	public List<Notification> getNotifications() {
		return new ArrayList<>(notifications.values());
	}

	public Optional<Notification> getNotification(String notificationId) {
		return Optional.ofNullable(notifications.get(notificationId));
	}

	public String getCloseReason() {
		return closeReason;
	}

	public String getAcceptReason() {
		return acceptReason;
	}

	public String getDeclineReason() {
		return declineReason;
	}

	public void addNotification(Notification notification) {
		notifications.put(notification.getId(), notification);

		notification.getAffectedParts().stream()
			.map(AffectedPart::assetId)
			.forEach(assetIds::add);
	}

	public InvestigationId getInvestigationId() {
		return investigationId;
	}

	public void setInvestigationId(InvestigationId investigationId) {
		this.investigationId = investigationId;
	}

	public void setBpn(BPN bpn) {
		this.bpn = bpn;
	}

	public void setInvestigationStatus(InvestigationStatus investigationStatus) {
		this.investigationStatus = investigationStatus;
	}

	public void setInvestigationSide(InvestigationSide investigationSide) {
		this.investigationSide = investigationSide;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public void setAssetIds(List<String> assetIds) {
		this.assetIds = assetIds;
	}

	public void setNotifications(Map<String, Notification> notifications) {
		this.notifications = notifications;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public void setAcceptReason(String acceptReason) {
		this.acceptReason = acceptReason;
	}

	public void setDeclineReason(String declineReason) {
		this.declineReason = declineReason;
	}

	public List<Notification> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(List<Notification> notificationList) {
		this.notificationList = notificationList;
	}
}
