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

import java.util.Map;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import org.drools.workbench.screens.scenariosimulation.client.collectioneditor.CollectionView;
import org.uberfire.client.mvp.HasPresenter;

public interface KeyValueEditingBox extends HasPresenter<KeyValueEditingBox.Presenter> {

    interface Presenter {

        /**
         * @param key The key representing the property, i.e Classname#propertyname (e.g Author#books)
         * @param keyPropertyMap
         * @param valuePropertyMap
         */
        DivElement getEditingBox(String key, Map<String, String> keyPropertyMap, Map<String, String> valuePropertyMap);

        void save(String key);

        void close(KeyValueEditingBox toClose);

        void setCollectionEditorPresenter(CollectionView.Presenter collectionEditorPresenter);
    }

    /**
     * @param key The key representing the property, i.e Classname#propertyname (e.g Author#books)
     */
    void setKey(String key);

    DivElement getEditingBox();

    HeadingElement getEditingBoxTitle();

    UListElement getPropertiesContainer();

    LIElement getKeyValueContainer();

    UListElement getKeyContainer();

    UListElement getValueContainer();
}
