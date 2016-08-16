/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.http;

import com.github.tomakehurst.wiremock.admin.AdminRoutes;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.stubbing.ServedStub;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;

import java.net.URI;

import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;
import static com.github.tomakehurst.wiremock.core.WireMockApp.ADMIN_CONTEXT_ROOT;

public class AdminRequestHandler extends AbstractRequestHandler {

    private final AdminRoutes adminRoutes;
    private final Admin admin;

	public AdminRequestHandler(AdminRoutes adminRoutes, Admin admin, ResponseRenderer responseRenderer) {
		super(responseRenderer);
        this.adminRoutes = adminRoutes;
        this.admin = admin;
	}

	@Override
	public ServedStub handleRequest(Request request) {
        notifier().info("Received request to " + request.getUrl() + " with body " + request.getBodyAsString());
        String path = URI.create(withoutAdminRoot(request.getUrl())).getPath();
        AdminTask adminTask = adminRoutes.taskFor(
            request.getMethod(),
            path
        );
        return ServedStub.exactMatch(LoggedRequest.createFrom(request), adminTask.execute(admin, request));
	}

	private static String withoutAdminRoot(String url) {
	    return url.replace(ADMIN_CONTEXT_ROOT, "");
	}
	
}
