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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.enterprise.context.Dependent;

import elemental2.dom.HTMLAnchorElement;
import elemental2.promise.Promise;
import org.jboss.errai.demo.client.local.reactive.model.RStore;

import static org.jboss.errai.demo.client.local.nav.NavBarViewController.Props;
import static org.jboss.errai.demo.client.local.nav.NavBarViewController.State;

//Generated
@Dependent
public class NavBarViewStore extends RStore<NavBarView, Props, State, NavBarViewStore.StateImpl> {

    @Override
    protected Promise<State> setState(Function<StateImpl, StateImpl> newState) {
        return super.setState(newState);
    }

    public static class PropsImpl implements Props {

    }

    public static class StateImpl implements State {

        private final List<HTMLAnchorElement> links;

        public StateImpl(final List<HTMLAnchorElement> links) {
            this.links = links;
        }

        public List<HTMLAnchorElement> links() {
            return links;
        }

        public StateImpl withOneMoreLink(final HTMLAnchorElement link) {
            final List<HTMLAnchorElement> newLinks = new ArrayList<>(links);
            newLinks.add(link);
            return new StateImpl(newLinks);
        }

        public StateImpl withoutLink(final HTMLAnchorElement link) {
            final List<HTMLAnchorElement> newLinks = new ArrayList<>(links);
            newLinks.remove(link);
            return new StateImpl(newLinks);
        }
    }
}
