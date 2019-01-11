/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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
package org.drools.workbench.screens.scenariosimulation.client.commands.actualcommands;

import java.util.Optional;

import org.drools.core.factmodel.Fact;
import org.drools.workbench.screens.scenariosimulation.client.commands.ScenarioSimulationContext;
import org.drools.workbench.screens.scenariosimulation.client.metadata.ScenarioHeaderMetaData;
import org.drools.workbench.screens.scenariosimulation.client.resources.i18n.ScenarioSimulationEditorConstants;
import org.drools.workbench.screens.scenariosimulation.client.widgets.ScenarioGridColumn;
import org.drools.workbench.screens.scenariosimulation.model.FactIdentifier;
import org.drools.workbench.screens.scenariosimulation.model.ScenarioSimulationModel;

import static org.drools.workbench.screens.scenariosimulation.model.FactMapping.getPropertyPlaceHolder;

/**
 * <b>Abstract</b> <code>Command</code> class to provide common methods used by SetInstanceHeaderCommand and SetPropertyHeaderCommand implementations
 */
public abstract class AbstractSetHeaderCommand extends AbstractScenarioSimulationCommand {

    public AbstractSetHeaderCommand() {
        super(true);
    }

    protected void setInstanceHeader(ScenarioSimulationContext context) {
        final ScenarioSimulationContext.Status status = context.getStatus();
        if (getSelectedColumn(context).isPresent()) {
            ScenarioGridColumn selectedColumn = getSelectedColumn(context).get();
            int columnIndex = context.getModel().getColumns().indexOf(selectedColumn);
            String className = status.getClassName();
            String canonicalClassName = getFullPackage(context) + className;
            FactIdentifier factIdentifier = setEditableHeadersAndGetFactIdentifier(context, selectedColumn, className, canonicalClassName);
            setInstanceHeaderMetaData(selectedColumn, className, factIdentifier);
            final ScenarioHeaderMetaData propertyHeaderMetaData = selectedColumn.getPropertyHeaderMetaData();
            setPropertyMetaData(propertyHeaderMetaData, getPropertyPlaceHolder(columnIndex), false, selectedColumn, ScenarioSimulationEditorConstants.INSTANCE.defineValidType());
            context.getModel().updateColumnInstance(columnIndex, selectedColumn);
        }
        else {
            return;
        }

    }

    protected Optional<ScenarioGridColumn> getSelectedColumn(ScenarioSimulationContext context) {
        return Optional.ofNullable((ScenarioGridColumn) context.getModel().getSelectedColumn());
    }

    protected String getFullPackage(ScenarioSimulationContext context) {
        String fullPackage = context.getStatus().getFullPackage();
        if (!fullPackage.endsWith(".")) {
            fullPackage += ".";
        }
        return fullPackage;
    }

    protected FactIdentifier setEditableHeadersAndGetFactIdentifier(ScenarioSimulationContext context, ScenarioGridColumn selectedColumn, String className, String canonicalClassName) {
        final ScenarioSimulationModel.Type simulationModelType = context.getModel().getSimulation().get().getSimulationDescriptor().getType();
        selectedColumn.setEditableHeaders(!simulationModelType.equals(ScenarioSimulationModel.Type.DMN));
        String nameToUseForCreation = simulationModelType.equals(ScenarioSimulationModel.Type.DMN) ? className : selectedColumn.getInformationHeaderMetaData().getColumnId();
        return getFactIdentifierByColumnTitle(className, context).orElse(FactIdentifier.create(nameToUseForCreation, canonicalClassName));
    }

    protected void setInstanceHeaderMetaData(ScenarioGridColumn scenarioGridColumn, String className, FactIdentifier factIdentifier) {
        scenarioGridColumn.getInformationHeaderMetaData().setTitle(className);
        scenarioGridColumn.setInstanceAssigned(true);
        scenarioGridColumn.setFactIdentifier(factIdentifier);
    }

    protected void setPropertyMetaData(ScenarioHeaderMetaData propertyHeaderMetaData, String title, boolean readOnly, ScenarioGridColumn selectedColumn, String placeHolder) {
        propertyHeaderMetaData.setTitle(title);
        propertyHeaderMetaData.setReadOnly(readOnly);
        selectedColumn.setPlaceHolder(placeHolder);

    }
}
