package com.dwkim.android.gomtalk;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
import android.test.mock.MockCursor;

import com.dwkim.android.gomtalk.model.ConversationListModel;
import com.dwkim.android.gomtalk.model.ModelCallback;
import com.dwkim.android.gomtalk.provider.GomTalkProviderContract;
import com.dwkim.android.gomtalk.ui.conversationlist.ConversationListContract;
import com.dwkim.android.gomtalk.ui.conversationlist.ConversationListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class ConversationListTest {

    @Mock
    GomTalkProviderContract mProviderContract;

    @Mock
    ConversationListModel mMockModel;

    @Mock
    ConversationListContract.View mMockView;

    @Captor
    private ArgumentCaptor<ModelCallback> mModelCallbackCaptor;

    private ConversationListPresenter mConversationListPresenter;
    private MatrixCursor mConversationListCursor;

    @Before
    public void setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mConversationListPresenter = new ConversationListPresenter(mMockModel, mMockView);

        mConversationListCursor = new MatrixCursor(new String[]{"_id","snippet"});
        mConversationListCursor.addRow(new Object[]{"1","abcd"});
    }

    @Test
    public void loadConversationList() {
        mConversationListPresenter.loadConversationList();

        verify(mMockModel).queryConversationList(mModelCallbackCaptor.capture());
        mModelCallbackCaptor.getValue().onComplete(ModelCallback.RESULT_OK, 0, mConversationListCursor);

        verify(mMockView).showConversationList(mConversationListCursor);
//        verify(mMockView).showConversationList(any(Cursor.class));
    }
}