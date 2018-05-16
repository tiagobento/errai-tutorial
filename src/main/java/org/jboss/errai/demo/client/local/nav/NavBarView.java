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

package org.jboss.errai.demo.client.local.nav;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLUListElement;
import org.jboss.errai.demo.client.local.AppSetup;
import org.jboss.errai.demo.client.local.nav.NavBarViewController.Props;
import org.jboss.errai.demo.client.local.reactive.lists.RListView;
import org.jboss.errai.demo.client.local.reactive.model.RStore.NoState;
import org.jboss.errai.demo.client.local.reactive.model.RView;
import org.jboss.errai.demo.client.local.reactive.model.RViewController;
import org.jboss.errai.demo.client.shared.Contact;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.jboss.errai.demo.client.local.nav.NavBarViewController.State;

/**
 * <p>
 * An Errai UI component for displaying a bootstrap 3 navigation bar.
 * <p>
 * <p>
 * The HTML markup for this {@link Templated} component is the HTML element with the CSS class {@code navbar} in the
 * file {@code contact-page.html} in this package.
 * <p>
 * <p>
 * The {@link DataField} annotation marks fields that replace HTML elements from the template file. As an example, the
 * field {@link #navbar} is the root {@code nav} element of this component; it can be used to attach this component to
 * the DOM (see {@link AppSetup}).
 * <p>
 * <p>
 * The {@link Bound} annotations mark UI fields as managed by Errai Data Binding, which keeps UI values synchronized
 * with properties in the bound {@link Contact} model instance. (See the base class, {@link BaseContactView}.)
 * <p>
 * <p>
 * Instances of this type should be obtained via Errai IoC, either by using {@link Inject} in another container managed
 * bean, or by programmatic lookup through the bean manager.
 */
@ApplicationScoped
@Templated("../contact/contact-page.html#navbar")
public class NavBarView implements RView<Props, State> {

    @Inject
    @Named("nav")
    @DataField("navbar")
    private HTMLElement navbar;

    @Inject
    @DataField("navlist")
    private HTMLUListElement navList;

    @Inject
    private RListView<HTMLAnchorElement, NavBarLinkView.Props, NoState> linksView;

    @Inject
    private ManagedInstance<NavBarLinkView> linkViewFactory;

    @Inject
    private NavBarViewController controller;

    @Override
    public HTMLElement getElement() {
        return navbar;
    }

    @Override
    public RViewController getController() {
        return controller;
    }

    @PostConstruct
    public void init() {
        linksView.init(navList, linkViewFactory::get);
        render(new NavBarViewStore.PropsImpl());
    }

    @Override
    public void render(final Props props, final State state) {
        linksView.render(state.links(), NavBarLinkViewStore.PropsImpl::new);
    }
}
