package org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.validators;

import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.model.EDCNotification;
import org.eclipse.tractusx.traceability.investigations.domain.model.exception.InvestigationReceiverBpnMismatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EDCNotificationValidatorTest {


	@Mock
	TraceabilityProperties traceabilityProperties;

	@Mock
	ConstraintValidatorContext context;

	@Mock
	EDCNotification edcNotification;

	@InjectMocks
	EDCNotificationValidator validator;

	@Test
	public void testIsValidWithNullEDCNotification() {
		// Given
		EDCNotification edcNotification = null;

		// When
		boolean result = validator.isValid(edcNotification, context);

		// Then
		assertTrue(result);

	}

	@Test
	public void testIsValidWithValidEDCNotification() {
		// Given
		when(traceabilityProperties.getBpn()).thenReturn(BPN.of("BPN_OF_APPLICATION"));
		when(edcNotification.getSenderBPN()).thenReturn("BPN_OF_SENDER");

		// When
		// Then
		assertThrows(InvestigationReceiverBpnMismatchException.class, () -> {
			validator.isValid(edcNotification, context);
		});
	}

	@Test
	public void testIsValidWithInvalidEDCNotification() {
		// Given
		when(traceabilityProperties.getBpn()).thenReturn(BPN.of("BPN_OF_APPLICATION"));
		when(edcNotification.getSenderBPN()).thenReturn("BPN_OF_APPLICATION");

		// When
		boolean result = validator.isValid(edcNotification, context);

		// Then
		assertTrue(result);
	}

}


