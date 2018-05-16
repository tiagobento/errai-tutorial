package org.jboss.errai.demo.client.local.contact;

import java.util.ArrayList;
import java.util.Arrays;

import org.jboss.errai.demo.client.local.contact.ContactListPageController.State.ModalState;
import org.jboss.errai.demo.client.shared.Contact;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static org.jboss.errai.demo.client.local.contact.ContactListPageController.State;
import static org.junit.Assert.assertEquals;

public class ContactListPageControllerTest {

    private final Promises promises = new SyncPromises();

    private ContactListPageController controller;

    @Before
    public void before() {
        controller = new ContactListPageController(mockStore(promises), null, null, null, null, null, promises);
    }

    @Test
    public void testAdd() {

        controller.store.setState(i -> new ContactListPageStore.StateImpl(
                emptyList(),
                ModalState.HIDDEN,
                null));

        controller.add(new Contact()).then(state -> {

            assertEquals(1, state.contacts().size());
            assertEquals(empty(), state.selectedContact());
            assertEquals(ModalState.HIDDEN, state.modalState());

            return promises.resolve();
        });
    }

    @Test
    public void testDelete() {

        final Contact contact = new Contact();
        contact.setId(1L);

        controller.store.setState(i -> new ContactListPageStore.StateImpl(
                new ArrayList<>(Arrays.asList(new Contact(), contact)),
                ModalState.HIDDEN,
                null));

        controller.remove(contact).then(state -> {

            assertEquals(1, state.contacts().size());
            assertEquals(empty(), state.selectedContact());
            assertEquals(ModalState.HIDDEN, state.modalState());

            return promises.resolve();
        });
    }

    @Test
    public void testOnRemoteDelete_hit() {

        final Contact contact1 = new Contact();
        contact1.setId(1L);

        final Contact contact2 = new Contact();
        contact2.setId(2L);

        controller.store.setState(i -> new ContactListPageStore.StateImpl(
                new ArrayList<>(Arrays.asList(contact1, contact2)),
                ModalState.HIDDEN,
                null));

        controller.onRemoteDelete(1L).then(state -> {
            assertEquals(singletonList(contact2), state.contacts());
            assertEquals(empty(), state.selectedContact());
            assertEquals(ModalState.HIDDEN, state.modalState());
            return promises.resolve();
        });
    }

    @Test
    public void testOnRemoteDelete_miss() {

        controller.store.setState(i -> new ContactListPageStore.StateImpl(
                emptyList(),
                ModalState.HIDDEN,
                null));

        controller.onRemoteDelete(1L).then(state -> {
            assertEquals(emptyList(), state.contacts());
            assertEquals(empty(), state.selectedContact());
            assertEquals(ModalState.HIDDEN, state.modalState());
            return promises.resolve();
        });
    }

    private static ContactListPageStore mockStore(final Promises p) {
        return new ContactListPageStore() {
            {
                this.promises = p;
            }

            @Override
            protected void render(ContactListPage view,
                                  NoProps props,
                                  State state) {
                //No-op
            }
        };
    }
}