package com.idega.block.quote.business;



import java.sql.SQLException;

import com.idega.block.quote.data.QuoteEntity;



public class QuoteFinder {



  public static QuoteEntity getRandomQuote(int localeID) {

    try {

      QuoteEntity[] quotes = (QuoteEntity[]) com.idega.block.quote.data.QuoteEntityBMPBean.getStaticInstance(QuoteEntity.class).findAllByColumn(com.idega.block.quote.data.QuoteEntityBMPBean.getColumnNameICLocaleID(),Integer.toString(localeID),"=");

      if ( quotes != null ) {

	if ( quotes.length > 0 ) {

	  int quoteNumber = (int) Math.round(Math.random() * (quotes.length - 1));

	  return quotes[quoteNumber];

	}

      }

      return null;

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static QuoteEntity getQuote(int quoteID) {

    try {

      return ((com.idega.block.quote.data.QuoteEntityHome)com.idega.data.IDOLookup.getHomeLegacy(QuoteEntity.class)).findByPrimaryKeyLegacy(quoteID);

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static QuoteHolder getQuoteHolder(int quoteID) {

    try {

      return getQuoteHolder(((com.idega.block.quote.data.QuoteEntityHome)com.idega.data.IDOLookup.getHomeLegacy(QuoteEntity.class)).findByPrimaryKeyLegacy(quoteID));

    }

    catch (SQLException e) {

      return null;

    }

  }



  public static QuoteHolder getQuoteHolder(QuoteEntity quote) {

    QuoteHolder holder = null;

    if ( quote != null ) {

      holder = new QuoteHolder();

      holder.setQuoteID(quote.getID());

      holder.setAuthor(quote.getQuoteAuthor());

      holder.setOrigin(quote.getQuoteOrigin());

      holder.setText(quote.getQuoteText());

      holder.setLocaleID(quote.getICLocaleID());

    }

    return holder;

  }

}
