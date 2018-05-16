/**
 * Copyright (C) 2016 Red Hat, Inc. and/or its affiliates.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.demo.client.local.contact;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.MouseEvent;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.databinding.client.components.ListComponent;
import org.jboss.errai.demo.client.local.reactive.lists.RListView;
import org.jboss.errai.demo.client.local.reactive.model.RStore;
import org.jboss.errai.demo.client.local.reactive.model.RView;
import org.jboss.errai.demo.client.local.reactive.model.RViewController;
import org.jboss.errai.demo.client.shared.Contact;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageHiding;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.jboss.errai.demo.client.local.contact.ContactListPageController.State;

/**
 * <p>
 * An Errai UI component for creating, displaying, updating, and deleting {@link Contact Contacts}. This component is
 * also an Errai Navigation {@link Page}; it will be displayed on the GWT host page whenever the navigation URL fragment
 * is {@code #/contacts}.
 * <p>
 * <p>
 * The HTML markup for this {@link Templated} component is the HTML element with the CSS class {@code contact-list} in
 * the file {@code contact-page.html} in this package. This component uses CSS from the file {@code contact-page.css} in
 * this package.
 * <p>
 * <p>
 * The {@link DataField} annotation marks fields that replace HTML elements from the template file. As an example, the
 * field {@link ContactView#editor} replaces the {@code <div>} element in the template with the CSS class
 * {@code modal-fields}. Because {@link ContactEditor} is an Errai UI component, the markup for {@link ContactEditor}
 * will replace the contents of the {@code modal-fields} div in this component.
 * <p>
 * <p>
 * This component uses a {@link ListComponent} to display a list of {@link Contact Contacts}. The {@code List<Contact>}
 * returned by calling {@link DataBinder#getModel()} on {@link #binder} is a model bound to a table of
 * {@link ContactView ContactDisplays} in an HTML table. Any changes to the model list (such as adding or removing
 * items) will be automatically reflected in the displayed table.
 * <p>
 * <p>
 * Instances of this type should be obtained via Errai IoC, either by using {@link Inject} in another container managed
 * bean, or by programmatic lookup through the bean manager.
 */
@Page(role = DefaultPage.class, path = "/contacts")
@Templated(value = "contact-page.html#contact-list", stylesheet = "contact-page.css")
public class ContactListPage implements RView<RStore.NoProps, State> {

    @Inject
    @DataField("contact-list-container")
    private HTMLDivElement contactListContainer;

    @Inject
    private RListView<Contact, ContactView.Props, RStore.NoState> contactViews;

    @Inject
    private ManagedInstance<ContactView> contactViewFactory;

    @Inject
    @DataField("contact-modal")
    private ContactModalView contactModalView;

    @Inject
    private ContactListPageController controller;

    @PostConstruct
    private void setup() {
        contactViews.init(contactListContainer, contactViewFactory::get);
        render(new RStore.NoProps());
    }

    @PageShown
    public void addNavBarButtons() {
        controller.addNavBarButtons();
    }

    @PageHiding
    public void removeNavBarButtons() {
        controller.removeNavBarButtons();
    }

    @EventHandler("new-contact")
    public void onNewContactClick(final @ForEvent("click") MouseEvent event) {
        controller.showNewContactModal();
    }

    @Override
    public RViewController getController() {
        return controller;
    }

    @Override
    public void render(final RStore.NoProps props, final State state) {

        contactViews.render(state.contacts(), contact -> {
            final boolean selected = state.selectedContact().map(contact::equals).orElse(false);
            return new ContactViewStore.PropsImpl(contact, selected);
        });

        contactModalView.render(new ContactModalViewStore.PropsImpl(
                state.modalState(),
                state.selectedContact().orElse(null),
                controller));
    }
}
