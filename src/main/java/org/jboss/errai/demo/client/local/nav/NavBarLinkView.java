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

package org.jboss.errai.demo.client.local.nav;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLIElement;
import org.jboss.errai.common.client.dom.elemental2.Elemental2DomUtil;
import org.jboss.errai.demo.client.local.reactive.codegen.PropsSource2;
import org.jboss.errai.demo.client.local.reactive.lists.RListElementView;
import org.jboss.errai.demo.client.local.reactive.model.RView;
import org.jboss.errai.demo.client.local.reactive.model.RViewController;

import static org.jboss.errai.demo.client.local.reactive.model.RStore.NoState;

@Dependent
public class NavBarLinkView implements RListElementView<NavBarLinkView.Props, NoState> {

    @Inject
    private HTMLLIElement li;

    @Inject
    private Elemental2DomUtil elemental2DomUtil;

    @Inject
    private NavBarLinkViewStore store;

    @Override
    public HTMLElement getElement() {
        return li;
    }

    @Override
    public RViewController<RView<Props, NoState>, Props> getController() {
        return (view, props) -> {
            store.init((NavBarLinkView) view, props, new NoState());
        };
    }

    @Override
    public void render(final Props props, final NoState state) {
        elemental2DomUtil.removeAllElementChildren(li); //TODO: Remove when every render provides a new HTML
        li.appendChild(props.htmlAnchorElement());
    }

    @PropsSource2
    public interface Props {

        HTMLAnchorElement htmlAnchorElement();
    }
}
