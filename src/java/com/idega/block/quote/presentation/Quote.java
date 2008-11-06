package com.idega.block.quote.presentation;

import com.idega.block.quote.business.QuoteBusiness;
import com.idega.block.quote.business.QuoteHolder;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.PresentationUtil;
import com.idega.util.text.TextSoap;

/**
 * Title: Quote block
 * Description: A block that displays random quotes from the database
 * Copyright: Copyright (c) 2000-2002 idega.is All Rights Reserved
 * Company: idega
  *@author <a href="mailto:laddi@idega.is">Thorhallur "Laddi" Helgason</a>
 * @modified <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.2
 */

public class Quote extends Block implements Builderaware {

	private int _quoteID = -1;
	private int _objectID = -1;
	private boolean _hasEditPermission = false;
	private int _iLocaleID;
	private String width_;
	private String height_;
	private String alignment_ = Table.HORIZONTAL_ALIGN_CENTER;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.quote";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	
	private boolean _alwaysFetchFromDatabase = false;
	private boolean _showAuthor = true;
	private boolean _showOrigin = true;
	private boolean _showQuotes = true;

	public Quote() {
		setDefaultValues();
	}

	public Quote(int quoteID) {
		this();
		this._quoteID = quoteID;
	}

	@Override
	public void main(IWContext iwc) throws Exception {
		this._iwb = getBundle(iwc);
		this._iwrb = this._iwb.getResourceBundle(iwc.getCurrentLocale());
		this._objectID = getICObjectInstanceID();
		PresentationUtil.addStyleSheetToHeader(iwc, _iwb.getVirtualPathWithFileNameString("style/quote.css"));

		this._hasEditPermission = iwc.hasEditPermission(this);
		this._iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

		Layer layer = drawLayer();
		
		QuoteHolder quote = getQuoteBusiness().getRandomQuote(iwc, this._iLocaleID, this._objectID, this._alwaysFetchFromDatabase);
		if (quote != null) {
			this._quoteID = quote.getQuoteID();
		}

		if (this._hasEditPermission) {
			layer.add(getAdminLayer(iwc));
		}

		layer.add(getQuoteLayer(iwc, quote));
		
		add(layer);
	}

	private Layer getQuoteLayer(IWContext iwc, QuoteHolder quote) {
		Layer layer = new Layer();
		layer.setStyleClass("quoteItem");

		if (quote != null) {
			String originString = quote.getOrigin();
			String textString = quote.getText();
			if (textString == null) {
				textString = "";
			}
			String authorString = quote.getAuthor();
			if (authorString == null || authorString.length() == 0) {
				authorString = this._iwrb.getLocalizedString("unknown", "Unknown");
			}

			Layer origin = new Layer();
			origin.setStyleClass("origin");
			origin.add(new Text(originString + ":"));
			
			Layer text = new Layer();
			text.setStyleClass("text");
			Text quoteText = null;
			if (this._showQuotes) {
				quoteText = new Text("\"" + TextSoap.formatText(textString) + "\"");
			}
			else {
				quoteText = new Text(TextSoap.formatText(textString));
			}
			text.add(quoteText);
			
			Layer author = new Layer();
			author.setStyleClass("author");
			author.add(new Text("-" + Text.getNonBrakingSpace().getText() + authorString));

			if (this._showOrigin && originString != null && originString.length() > 0) {
				layer.add(origin);
			}
			layer.add(text);
			if (this._showAuthor) {
				layer.add(author);
			}
		}
		else {
			layer.add(new Text(this._iwrb.getLocalizedString("no_quotes", "No quotes in database...")));
		}

		return layer;
	}

	private Layer getAdminLayer(IWContext iwc) {
		Layer layer = new Layer();
		layer.setStyleClass("quoteAdmin");

		layer.add(getCreateLink(iwc));
		if (this._quoteID != -1) {
			layer.add(getEditLink(iwc));
			layer.add(getDeleteLink(iwc));
		}

		return layer;
	}

	private Layer drawLayer() {
		Layer layer = new Layer();
		layer.setStyleClass("quote");
		
		return layer;
	}

	private Link getCreateLink(IWContext iwc) {
		Link link = new Link(iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/create.gif", this._iwrb.getLocalizedString("new_quote", "New Quote")));
		link.setWindowToOpen(QuoteEditor.class);
		link.setStyleClass("quoteAdminLink");
		link.addParameter(QuoteBusiness.PARAMETER_MODE, QuoteBusiness.PARAMETER_NEW);
		link.addParameter(QuoteBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);
		return link;
	}

	private Link getEditLink(IWContext iwc) {
		Link link = new Link(iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/edit.gif", this._iwrb.getLocalizedString("edit_quote", "Edit Quote")));
		link.setWindowToOpen(QuoteEditor.class);
		link.setStyleClass("quoteAdminLink");
		link.addParameter(QuoteBusiness.PARAMETER_MODE, QuoteBusiness.PARAMETER_EDIT);
		link.addParameter(QuoteBusiness.PARAMETER_QUOTE_ID, this._quoteID);
		link.addParameter(QuoteBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);
		return link;
	}

	private Link getDeleteLink(IWContext iwc) {
		Link link = new Link(iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER).getImage("shared/delete.gif", this._iwrb.getLocalizedString("delete_quote", "Delete Quote")));
		link.setWindowToOpen(QuoteEditor.class);
		link.setStyleClass("quoteAdminLink");
		link.addParameter(QuoteBusiness.PARAMETER_MODE, QuoteBusiness.PARAMETER_DELETE);
		link.addParameter(QuoteBusiness.PARAMETER_QUOTE_ID, this._quoteID);
		link.addParameter(QuoteBusiness.PARAMETER_OBJECT_INSTANCE_ID, this._objectID);
		return link;
	}

	private void setDefaultValues() {
		this.width_ = "150";
	}

	@Override
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/** @deprecated */
	@Deprecated
	@Override
	public void setWidth(String width) {
		this.width_ = width;
	}

	/** @deprecated */
	@Deprecated
	@Override
	public String getWidth() {
		return this.width_;
	}

	/** @deprecated */
	@Deprecated
	@Override
	public void setHeight(String height) {
		this.height_ = height;
	}

	/** @deprecated */
	@Deprecated
	@Override
	public String getHeight() {
		return this.height_;
	}

	/** @deprecated */
	@Deprecated
	public void setOriginStyle(String style) {
	}

	/** @deprecated */
	@Deprecated
	public void setTextStyle(String style) {
	}

	/** @deprecated */
	@Deprecated
	public void setAuthorStyle(String style) {
	}

	/** @deprecated */
	@Deprecated
	@Override
	public void setHorizontalAlignment(String alignment) {
		this.alignment_ = alignment;
	}

	/** @deprecated */
	@Deprecated
	@Override
	public String getHorizontalAlignment() {
		return this.alignment_;
	}

	@Override
	public boolean deleteBlock(int ICObjectInstanceID) {
		return false;
	}

	@Override
	public Object clone() {
		Quote obj = null;
		try {
			obj = (Quote) super.clone();
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	private QuoteBusiness getQuoteBusiness() {
		return QuoteBusiness.getQuoteBusinessInstace();
	}

	/** @deprecated */
	@Deprecated
	public void setQuoteWidth(String width) {
		setWidth(width);
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteWidth(int width) {
		setWidth(Integer.toString(width));
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteHeight(String height) {
		setHeight(height);
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteHeight(int height) {
		setHeight(Integer.toString(height));
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteOriginStyle(String style) {
		setOriginStyle(style);
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteTextStyle(String style) {
		setTextStyle(style);
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteAuthorStyle(String style) {
		setAuthorStyle(style);
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteOriginSize(String size) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteOriginSize(int size) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteOriginColor(String color) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteOriginFace(String face) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteTextSize(String size) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteTextSize(int size) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteTextColor(String color) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteTextFace(String face) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteAuthorSize(String size) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteAuthorSize(int size) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteAuthorColor(String color) {
	}
	/** @deprecated */
	@Deprecated
	public void setQuoteAuthorFace(String face) {
	}
	

	public void setToGetNewQuoteOnEveryReload(boolean fetchFromDatabase) {
		this._alwaysFetchFromDatabase = fetchFromDatabase;
	}

	/**
	 * @param author The _showAuthor to set.
	 */
	public void setShowAuthor(boolean author) {
		this._showAuthor = author;
	}
	
	/**
	 * @param origin The _showOrigin to set.
	 */
	public void setShowOrigin(boolean origin) {
		this._showOrigin = origin;
	}
	
	/**
	 * @param quotes The _showQuotes to set.
	 */
	public void setShowQuotes(boolean quotes) {
		this._showQuotes = quotes;
	}
}