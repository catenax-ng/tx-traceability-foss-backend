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

package org.eclipse.tractusx.traceability.assets.infrastructure.adapters.rest.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.eclipse.tractusx.traceability.assets.application.AssetFacade;
import org.eclipse.tractusx.traceability.assets.domain.model.Asset;
import org.eclipse.tractusx.traceability.assets.domain.model.PagedAsset;
import org.eclipse.tractusx.traceability.assets.domain.ports.AssetRepository;
import org.eclipse.tractusx.traceability.common.model.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_USER')")
@Tag(name = "Assets")
public class AssetsController {

	private final AssetRepository assetRepository;
	private final AssetFacade assetFacade;

	public AssetsController(AssetRepository assetRepository, AssetFacade assetFacade) {
		this.assetRepository = assetRepository;
		this.assetFacade = assetFacade;
	}

	@Consumes("application/json")
	@Operation(operationId = "dashboard",
		summary = "Synchronizes assets from IRS",
		tags = {"Assets"},
		description = "The endpoint synchronizes the assets from irs.",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created."),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@PostMapping("/assets/sync")
	public void sync(@Valid @RequestBody SyncAssets syncAssets) {
		assetFacade.synchronizeAssetsAsync(syncAssets.globalAssetIds());
	}

	@Consumes("application/json")
	@Produces("application/json")
	@Operation(operationId = "Assets",
		summary = "Get assets by pagination",
		tags = {"Assets"},
		description = "The endpoint returns a paged result of assets.",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the paged result found",
		content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@GetMapping("/assets")
	public PageResult<Asset> assets(Pageable pageable) {
		return assetRepository.getAssets(pageable);
	}

	@Produces("application/json")
	@Operation(operationId = "Assets",
		summary = "Get supplier assets by pagination",
		tags = {"Assets"},
		description = "The endpoint returns a paged result of supplier assets.",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the paged result found",
		content = {@Content(mediaType = APPLICATION_JSON_VALUE,
			schema = @Schema(implementation = PagedAsset.class))}),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@GetMapping("/assets/supplier")
	public PageResult<Asset> supplierAssets(Pageable pageable) {
		return assetRepository.getSupplierAssets(pageable);
	}

	@Produces("application/json")
	@Operation(operationId = "Assets",
		summary = "Get own assets by pagination",
		tags = {"Assets"},
		description = "The endpoint returns a paged result of own assets.",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the paged result found",
		content = {@Content(mediaType = APPLICATION_JSON_VALUE,
			schema = @Schema(implementation = PagedAsset.class))}),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@GetMapping("/assets/my")
	public PageResult<Asset> ownAssets(Pageable pageable) {
		return assetRepository.getOwnAssets(pageable);
	}

	@Produces("application/json")
	@Operation(operationId = "Assets",
		summary = "Get map of assets",
		tags = {"Assets"},
		description = "The endpoint returns a map for assets consumed by the map.",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the assets found",
		content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@GetMapping("/assets/countries")
	public Map<String, Long> assetsCountryMap() {
		return assetFacade.getAssetsCountryMap();
	}

	@Produces("application/json")
	@Operation(operationId = "Assets",
		summary = "Get asset by id",
		tags = {"Assets"},
		description = "The endpoint returns an asset filtered by id .",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the assets found",
		content = {@Content(mediaType = APPLICATION_JSON_VALUE,
			schema = @Schema(implementation = Asset.class))}),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@GetMapping("/assets/{assetId}")
	public Asset asset(@PathVariable String assetId) {
		return assetRepository.getAssetById(assetId);
	}

	@Produces("application/json")
	@Operation(operationId = "Assets",
		summary = "Get asset by child id",
		tags = {"Assets"},
		description = "The endpoint returns an asset filtered by child id.",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the asset by childId",
		content = {@Content(mediaType = APPLICATION_JSON_VALUE,
			schema = @Schema(implementation = Asset.class))}),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@GetMapping("/assets/{assetId}/children/{childId}")
	public Asset asset(@PathVariable String assetId, @PathVariable String childId) {
		return assetRepository.getAssetByChildId(assetId, childId);
	}

	@Produces("application/json")
	@Consumes("application/json")
	@Operation(operationId = "Assets",
		summary = "Updates asset",
		tags = {"Assets"},
		description = "The endpoint updates asset by provided quality type.",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the updated asset",
		content = {@Content(mediaType = APPLICATION_JSON_VALUE,
			schema = @Schema(implementation = Asset.class))}),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@PatchMapping("/assets/{assetId}")
	public Asset updateAsset(@PathVariable String assetId, @Valid @RequestBody UpdateAsset updateAsset) {
		return assetFacade.updateAsset(assetId, updateAsset);
	}

	@Produces("application/json")
	@Consumes("application/json")
	@Operation(operationId = "Assets",
		summary = "Searches for assets by ids.",
		tags = {"Assets"},
		description = "The endpoint searchs for assets by id and returns a list of them.",
		security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns list of found assets",
		content = {@Content(mediaType = APPLICATION_JSON_VALUE,
			schema = @Schema(
				type = "array",
				implementation = Asset.class
			))}),
		@ApiResponse(responseCode = "401", description = "Authorization failed.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)
			}),
		@ApiResponse(responseCode = "403", description = "Forbidden.",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
	})
	@PostMapping("/assets/detail-information")
	public List<Asset> getDetailInformation(@Valid @RequestBody GetDetailInformationRequest getDetailInformationRequest) {
		return assetRepository.getAssetsById(getDetailInformationRequest.assetIds());
	}
}
