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

import com.google.gwt.user.client.ui.Composite;

public class WidgetBase extends Composite implements IsGwtMobileWidget {

    private IsGwtMobileWidgetHelper _widgetHelper = new IsGwtMobileWidgetHelper();
    
    @Override
    public void onLoad() {
    	super.onLoad();
    	_widgetHelper.CheckInitialLoad(this);
    }

    @Override
	public void onInitialLoad() {
    }
    
    //FIXME: shouldn't this method be on PageBase/PanelBase?
    @Override
	public void onTransitionEnd() {    	
    }
    
    @Override
	public void setSecondaryStyle(String style) {
    	_widgetHelper.setSecondaryStyle(this, style);
    }
}
