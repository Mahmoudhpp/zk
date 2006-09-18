/* Separator.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Jul 19 12:31:35     2005, Created by tomyeh@potix.com
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package com.potix.zul.html;

import com.potix.lang.Objects;
import com.potix.xml.HTMLs;

import com.potix.zk.ui.WrongValueException;
import com.potix.zul.html.impl.XulElement;

/**
 * A separator.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class Separator extends XulElement {
	private String _orient = "horizontal";
	private String _spacing;
	private boolean _bar;

	/** Returns the orient.
	 * <p>Default: "horizontal".
	 */
	public String getOrient() {
		return _orient;
	}
	/** Sets the orient.
	 * @param orient either "horizontal" or "vertical".
	 */
	public void setOrient(String orient) throws WrongValueException {
		if (!"horizontal".equals(orient) && !"vertical".equals(orient))
			throw new WrongValueException(orient);

		if (!Objects.equals(_orient, orient)) {
			_orient = orient;
			smartUpdate("class", getSclass());
		}
	}
	/** Returns whether to display a visual bar as the separator.
	 * <p>Default: false
	 */
	public boolean isBar() {
		return _bar;
	}
	/** Sets  whether to display a visual bar as the separator.
	 */
	public void setBar(boolean bar) {
		if (_bar != bar) {
			_bar = bar;
			smartUpdate("class", getSclass());
		}
	}

	/** Returns the spacing.
	 * <p>Default: null (depending on CSS).
	 */
	public String getSpacing() {
		return _spacing;
	}
	/** Sets the spacing.
	 * @param spacing the spacing (such as "0", "5px", "3pt" or "1em")
	 */
	public void setSpacing(String spacing) {
		if (spacing != null && spacing.length() == 0) spacing = null;
		if (!Objects.equals(_spacing, spacing)) {
			_spacing = spacing;
			smartUpdate("style", getRealStyle());
		}
	}
	protected String getRealStyle() {
		final String style = super.getRealStyle();
		if (_spacing == null)
			return style;

		final StringBuffer sb = new StringBuffer(64).append("margin:");
		if ("vertical".equals(_orient))
			sb.append("0 ").append(_spacing);
		else
			sb.append(_spacing).append(" 0");
		return sb.append(';').append(style).toString();
	}

	//-- super --//
	/** Returns the style class.
	 * If the style class is not defined ({@link #setSclass} is not called
	 * or called with null or empty), it returns the style class based
	 * on {@link #getOrient} and {@link #isBar}.
	 */
	public String getSclass() {
		final String scls = super.getSclass();
		if (scls != null) return scls;
		return "vertical".equals(getOrient()) ?
			isBar() ?"vsep-bar":"vsep": isBar() ?"hsep-bar":"hsep";
	}

	//-- Component --//
	/** Default: not childable.
	 */
	public boolean isChildable() {
		return false;
	}
}
