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

package org.jboss.errai.demo.client.local.reactive.lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.enterprise.context.Dependent;

import elemental2.dom.HTMLElement;
import org.jboss.errai.common.client.dom.elemental2.Elemental2DomUtil;

@Dependent
public class RListView<T, P, S> {

    private Function<T, P> toProps;
    private Supplier<RListElementView<P, S>> viewFactory;
    private HTMLElement container;

    private List<T> objects = new ArrayList<>();
    private Map<T, RListElementView<P, S>> views;

    public void init(final HTMLElement container,
                     final Supplier<RListElementView<P, S>> viewFactory) {

      this.container = container;
      this.viewFactory = viewFactory;
    }

    public void render(final List<T> newObjects,
                       final Function<T, P> toProps) {

        new HashSet<>(this.objects).forEach(this::remove);
        this.toProps = toProps;
        this.objects = new ArrayList<>(newObjects);
        this.views = new HashMap<>();
        newObjects.forEach(this::addViewFor);
    }

    public void addAfter(final T object, final T newObject) {
        final RListElementView<P, S> view = viewFactory.get();
        view.render(toProps.apply(newObject));
        views.put(newObject, view);

        int next = objects.indexOf(object) + 1;

        if (next <= 0 || next >= objects.size()) {
            container.appendChild(view.getElement());
            objects.add(newObject);
        } else {
            container.insertBefore(view.getElement(), views.get(objects.get(next)).getElement());
            objects.add(next, newObject);
        }
    }

    public void add(final T newObject) {
        addViewFor(newObject);
        objects.add(newObject);
    }

    private void addViewFor(final T newObject) {
        final RListElementView<P, S> view = viewFactory.get();
        view.render(toProps.apply(newObject));
        views.put(newObject, view);
        container.appendChild(view.getElement());
    }

    public void remove(final T object) {
        final RListElementView<P, S> view = views.remove(object);
        view.destroy();
        container.removeChild(view.getElement());
        objects.remove(object);
    }

    public List<T> getObjects() {
        return objects;
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    public RListElementView<P, S> getView(final T obj) {
        return views.get(obj);
    }

    public void addIfNotPresent(final T obj) {
        if (!objects.contains(obj)) {
            add(obj);
        }
    }

    public void addOrReplace(final T obj, final T newObj) {
        if (!objects.contains(obj)) {
            add(newObj);
        } else {
            addAfter(obj, newObj);
            remove(obj);
        }
    }
}
