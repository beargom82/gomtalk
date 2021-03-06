package com.dwkim.android.gomtalk;

import android.database.Cursor;
import android.database.MatrixCursor;

import com.dwkim.android.gomtalk.model.ConversationListModel;
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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConversationListTest {

    @Mock
    GomTalkProviderContract mProviderContract;

    @Mock
    ConversationListModel mMockModel;

    @Mock
    ConversationListContract.View mMockView;

    @Captor
    private ArgumentCaptor<ConversationListModel.ConversationListLoadCallback> mConvLoadCallbackCaptor;

    private ConversationListPresenter mConversationListPresenter;
    private MatrixCursor mConversationListCursor;

    @Before
    public void setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mConversationListPresenter = spy(new ConversationListPresenter(mMockModel, mMockView));

        mConversationListCursor = new MatrixCursor(new String[]{"_id","snippet"});
        mConversationListCursor.addRow(new Object[]{"1","abcd"});

    }

    @Test
    public void start() {
        mConversationListPresenter.start();

        verify(mMockModel).loadConversationList(mConvLoadCallbackCaptor.capture());
        mConvLoadCallbackCaptor.getValue().onConversationListLoaded(mConversationListCursor);

        verify(mMockView).showConversationList(any(Cursor.class));
    }

    @Test
    public void loadConversationList() {
        mConversationListPresenter.loadConversationList();

        verify(mMockModel).loadConversationList(mConvLoadCallbackCaptor.capture());
        mConvLoadCallbackCaptor.getValue().onConversationListLoaded(mConversationListCursor);

        verify(mMockView).showConversationList(any(Cursor.class));
    }

    @Test
    public void openConversation() {
        mConversationListPresenter.openConversation(10l);

        verify(mMockView).openConversation(10L);
    }

    /* old codes
    //when(mMessageStorage.queryConversationList()).thenReturn(mConversationListCursor);

//    public void loadConversationList_old() {
//
//        mConversationListPresenter.loadConversationList();
//        mMockModel.onConversationListLoaded(mMessageStorage.queryConversationList());
//
//        verify(mMockModel).loadConversationList();
//        verify(mMockView).showConversationList(any(Cursor.class));
//    }

     */
}