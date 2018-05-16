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

import javax.inject.Inject;

import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLFormElement;
import elemental2.dom.MouseEvent;
import org.jboss.errai.demo.client.local.components.PaperInput;
import org.jboss.errai.demo.client.local.components.PaperTextArea;
import org.jboss.errai.demo.client.local.contact.ContactListPageController.State.ModalState;
import org.jboss.errai.demo.client.local.misc.DateConverter;
import org.jboss.errai.demo.client.local.reactive.codegen.PropsSource2;
import org.jboss.errai.demo.client.local.reactive.model.RView;
import org.jboss.errai.demo.client.local.reactive.model.RViewController;
import org.jboss.errai.demo.client.shared.Contact;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.jboss.errai.demo.client.local.contact.ContactListPageController.State.ModalState.SHOWING;
import static org.jboss.errai.demo.client.local.reactive.model.RStore.NoState;

@Templated
public class ContactModalView implements RView<ContactModalView.Props, NoState> {

    @Inject
    @DataField("modal")
    private HTMLFormElement form;

    @Inject
    @DataField("nickname")
    private PaperInput nickName;

    @Inject
    @DataField("notes")
    private PaperTextArea notes;

    @Inject
    @DataField("fullname")
    private PaperInput fullName;

    @Inject
    @DataField("birthday")
    private PaperInput birthday;

    @Inject
    @DataField("email")
    private PaperInput email;

    @Inject
    @DataField("phonenumber")
    private PaperInput phoneNumber;

    @Inject
    @DataField("modal-delete")
    private HTMLButtonElement deleteButton;

    @Inject
    private ContactModalViewStore store;

    @EventHandler("modal-cancel")
    public void onCancelButtonClick(final @ForEvent("click") MouseEvent e) {
        store.props().contactListPageController().hideModal();
    }

    @EventHandler("modal-delete")
    public void onDeleteButtonClick(final @ForEvent("click") MouseEvent e) {
        store.props().contactListPageController().triggerDelete(store.props().contact().get());
    }

    @EventHandler("modal-submit")
    public void onSubmitButtonClick(final @ForEvent("click") MouseEvent e) {

        final Contact contact = new Contact();
        contact.setNickname(nickName.getValue());
        contact.setEmail(email.getValue());
        contact.setBirthday(new DateConverter().toModelValue(birthday.getValue()));
        contact.setFullname(fullName.getValue());
        contact.setNotes(notes.getValue());
        contact.setPhonenumber(phoneNumber.getValue());

        store.props().contactListPageController().triggerAdd(contact);
    }

    @Override
    public void render(final Props props, final NoState state) {

        if (props.modalState().equals(SHOWING)) {
            form.classList.add("displayed");
        }

        //TODO: Remove when every render provides a new HTML
        else {
            form.classList.remove("displayed");
        }

        if (props.contact().isPresent()) {
            final Contact contact = props.contact().get();
            nickName.setValue(contact.getNickname());
            email.setValue(contact.getEmail());
            birthday.setValue(new DateConverter().toWidgetValue(contact.getBirthday()));
            fullName.setValue(contact.getFullname());
            notes.setValue(contact.getNotes());
            phoneNumber.setValue(contact.getPhonenumber());
            deleteButton.classList.remove("hidden");
        }

        //TODO: Remove when every render provides a new HTML
        else {
            nickName.setValue("");
            email.setValue("");
            birthday.setValue("");
            fullName.setValue("");
            notes.setValue("");
            phoneNumber.setValue("");
            deleteButton.classList.add("hidden");
        }
    }

    @Override
    public RViewController<RView<Props, NoState>, Props> getController() {
        return (view, props) -> {
            store.init((ContactModalView) view, props, new NoState());
        };
    }

    @PropsSource2
    public interface Props {

        Optional<Contact> contact();

        ModalState modalState();

        ContactListPageController contactListPageController();
    }
}
