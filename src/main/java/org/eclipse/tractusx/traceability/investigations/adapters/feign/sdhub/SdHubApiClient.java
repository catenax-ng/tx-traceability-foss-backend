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

package org.eclipse.tractusx.traceability.investigations.adapters.feign.sdhub;

import feign.Param;
import feign.RequestLine;
import org.eclipse.tractusx.traceability.assets.infrastructure.config.openapi.CatenaApiConfig;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(
	name = "sdHubApi",
	url = "${feign.sdHubApi.url}",
	configuration = {CatenaApiConfig.class}
)
public interface SdHubApiClient {

	@RequestLine("GET /selfdescription/by-params")
	GetSdHubResponse getSelfDescriptions(
		@Param(value = "id") List<String> ids,
		@Param(value = "companyNumbers") List<String> companyNumbers,
		@Param(value = "headquarterCountries") List<String> headquarterCountries,
		@Param(value = "legalCountries") List<String> legalCountries,
		@Param(value = "serviceProviders") List<String> serviceProviders,
		@Param(value = "sdTypes") List<String> sdTypes,
		@Param(value = "bpns") List<String> bpns
	);
}
