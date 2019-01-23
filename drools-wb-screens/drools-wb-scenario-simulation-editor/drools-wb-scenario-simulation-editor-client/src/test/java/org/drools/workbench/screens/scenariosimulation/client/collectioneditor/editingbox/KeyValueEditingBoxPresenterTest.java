package org.drools.workbench.screens.scenariosimulation.client.collectioneditor.editingbox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.screens.scenariosimulation.client.collectioneditor.CollectionView;
import org.drools.workbench.screens.scenariosimulation.client.collectioneditor.PropertyPresenter;
import org.drools.workbench.screens.scenariosimulation.client.utils.ViewsProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class KeyValueEditingBoxPresenterTest {

    private static final String TEST_KEY = "TEST-CLASSNAME#TEST-PROPERTYNAME";
    private static final String TEST_PROPERTYNAME = "TEST-PROPERTYNAME";

    private Map<String, String> testKeyPropertyMap = Collections.singletonMap("TEST-KEY1", "TEST-KEY2");
    private Map<String, String> testValuePropertyyMap = Collections.singletonMap("TEST-VALUE1", "TEST-VALUE2");

    @Mock
    private PropertyPresenter propertyPresenterMock;

    @Mock
    private CollectionView.Presenter collectionViewPresenterMock;

    @Mock
    private ViewsProvider viewsProviderMock;

    @Mock
    private KeyValueEditingBox keyValueEditingBoxMock;

    @Mock
    private LIElement editingBoxMock;

    @Mock
    private HeadingElement editingBoxTitleMock;

    @Mock
    private UListElement keyContainerMock;

    @Mock
    private UListElement valueContainerMock;

    @Mock
    private LIElement propertyFieldsMock;

    private KeyValueEditingBoxPresenter keyValueEditingBoxPresenter;

    @Before
    public void setup() {
        when(viewsProviderMock.getKeyValueEditingBox()).thenReturn(keyValueEditingBoxMock);
        when(keyValueEditingBoxMock.getEditingBoxTitle()).thenReturn(editingBoxTitleMock);
        when(keyValueEditingBoxMock.getKeyContainer()).thenReturn(keyContainerMock);
        when(propertyPresenterMock.getEditingPropertyFields(anyString(), anyString(), anyString())).thenReturn(propertyFieldsMock);
        when(keyValueEditingBoxMock.getValueContainer()).thenReturn(valueContainerMock);
        when(keyValueEditingBoxMock.getEditingBox()).thenReturn(editingBoxMock);
        this.keyValueEditingBoxPresenter = spy(new KeyValueEditingBoxPresenter() {
            {
                this.viewsProvider = viewsProviderMock;
                this.propertyPresenter = propertyPresenterMock;
                this.collectionEditorPresenter = collectionViewPresenterMock;
            }
        });
    }

    @Test
    public void getEditingBox() {
        editingBoxMock = keyValueEditingBoxPresenter.getEditingBox(TEST_KEY, testKeyPropertyMap, testValuePropertyyMap);
        verify(viewsProviderMock, times(1)).getKeyValueEditingBox();
        verify(keyValueEditingBoxMock, times(1)).init(keyValueEditingBoxPresenter);
        verify(keyValueEditingBoxMock, times(1)).setKey(TEST_KEY);
        verify(editingBoxTitleMock, times(1)).setInnerText("Edit " + TEST_PROPERTYNAME);
        verify(keyContainerMock, times(1)).appendChild(propertyFieldsMock);
        verify(valueContainerMock, times(1)).appendChild(propertyFieldsMock);
        verify(keyValueEditingBoxMock, times(1)).getEditingBox();
        assertNotNull(editingBoxMock);
    }

    @Test
    public void save() {
        keyValueEditingBoxPresenter.save();
        verify(propertyPresenterMock, times(1)).updateProperties("key");
        verify(propertyPresenterMock, times(1)).updateProperties("value");
        verify(collectionViewPresenterMock, times(1)).addMapItem(anyMap(), anyMap());
    }

    @Test
    public void close() {
        keyValueEditingBoxPresenter.close(keyValueEditingBoxMock);
        verify(editingBoxMock, times(1)).removeFromParent();
    }
}