package com.terium.utils.zk;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zul.Div;

@SuppressWarnings("serial")
public class Menucard extends HtmlMacroComponent{
	
	Div divCard;
	
	@Override
	public void afterCompose() {
		super.afterCompose();
		divCard = (Div) this.getChildren().get(0);
		
	}
	
	
}
