/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.testscenario.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import org.drools.workbench.models.testscenarios.shared.ExecutionTrace;
import org.drools.workbench.models.testscenarios.shared.Scenario;
import org.drools.workbench.screens.testscenario.client.resources.i18n.TestScenarioConstants;
import org.gwtbootstrap3.client.ui.Button;

public class AddExecuteButton extends Button {

    public AddExecuteButton( final Scenario scenario,
                             final ScenarioParentWidget parent ) {
        super( TestScenarioConstants.INSTANCE.MoreDotDot() );

        setTitle( TestScenarioConstants.INSTANCE.AddAnotherSectionOfDataAndExpectations() );

        addClickHandler( new ClickHandler() {

            public void onClick( ClickEvent event ) {
                scenario.getFixtures().add( new ExecutionTrace() );
                parent.renderEditor();
            }
        } );
    }
}
