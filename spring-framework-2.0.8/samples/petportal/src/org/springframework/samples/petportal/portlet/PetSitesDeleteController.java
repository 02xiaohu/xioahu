/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petportal.portlet;

import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.samples.petportal.domain.PetSite;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.Controller;

/**
 * This Controller deletes a {@link PetSite PetSite} from the map of 
 * pet-related websites. It is used by the 'petsites' portlet when
 * in EDIT mode and the <i>action</i> parameter is set to 'delete'.
 * See 'WEB-INF/context/petsites-portlet.xml' for details.
 * 
 * @author Mark Fisher
 */
public class PetSitesDeleteController implements Controller {

	private Properties petSites;
	
	public void setPetSites(Properties petSites) {
		this.petSites = petSites;
	}
	
	public void handleActionRequest(ActionRequest request, ActionResponse response) throws Exception {
		String site = PortletRequestUtils.getStringParameter(request, "site");
		if (site != null) {
			petSites.remove(site);
		}
	}

	public ModelAndView handleRenderRequest(RenderRequest arg0, RenderResponse arg1) throws Exception {
		return new ModelAndView("petSitesEdit", "petSites", petSites);
	}

}
