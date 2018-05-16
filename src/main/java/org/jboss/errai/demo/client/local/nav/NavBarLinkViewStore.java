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

import java.util.function.Function;

import elemental2.dom.HTMLAnchorElement;
import elemental2.promise.Promise;
import org.jboss.errai.demo.client.local.reactive.model.RStore;

//Generated
public class NavBarLinkViewStore extends RStore<NavBarLinkView, NavBarLinkView.Props, RStore.NoState, RStore.NoState> {

    @Override
    protected Promise<NoState> setState(final Function<NoState, NoState> newState) {
        return super.setState(newState);
    }

    public static class PropsImpl implements NavBarLinkView.Props {

        private final HTMLAnchorElement htmlAnchorElement;

        public PropsImpl(final HTMLAnchorElement htmlAnchorElement) {
            this.htmlAnchorElement = htmlAnchorElement;
        }

        @Override
        public HTMLAnchorElement htmlAnchorElement() {
            return htmlAnchorElement;
        }
    }
}
