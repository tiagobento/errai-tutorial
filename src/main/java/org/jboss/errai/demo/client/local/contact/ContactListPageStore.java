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
import java.util.function.Function;

import elemental2.promise.Promise;
import org.jboss.errai.demo.client.local.reactive.model.RStore;
import org.jboss.errai.demo.client.shared.Contact;

import static org.jboss.errai.demo.client.local.contact.ContactListPageController.State;

//Generated
public class ContactListPageStore extends RStore<ContactListPage, RStore.NoProps, State, ContactListPageStore.StateImpl> {

    @Override
    protected Promise<State> setState(final Function<StateImpl, StateImpl> s) {
        return super.setState(s);
    }

    public static class StateImpl implements State {

        private final List<Contact> contacts;
        private ModalState modalState;
        private Contact selectedContact;

        public StateImpl(final List<Contact> contacts,
                         final ModalState modalState,
                         final Contact selectedContact) {

            this.contacts = contacts;
            this.modalState = modalState;
            this.selectedContact = selectedContact;
        }

        @Override
        public List<Contact> contacts() {
            return new ArrayList<>(contacts);
        }

        @Override
        public Optional<Contact> selectedContact() {
            return Optional.ofNullable(selectedContact);
        }

        @Override
        public ModalState modalState() {
            return modalState;
        }

        public StateImpl withOneMoreContact(final Contact contact) {
            final List<Contact> newContacts = new ArrayList<>(contacts);
            newContacts.add(contact);
            return new StateImpl(newContacts, modalState, selectedContact);
        }

        public StateImpl withoutContact(final Contact contact) {
            final List<Contact> newContacts = new ArrayList<>(contacts);
            newContacts.remove(contact);
            return new StateImpl(newContacts, modalState, selectedContact);
        }

        public StateImpl withContacts(final List<Contact> contacts) {
            return new StateImpl(new ArrayList<>(contacts), modalState, selectedContact);
        }

        public StateImpl withModalState(final ModalState modalState) {
            return new StateImpl(new ArrayList<>(contacts), modalState, selectedContact);
        }

        public StateImpl withSelectedContact(final Contact contact) {
            return new StateImpl(new ArrayList<>(contacts), modalState, contact);
        }
    }
}
