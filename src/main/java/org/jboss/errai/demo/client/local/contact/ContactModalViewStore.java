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

import java.util.Optional;
import java.util.function.Function;

import elemental2.promise.Promise;
import org.jboss.errai.demo.client.local.contact.ContactListPageController.State.ModalState;
import org.jboss.errai.demo.client.local.reactive.model.RStore;
import org.jboss.errai.demo.client.shared.Contact;

//Generated
public class ContactModalViewStore extends RStore<ContactModalView, ContactModalView.Props, RStore.NoState, RStore.NoState> {

    @Override
    protected Promise<NoState> setState(final Function<RStore.NoState, RStore.NoState> s) {
        return super.setState(s);
    }

    public static class PropsImpl implements ContactModalView.Props {

        private final ModalState modalState;
        private final Contact contact;
        private final ContactListPageController contactListPageController;

        public PropsImpl(final ModalState modalState,
                         final Contact contact,
                         final ContactListPageController contactListPageController) {

            this.modalState = modalState;
            this.contact = contact;
            this.contactListPageController = contactListPageController;
        }

        @Override
        public Optional<Contact> contact() {
            return Optional.ofNullable(contact);
        }

        @Override
        public ModalState modalState() {
            return modalState;
        }

        @Override
        public ContactListPageController contactListPageController() {
            return contactListPageController;
        }
    }
}
