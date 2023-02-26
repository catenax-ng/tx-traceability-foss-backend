package org.eclipse.tractusx.traceability.investigations.adapters.rest;

import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.eclipse.tractusx.traceability.investigations.adapters.rest.model.InvestigationData;
import org.eclipse.tractusx.traceability.investigations.adapters.rest.model.InvestigationReason;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationSide;
import org.eclipse.tractusx.traceability.investigations.domain.service.InvestigationsPublisherService;
import org.eclipse.tractusx.traceability.investigations.domain.service.InvestigationsReadService;
import org.eclipse.tractusx.traceability.investigations.domain.service.InvestigationsReceiverService;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(InvestigationsController.class)
class InvestigationsControllerTest {

	@MockBean
	private InvestigationsReadService investigationsReadService;
	@MockBean
	private InvestigationsPublisherService investigationsPublisherService;
	@MockBean
	private InvestigationsReceiverService investigationsReceiverService;
	@MockBean
	private TraceabilityProperties traceabilityProperties;

	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		this.objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		InvestigationsController investigationsController = new InvestigationsController(investigationsReadService,
			investigationsPublisherService,
			investigationsReceiverService,
			traceabilityProperties);
		this.mockMvc = MockMvcBuilders.standaloneSetup(investigationsController).build();
	}
	@Ignore("Temporarily disabled because WithMockUser is causing error while groovy test is on class path")
	//@WithMockUser(roles = {"ADMIN", "SUPERVISOR"})
	@Test
	public void getInvestigation() throws Exception {
		// Given
		InvestigationData investigationData = new InvestigationData(
			66L,
			"CREATED",
			"DescriptionText",
			"BPNL00000003AYRE",
			"2023-02-21T21:27:10.734950Z",
			List.of("urn:uuid:ceb6b964-5779-49c1-b5e9-0ee70528fcbd"),
			InvestigationSide.SENDER,
			new InvestigationReason("Reason", "Details", "decline"),
			"BPNL00000003AYRE"
		);

		when(investigationsReadService.findInvestigation(66L)).thenReturn(investigationData);
		String expected = objectMapper.writeValueAsString(investigationData);

		// When
		MvcResult result = mockMvc.perform(get("/investigations/{id}", 66)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		// Then
		String actual = result.getResponse().getContentAsString();
		JSONAssert.assertEquals(expected, actual, false);
	}
}




/*	@Test
	@WithMockUser(roles = {"ADMIN", "SUPERVISOR"})
	public void closeInvestigation_ReturnsNoContent() throws Exception {
		Long investigationId = 1L;
		CloseInvestigationRequest closeInvestigationRequest = new CloseInvestigationRequest("reason");

		mockMvc.perform(post("/{investigationId}/close", investigationId)
				.contentType(MediaType.APPLICATION_JSON)
				.body(objectMapper.writeValueAsString(closeInvestigationRequest)))
			.andExpect(status().isNoContent());*/

/*		verify(investigationsPublisherService)
			.closeInvestigation(eq(traceabilityProperties.getBpn()), eq(investigationId), eq(closeInvestigationRequest.reason()));*/


/*	@Test
	@WithMockUser
	public void closeInvestigation_ReturnsUnauthorized_WhenUserRoleIsNotAuthorized() throws Exception {
		Long investigationId = 1L;
		CloseInvestigationRequest closeInvestigationRequest = new CloseInvestigationRequest("reason");

		mockMvc.perform(post("/{investigationId}/close", investigationId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(closeInvestigationRequest)))
			.andExpect(status().isUnauthorized());
	}*/
