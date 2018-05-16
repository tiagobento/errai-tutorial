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

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLHeadingElement;
import elemental2.dom.MouseEvent;
import org.jboss.errai.databinding.client.components.ListComponent;
import org.jboss.errai.demo.client.local.components.BindableEmailAnchor;
import org.jboss.errai.demo.client.local.components.BindableTelAnchor;
import org.jboss.errai.demo.client.local.misc.Click;
import org.jboss.errai.demo.client.local.misc.DateConverter;
import org.jboss.errai.demo.client.local.reactive.codegen.PropsSource2;
import org.jboss.errai.demo.client.local.reactive.lists.RListElementView;
import org.jboss.errai.demo.client.local.reactive.model.RView;
import org.jboss.errai.demo.client.local.reactive.model.RViewController;
import org.jboss.errai.demo.client.shared.Contact;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.jboss.errai.demo.client.local.misc.Click.Type.DOUBLE;
import static org.jboss.errai.demo.client.local.misc.Click.Type.SINGLE;
import static org.jboss.errai.demo.client.local.reactive.model.RStore.NoState;

/**
 * <p>
 * An Errai UI component for displaying a single {@link Contact} as a row in an HTML table. Can be used to display
 * {@link Contact Contacts} in a {@link ListComponent}. This component can be bound to a {@link Contact} by calling
 * {@link #setValue(Contact)}.
 * <p>
 * <p>
 * The HTML markup for this {@link Templated} component is the HTML element with the CSS class {@code contact} in the
 * file {@code contact-page.html} in this package. This component uses CSS from the file {@code contact-page.css} in
 * this package.
 * <p>
 * <p>
 * The {@link DataField} annotation marks fields that replace HTML elements from the template file. As an example, the
 * field {@link ContactView#contact} is the root {@code
 * <p>
 * <tr>
 * } element of this component; it can be used to attach this component to the DOM.
 * <p>
 * <p>
 * The {@link Bound} annotations mark UI fields as managed by Errai Data Binding, which keeps UI values synchronized
 * with properties in the bound {@link Contact} model instance. (See the base class, {@link BaseContactView}.)
 * <p>
 * <p>
 * Errai UI automatically registers methods annotated with {@link EventHandler} to listen for DOM events.
 * <p>
 * <p>
 * Instances of this type should be obtained via Errai IoC, either by using {@link Inject} in another container managed
 * bean, or by programmatic lookup through the bean manager.
 */
@Templated(stylesheet = "contact-page.css")
public class ContactView implements RListElementView<ContactView.Props, NoState> {


    @Inject
    @DataField("contact")
    private HTMLDivElement root;

    @Inject
    @DataField("fullname")
    private HTMLDivElement fullname;

    @Inject
    @Named("h4")
    @DataField("nickname")
    private HTMLHeadingElement nickname;

    @Inject
    @DataField("phonenumber")
    private BindableTelAnchor phonenumber;

    @Inject
    @DataField("email")
    private BindableEmailAnchor email;

    @Inject
    @DataField("birthday")
    private HTMLDivElement birthday;

    @Inject
    @DataField("notes")
    private HTMLDivElement notes;

    @Inject
    @Click(SINGLE)
    private Event<Contact> click;

    @Inject
    @Click(DOUBLE)
    private Event<Contact> dblClick;

    @Inject
    private ContactViewStore store;

    @EventHandler("contact")
    public void onClick(final @ForEvent("click") MouseEvent event) {
        click.fire(store.props().contact());
    }

    @EventHandler("contact")
    public void onDoubleClick(final @ForEvent("dblclick") MouseEvent event) {
        dblClick.fire(store.props().contact());
    }

    @Override
    public void render(final Props props, final NoState state) {

        if (props.isSelected()) {
            getElement().classList.add("selected");
        } else {
            getElement().classList.remove("selected");
        }

        nickname.textContent = props.contact().getNickname();
        fullname.textContent = props.contact().getFullname();
        phonenumber.setTextContent(props.contact().getPhonenumber());
        email.textContent = props.contact().getEmail();
        birthday.textContent = new DateConverter().toWidgetValue(props.contact().getBirthday());
        notes.textContent = props.contact().getNotes();
    }

    @Override
    public RViewController<RView<Props, NoState>, Props> getController() {
        return (view, props) -> {
            store.init((ContactView) view, props, new NoState());
        };
    }

    @PropsSource2
    public interface Props {

        Contact contact();

        boolean isSelected();
    }
}
