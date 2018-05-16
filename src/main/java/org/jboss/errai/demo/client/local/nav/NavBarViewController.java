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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import elemental2.dom.HTMLAnchorElement;
import elemental2.promise.Promise;
import org.jboss.errai.demo.client.local.nav.NavBarViewStore.StateImpl;
import org.jboss.errai.demo.client.local.reactive.codegen.PropsSource2;
import org.jboss.errai.demo.client.local.reactive.codegen.StateSource2;
import org.jboss.errai.demo.client.local.reactive.model.RViewController;

@ApplicationScoped
public class NavBarViewController implements RViewController<NavBarView, NavBarViewController.Props> {

    @Inject
    private NavBarViewStore store;

    @Override
    public void init(final NavBarView view, final Props props) {
        store.init(view, props, new StateImpl(new ArrayList<>()));
    }

    public Promise<State> add(final HTMLAnchorElement link) {
        return store.setState(previousState -> previousState.withOneMoreLink(link));
    }

    public Promise<State> remove(final HTMLAnchorElement link) {
        return store.setState(previousState -> previousState.withoutLink(link));
    }

//    @PropsSource2
    public interface Props {

    }

//    @StateSource2
    public interface State {

        List<HTMLAnchorElement> links();
    }
}
