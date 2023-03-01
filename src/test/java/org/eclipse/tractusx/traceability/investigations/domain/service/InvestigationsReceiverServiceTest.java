package org.eclipse.tractusx.traceability.investigations.domain.service;

import org.eclipse.tractusx.traceability.common.mapper.InvestigationMapper;
import org.eclipse.tractusx.traceability.common.mapper.NotificationMapper;
import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.model.EDCNotification;
import org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.model.EDCNotificationFactory;
import org.eclipse.tractusx.traceability.investigations.domain.model.AffectedPart;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationStatus;
import org.eclipse.tractusx.traceability.investigations.domain.model.Notification;
import org.eclipse.tractusx.traceability.investigations.domain.ports.InvestigationsRepository;
import org.eclipse.tractusx.traceability.testdata.NotificationTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class InvestigationsReceiverServiceTest {

	@Mock
	private InvestigationsRepository mockRepository;

	@Mock
	private InvestigationsReadService mockReadService;

	@Mock
	private NotificationMapper mockNotificationMapper;

	@Mock
	private InvestigationMapper mockInvestigationMapper;

	@Mock
	private TraceabilityProperties mockTraceabilityProperties;

	@Mock
	private NotificationsService mockNotificationsService;

	@InjectMocks
	private InvestigationsReceiverService service;


	@Test
	@DisplayName("Test handleNotificationReceiverCallback when notification is invalid")
	void testHandleNotificationReceiverCallbackInvalidNotification() {

		// Given
		List<AffectedPart> affectedParts = List.of(new AffectedPart("partId"));
		Notification notification = new Notification(
			"123",
			"id123",
			"senderBPN",
			"recipientBPN",
			"senderAddress",
			"agreement",
			"information",
			InvestigationStatus.CLOSED,
			affectedParts
		);

		EDCNotification edcNotification = EDCNotificationFactory.createQualityInvestigation(
			"it", notification);

		// When
		service.handleNotificationReceiverCallback(edcNotification);
		// Then

	}

	@Test
	@DisplayName("Test handleNotificationReceiverCallback when notification status is SENT")
	void testHandleNotificationReceiverCallbackSent() {
	/*	EDCNotification notification = new EDCNotification();
		notification.setNotificationId(123L);
		notification.setRecipientBPN("test_bpn");
		notification.setInformation("test_info");
		notification.setInvestigationStatus(InvestigationStatus.SENT.toString());
		notification.setNotificationType(NotificationType.QMINVESTIGATION.toString());

		Notification mockNotification = new Notification();
		when(mockNotificationMapper.toReceiverNotification(any(EDCNotification.class))).thenReturn(mockNotification);

		Investigation mockInvestigation = new Investigation();
		when(mockInvestigationMapper.toReceiverInvestigation(any(BPN.class), any(String.class), any(Notification.class)))
			.thenReturn(mockInvestigation);

		service.handleNotificationReceiverCallback(notification);

		verify(mockRepository, times(1)).save(mockInvestigation);*/
	}

}
