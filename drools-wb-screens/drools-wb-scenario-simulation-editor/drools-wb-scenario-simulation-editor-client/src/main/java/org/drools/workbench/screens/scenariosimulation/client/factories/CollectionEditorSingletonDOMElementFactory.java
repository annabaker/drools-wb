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

package org.drools.workbench.screens.scenariosimulation.client.factories;

import java.util.HashMap;
import java.util.Optional;
import java.util.SortedMap;

import org.drools.workbench.screens.scenariosimulation.client.collectioneditor.CollectionViewImpl;
import org.drools.workbench.screens.scenariosimulation.client.commands.ScenarioSimulationContext;
import org.drools.workbench.screens.scenariosimulation.client.domelements.CollectionEditorDOMElement;
import org.drools.workbench.screens.scenariosimulation.client.models.ScenarioGridModel;
import org.drools.workbench.screens.scenariosimulation.client.utils.ScenarioSimulationUtils;
import org.drools.workbench.screens.scenariosimulation.client.utils.ViewsProvider;
import org.drools.workbench.screens.scenariosimulation.client.widgets.ScenarioGrid;
import org.drools.workbench.screens.scenariosimulation.model.FactMapping;
import org.drools.workbench.screens.scenariosimulation.model.typedescriptor.FactModelTree;
import org.uberfire.ext.wires.core.grids.client.model.GridColumn;
import org.uberfire.ext.wires.core.grids.client.model.GridData;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.impl.BaseSingletonDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.grid.GridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.GridLienzoPanel;

public class CollectionEditorSingletonDOMElementFactory extends BaseSingletonDOMElementFactory<String, CollectionViewImpl, CollectionEditorDOMElement> {

    protected ViewsProvider viewsProvider;

    protected ScenarioSimulationContext scenarioSimulationContext;

    public CollectionEditorSingletonDOMElementFactory(final GridLienzoPanel gridPanel,
                                                      final GridLayer gridLayer,
                                                      final GridWidget gridWidget,
                                                      ScenarioSimulationContext scenarioSimulationContext, ViewsProvider viewsProvider) {
        super(gridPanel,
              gridLayer,
              gridWidget);
        this.scenarioSimulationContext = scenarioSimulationContext;
        this.viewsProvider = viewsProvider;
    }

    @Override
    public CollectionViewImpl createWidget() {
        return (CollectionViewImpl) viewsProvider.getCollectionEditorView();
    }

    @Override
    public CollectionEditorDOMElement createDomElement(final GridLayer gridLayer,
                                                       final GridWidget gridWidget,
                                                       final GridBodyCellRenderContext context) {
        this.widget = createWidget();
        final ScenarioGridModel model = ((ScenarioGrid) gridWidget).getModel();
        final GridData.SelectedCell selectedCellsOrigin = model.getSelectedCellsOrigin();
        final Optional<GridColumn<?>> selectedColumn = model.getColumns().stream().filter(col -> col.getIndex() ==
                selectedCellsOrigin.getColumnIndex())
                .findFirst();
        selectedColumn.ifPresent(col -> {
            int actualIndex = model.getColumns().indexOf(col);
            final FactMapping factMapping = model.getSimulation().get().getSimulationDescriptor().getFactMappingByIndex(actualIndex);
            setCollectionEditorStructureData(this.widget, factMapping);
            String key = factMapping.getFactAlias() + "#" + factMapping.getExpressionAlias();
            this.e = internalCreateDomElement(widget, gridLayer, gridWidget, key);
            widget.addCloseCompositeEventHandler(event -> {
                destroyResources();
                gridLayer.batch();
                gridPanel.setFocus(true);
            });
            widget.addSaveEditorEventHandler(event -> flush());
        });
        return e;
    }

    @Override
    public void destroyResources() {
        if (e != null) {
            e.detach();
            widget = null;
            e = null;
        }
    }

    @Override
    protected String getValue() {
        return widget != null ? widget.getValue() : null;
    }

    protected void setCollectionEditorStructureData(CollectionViewImpl collectionEditorView, FactMapping factMapping) {
        String propertyClass = factMapping.getClassName();
        String className = factMapping.getFactAlias();
        String propertyName = factMapping.getExpressionAlias();
        String genericType = factMapping.getGenericType();
        String key = className + "#" + propertyName;
        final SortedMap<String, FactModelTree> dataObjectFieldsMap = scenarioSimulationContext.getDataObjectFieldsMap();
        final FactModelTree genericFactModelTree = dataObjectFieldsMap.get(genericType);
        if (ScenarioSimulationUtils.isList(propertyClass)) {
            collectionEditorView.setListWidget(true);
            collectionEditorView.initListStructure(key, genericFactModelTree.getSimpleProperties());
        } else {
            collectionEditorView.setListWidget(false);
            final HashMap<String, String> keyPropertyMap = new HashMap<>();
            keyPropertyMap.put("value", String.class.getName());
            collectionEditorView.initMapStructure(key, keyPropertyMap, genericFactModelTree.getSimpleProperties());
        }
    }

    protected CollectionEditorDOMElement internalCreateDomElement(CollectionViewImpl collectionEditorView, GridLayer gridLayer, GridWidget gridWidget, String key) {
        return new CollectionEditorDOMElement(collectionEditorView, gridLayer, gridWidget, key);
    }
}

