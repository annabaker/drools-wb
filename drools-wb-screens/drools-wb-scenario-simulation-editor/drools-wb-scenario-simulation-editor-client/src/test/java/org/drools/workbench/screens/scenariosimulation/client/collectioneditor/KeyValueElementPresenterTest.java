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
package org.drools.workbench.screens.scenariosimulation.client.collectioneditor;

import java.util.Collections;
import java.util.Map;

import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.UListElement;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class KeyValueElementPresenterTest extends ElementPresenterTest<KeyValueElementView, KeyValueElementView.Presenter> {

    private static final String TEST_ITEM_ID = "TEST-ITEM-ID";


    private Map<String, String> testKeyPropertyMap = Collections.singletonMap("TEST-KEY1", "TEST-KEY2");
    private Map<String, String> testValuePropertyyMap = Collections.singletonMap("TEST-VALUE1", "TEST-VALUE2");

    @Mock
    private LIElement propertyFieldsMock;

    @Mock
    private UListElement keyContainerMock;

    @Mock
    private UListElement valueContainerMock;

    @Mock
    private LIElement keyLabelMock;

    @Mock
    private LIElement valueLabelMock;


    @Before
    public void setup() {
        elementView1Mock = mock(KeyValueElementView.class);
        elementView2Mock = mock(KeyValueElementView.class);
        when(elementView1Mock.getKeyContainer()).thenReturn(keyContainerMock);
        when(elementView2Mock.getKeyContainer()).thenReturn(keyContainerMock);
        when(elementView1Mock.getValueContainer()).thenReturn(valueContainerMock);
        when(elementView2Mock.getValueContainer()).thenReturn(valueContainerMock);
        when(elementView1Mock.getItemId()).thenReturn(ELEMENT1_ID);
        when(elementView2Mock.getItemId()).thenReturn(ELEMENT2_ID);
        when(keyLabelMock.getStyle()).thenReturn(styleMock);
        when(valueLabelMock.getStyle()).thenReturn(styleMock);
        when(elementView1Mock.getKeyLabel()).thenReturn(keyLabelMock);
        when(elementView2Mock.getKeyLabel()).thenReturn(keyLabelMock);
        when(elementView1Mock.getValueLabel()).thenReturn(valueLabelMock);
        when(elementView2Mock.getValueLabel()).thenReturn(valueLabelMock);
        super.setup();
        when(viewsProviderMock.getKeyValueElementView()).thenReturn(elementView1Mock);
        when(propertyPresenterMock.getPropertyFields(anyString(), anyString(), anyString())).thenReturn(propertyFieldsMock);
        elementPresenter = spy(new KeyValueElementPresenter() {
            {
                this.viewsProvider = viewsProviderMock;
                this.propertyPresenter = propertyPresenterMock;
                this.elementViewList = elementViewListLocal;
            }
        });
    }

    @Test
    public void getKeyValueContainer() {
        elementViewListLocal.clear();
        elementPresenter.getKeyValueContainer(TEST_ITEM_ID, testKeyPropertyMap, testValuePropertyyMap);
        verify(elementView1Mock, times(1)).init(elementPresenter);
        verify(elementView1Mock, times(1)).setItemId(TEST_ITEM_ID);
        verify(elementView1Mock, times(1)).getKeyContainer();
        verify(keyContainerMock, times(1)).appendChild(propertyFieldsMock);
        verify(elementView1Mock, times(1)).getValueContainer();
        verify(valueContainerMock, times(1)).appendChild(propertyFieldsMock);
        assertNotNull(itemContainerMock);
        assertTrue(elementViewListLocal.contains(elementView1Mock));
    }

    @Test
    public void onEditItem() {
        elementPresenter.onEditItem(elementView1Mock);
        verify(propertyPresenterMock, times(2)).editProperties(anyString());
        verify(styleMock, times(1)).setVisibility(Style.Visibility.VISIBLE);
    }

    @Test
    public void updateItem() {
        elementPresenter.updateItem(elementView1Mock);
        verify(propertyPresenterMock, times(2)).updateProperties(anyString());
        verify(styleMock, times(1)).setVisibility(Style.Visibility.HIDDEN);
    }

}
