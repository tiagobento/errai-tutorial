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

import org.jboss.errai.demo.client.local.reactive.model.RView;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;

public interface RListElementView<P, S> extends RView<P, S>, IsElement {


    default void destroy() {
        //no-op
    }

    default void onDestroy(final Runnable onDestroy) {
        //no-op
    }
}
