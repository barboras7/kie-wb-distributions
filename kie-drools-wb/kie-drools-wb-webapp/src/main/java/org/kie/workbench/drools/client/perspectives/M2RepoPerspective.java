/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kie.workbench.drools.client.perspectives;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.guvnor.m2repo.client.event.M2RepoSearchEvent;
import org.kie.workbench.common.widgets.client.search.ContextualSearch;
import org.kie.workbench.common.widgets.client.search.SearchBehavior;
import org.kie.workbench.common.workbench.client.PerspectiveIds;
import org.kie.workbench.drools.client.resources.i18n.AppConstants;
import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.client.workbench.panels.impl.MultiListWorkbenchPanelPresenter;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.PartDefinitionImpl;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;

/**
 * A Perspective to show M2_REPO related screen
 */
@Dependent
@WorkbenchPerspective(identifier = PerspectiveIds.GUVNOR_M2REPO, isDefault = false)
public class M2RepoPerspective {

    @Inject
    private ContextualSearch contextualSearch;

    @Inject
    private Event<M2RepoSearchEvent> searchEvents;

    @PostConstruct
    private void init() {
        contextualSearch.setPerspectiveSearchBehavior(PerspectiveIds.GUVNOR_M2REPO, new SearchBehavior() {
            @Override
            public void execute(String searchFilter) {
                searchEvents.fire(new M2RepoSearchEvent(searchFilter));
            }

        });
    }

    @Perspective
    public PerspectiveDefinition getPerspective() {
        final PerspectiveDefinition p = new PerspectiveDefinitionImpl(MultiListWorkbenchPanelPresenter.class.getName());
        p.setName(PerspectiveIds.GUVNOR_M2REPO);
        p.getRoot().addPart(new PartDefinitionImpl(new DefaultPlaceRequest("M2RepoEditor")));
        return p;
    }

    @WorkbenchPartTitle
    public String getTitleText() {
        return AppConstants.INSTANCE.artifactRepository();
    }
}