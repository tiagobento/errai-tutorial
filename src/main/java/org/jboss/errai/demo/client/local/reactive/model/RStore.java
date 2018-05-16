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

package org.jboss.errai.demo.client.local.reactive.model;

import java.util.function.Function;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import elemental2.promise.Promise;
import org.jboss.errai.demo.client.local.contact.Promises;

@Dependent
public class RStore<V extends RView<P, S>, P, S, GS extends S> {

    private V view;
    private P props = null;
    private GS state;

    @Inject
    protected Promises promises;

    public void init(final V view, final P props, final GS initialState) {
        this.view = view;
        this.state = initialState;

        if (!props.equals(this.props)) {
            render(view, props, initialState);
        }

        this.props = props;
    }

    protected Promise<S> setState(final Function<GS, GS> newState) {
        state = newState.apply(state);
        render(view, props, state);
        return promises.resolve(state);
    }

    protected void render(final V view, final P props, final S state) {
        view.render(props, state);
    }

    public GS state() {
        return state;
    }

    public P props() {
        return props;
    }

    public static class NoState {

    }

    public static class NoProps {

    }
}
