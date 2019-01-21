/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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
package org.drools.workbench.screens.scenariosimulation.client.collectioneditor.editingbox;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.gwt.dom.client.DivElement;
import org.drools.workbench.screens.scenariosimulation.client.collectioneditor.CollectionView;
import org.drools.workbench.screens.scenariosimulation.client.utils.ViewsProvider;

public class KeyValueEditingBoxPresenter implements KeyValueEditingBox.Presenter {

    @Inject
    protected ViewsProvider viewsProvider;

    @Inject
    protected PropertyEditingElementPresenter propertyEditingElementPresenter;

    protected CollectionView.Presenter collectionEditorPresenter;

    @Override
    public void setCollectionEditorPresenter(CollectionView.Presenter collectionEditorPresenter) {
        this.collectionEditorPresenter = collectionEditorPresenter;
    }

    protected Map<String, String> keyPropertyMap;

    protected Map<String, String> valuePropertyMap;

    @Override
    public DivElement getEditingBox(String key, Map<String, String> keyPropertyMap, Map<String, String> valuePropertyMap) {
        String propertyName = key.substring(key.lastIndexOf("#") + 1);
        this.keyPropertyMap = keyPropertyMap;
        this.valuePropertyMap = valuePropertyMap;
        final KeyValueEditingBox mapEditingBox = viewsProvider.getKeyValueEditingBox();
        mapEditingBox.init(this);
        mapEditingBox.setKey(key);
        mapEditingBox.getEditingBoxTitle().setInnerText("Edit " + propertyName);
        keyPropertyMap.forEach((propertyKey, value) -> mapEditingBox.getKeyContainer().appendChild(propertyEditingElementPresenter.getPropertyContainer(propertyKey))
        );
        valuePropertyMap.forEach((propertyKey, value) -> mapEditingBox.getValueContainer().appendChild(propertyEditingElementPresenter.getPropertyContainer(propertyKey)));
        return mapEditingBox.getEditingBox();
    }

    @Override
    public void save(String key) {
        Map<String, String> keyPropertiesValues = new HashMap<>();
        Map<String, String> valuePropertiesMap = new HashMap<>();
        keyPropertyMap.keySet().forEach(propertyKey -> keyPropertiesValues.put(propertyKey, propertyEditingElementPresenter.getPropertyValue(propertyKey)));
        valuePropertyMap.keySet().forEach(propertyKey -> valuePropertiesMap.put(propertyKey, propertyEditingElementPresenter.getPropertyValue(propertyKey)));
        collectionEditorPresenter.addMapItem(key, keyPropertiesValues, valuePropertiesMap);
    }

    @Override
    public void close(KeyValueEditingBox toClose) {
        toClose.getEditingBox().removeFromParent();
    }
}
