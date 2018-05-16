/*
 * Copyright (C) 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.demo.client.local.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.http.client.Response;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLAnchorElement;
import elemental2.promise.Promise;
import org.jboss.errai.bus.client.api.ClientMessageBus;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.demo.client.local.contact.ContactListPageController.State.ModalState;
import org.jboss.errai.demo.client.local.misc.Click;
import org.jboss.errai.demo.client.local.nav.NavBarViewController;
import org.jboss.errai.demo.client.local.reactive.model.RStore;
import org.jboss.errai.demo.client.local.reactive.model.RViewController;
import org.jboss.errai.demo.client.shared.Contact;
import org.jboss.errai.demo.client.shared.ContactOperation;
import org.jboss.errai.demo.client.shared.ContactStorageService;
import org.jboss.errai.demo.client.shared.Operation;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.jboss.errai.demo.client.local.contact.ContactListPageController.State.ModalState.HIDDEN;
import static org.jboss.errai.demo.client.local.contact.ContactListPageController.State.ModalState.SHOWING;
import static org.jboss.errai.demo.client.local.misc.Click.Type.DOUBLE;
import static org.jboss.errai.demo.client.local.misc.Click.Type.SINGLE;
import static org.jboss.errai.demo.client.shared.Operation.OperationType.CREATE;
import static org.jboss.errai.demo.client.shared.Operation.OperationType.DELETE;
import static org.jboss.errai.demo.client.shared.Operation.OperationType.UPDATE;

public class ContactListPageController implements RViewController<ContactListPage, RStore.NoProps> {

    final ContactListPageStore store;

    final NavBarViewController navBarViewController;

    final HTMLAnchorElement newContactAnchor;

    final HTMLAnchorElement sortContactsAnchor;

    final Caller<ContactStorageService> contactService;

    final ClientMessageBus bus;

    final Promises promises;

    @Inject
    public ContactListPageController(ContactListPageStore store,
                                     NavBarViewController navBarViewController,
                                     HTMLAnchorElement newContactAnchor,
                                     HTMLAnchorElement sortContactsAnchor,
                                     Caller<ContactStorageService> contactService,
                                     ClientMessageBus bus,
                                     Promises promises) {

        this.store = store;
        this.navBarViewController = navBarViewController;
        this.newContactAnchor = newContactAnchor;
        this.sortContactsAnchor = sortContactsAnchor;
        this.contactService = contactService;
        this.bus = bus;
        this.promises = promises;
    }

    @Override
    public void init(final ContactListPage view, final RStore.NoProps props) {

        //Init store
        store.init(view, props, new ContactListPageStore.StateImpl(
                new ArrayList<>(),
                ModalState.HIDDEN,
                null));

        //Fetch initial contacts
        promises.promisify(contactService, ContactStorageService::getAllContacts).then(this::setContacts);

        //Configure NavBar links
        newContactAnchor.href = ("javascript:void(0);");
        newContactAnchor.textContent = ("Create Contact");
        newContactAnchor.onclick = e -> {
            showNewContactModal();
            return null;
        };

        sortContactsAnchor.href = ("javascript:");
        sortContactsAnchor.textContent = ("Sort By Nickname");
        sortContactsAnchor.onclick = (e -> {
            sortByNickname();
            return null;
        });
    }

    public Promise<NavBarViewController.State> addNavBarButtons() {
        return navBarViewController.add(newContactAnchor).then(i -> {
            return navBarViewController.add(sortContactsAnchor);
        });
    }

    public Promise<NavBarViewController.State> removeNavBarButtons() {
        return navBarViewController.remove(newContactAnchor).then(i -> {
            return navBarViewController.remove(sortContactsAnchor);
        });
    }

    //Screen actions

    private Promise<State> sortByNickname() {
        return store.setState(previousState -> previousState
                .withContacts(previousState.contacts().stream()
                                      .sorted(comparing(contact -> contact.getNickname().toLowerCase()))
                                      .collect(toList())));
    }

    public Promise<State> onContactClicked(@Observes @Click(SINGLE) final Contact contact) {
        return store.setState(previousState -> previousState
                .withSelectedContact(contact));
    }

    public Promise<State> onContactDoubleClicked(@Observes @Click(DOUBLE) final Contact contact) {
        return store.setState(previousState -> previousState
                .withSelectedContact(contact)
                .withModalState(SHOWING));
    }

    //Remote events

    public Promise<State> onRemoteCreated(final @Observes @Operation(CREATE) ContactOperation contactOperation) {
        return store.setState(previousState -> {

            DomGlobal.console.info("REMOTE: Created " + contactOperation.getContact().getId());

            if (sourceIsThisClient(contactOperation)) {
                return previousState;
            }

            return previousState.withOneMoreContact(contactOperation.getContact());
        });
    }

    public Promise<State> onRemoteUpdated(final @Observes @Operation(UPDATE) ContactOperation contactOperation) {
        return store.setState(previousState -> {

            if (sourceIsThisClient(contactOperation)) {
                return previousState;
            }

            //FIXME: Implement list updates (probably add the key mechanism to the RListView class)
            return previousState;
        });
    }

    public Promise<State> onRemoteDelete(final @Observes @Operation(DELETE) Long id) {

        return store.setState(previousState -> {
            final Optional<Contact> contact = findContact(id, previousState.contacts());

            if (!contact.isPresent()) {
                return previousState;
            }

            return previousState.withoutContact(contact.get());
        });
    }

    private boolean sourceIsThisClient(final ContactOperation contactOperation) {
        return contactOperation.getSourceQueueSessionId() != null &&
                contactOperation.getSourceQueueSessionId().equals(bus.getSessionId());
    }

    private Optional<Contact> findContact(final Long id,
                                          final List<Contact> contacts) {

        return contacts.stream()
                .filter(contact -> id.equals(contact.getId()))
                .findFirst();
    }

    //Sync methods

    private Promise<State> setContacts(final List<Contact> contacts) {
        return store.setState(previousState -> previousState
                .withContacts(contacts));
    }

    public Promise<State> showNewContactModal() {
        return store.setState(previousState -> previousState
                .withSelectedContact(null)
                .withModalState(SHOWING));
    }

    public Promise<State> hideModal() {
        return store.setState(previousState -> previousState
                .withModalState(HIDDEN));
    }

    Promise<State> add(final Contact contact) {
        return store.setState(previousState -> previousState
                .withOneMoreContact(contact)
                .withModalState(HIDDEN));
    }

    Promise<State> remove(final Contact contact) {
        return store.setState(previousState -> {

            DomGlobal.console.info("LOCAL: Creating " + contact.getId());

            return previousState
                    .withoutContact(contact)
                    .withModalState(HIDDEN);
        });
    }

    public Promise<State> triggerAdd(final Contact contact) {
        return addOverTheWire(contact).then(this::add);
    }

    public Promise<State> triggerDelete(final Contact contact) {
        return deleteOverTheWire(contact).then(this::remove);
    }

    private Promise<Contact> addOverTheWire(final Contact contact) {
        return promises.promisify(contactService, s -> {
            return s.create(new ContactOperation(contact, bus.getSessionId()));
        }).then((final Object response) -> ifSuccess((Response) response, () -> {

            final String uri = ((Response) response).getHeader("Location");
            contact.setId(Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1))); //FIXME: Mutating parameter: bad!!!!
            return promises.resolve(contact);
        }));
    }

    private Promise<Contact> deleteOverTheWire(final Contact contact) {
        return promises.promisify(contactService, s -> {
            return s.delete(contact.getId());
        }).then((final Object response) -> ifSuccess((Response) response, () -> {
            return promises.resolve(contact);
        }));
    }

    private <X> Promise<X> ifSuccess(final Response response,
                                     final Supplier<Promise<X>> success) {

        if (response.getStatusCode() <= 200 && response.getStatusCode() >= 300) {
            return promises.reject(response);
        } else {
            return success.get();
        }
    }

    //    @StateSource2
    public interface State {

        List<Contact> contacts();

        Optional<Contact> selectedContact();

        ModalState modalState();

        enum ModalState {
            SHOWING,
            HIDDEN;
        }
    }
}
