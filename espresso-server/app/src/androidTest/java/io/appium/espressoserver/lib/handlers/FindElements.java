/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.appium.espressoserver.lib.handlers;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;
import io.appium.espressoserver.lib.handlers.exceptions.InvalidStrategyException;
import io.appium.espressoserver.lib.handlers.exceptions.MissingCommandsException;
import io.appium.espressoserver.lib.model.Element;
import io.appium.espressoserver.lib.model.Locator;
import io.appium.espressoserver.lib.viewaction.ViewGetter;

import static io.appium.espressoserver.lib.helpers.ViewFinder.findAllBy;

public class FindElements implements RequestHandler<Locator, List<Element>> {

    @Override
    public List<Element> handle(Locator locator) throws AppiumException {
        View parentView = null;
        if (locator.getElementId() != null) {
            parentView = new ViewGetter().getView(Element.getViewInteractionById(locator.getElementId()));
        }
        if (locator.getUsing() == null) {
            throw new InvalidStrategyException("Locator strategy cannot be empty");
        } else if (locator.getValue() == null) {
            throw new MissingCommandsException("No locator provided");
        }

        // Get the viewInteractions
        List<View> views = findAllBy(parentView,
                locator.getUsing(), locator.getValue());

        // Turn it into a list of elements
        List<Element> elements = new ArrayList<>();
        for (View view : views) {
            elements.add(new Element(view));
        }

        // If we have a match, return success
        return elements;
    }
}
