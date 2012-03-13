/*
 * Copyright (c) 2010 Zhihua (Dennis) Jiang
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtmobile.ui.client.widgets;

import java.util.Iterator;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmobile.ui.client.utils.Utils;

public class TabContent extends WidgetBase implements HasWidgets {

	protected FlowPanel _panel = new FlowPanel();
	
	public TabContent() {
		initWidget(_panel);
	}
	
	@Override
	public void add(Widget w) {
		_panel.add(w);
	}

	@Override
	public void clear() {
		_panel.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return _panel.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return _panel.remove(w);
	}
	
	@Override
	public void onTransitionEnd() {
		for (int i = 0; i < _panel.getWidgetCount(); i++) {
			Widget w = _panel.getWidget(i);
			if (Utils.isSubclassOf(w.getClass(), WidgetBase.class)) {
				((WidgetBase) w).onTransitionEnd();
			}
		}
	}

}
