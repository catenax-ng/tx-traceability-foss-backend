package org.eclipse.tractusx.traceability.testdata;

import org.eclipse.tractusx.traceability.investigations.domain.model.AffectedPart;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationStatus;
import org.eclipse.tractusx.traceability.investigations.domain.model.Notification;

import java.util.List;

public class NotificationTestDataFactory {

	public static Notification createNotificationTestData() {
		List<AffectedPart> affectedParts = List.of(new AffectedPart("partId"));
		return new Notification(
			"123",
			"id123",
			"senderBPN",
			"recipientBPN",
			"senderAddress",
			"agreement",
			"information",
			InvestigationStatus.RECEIVED,
			affectedParts
		);
	}
}
