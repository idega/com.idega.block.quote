//idega 2000 - Laddi

package com.idega.block.quote.data;



import java.sql.*;
import java.util.Locale;
import java.util.Collection;
import javax.ejb.*;
import com.idega.data.*;
import com.idega.core.data.ICLocale;
import com.idega.block.text.business.TextFinder;


public class QuoteEntityBMPBean extends GenericEntity implements QuoteEntity {


  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameQuoteOrigin(), "Source", true, true, String.class);
    addAttribute(getColumnNameQuoteText(), "Text", true, true, String.class);
    addAttribute(getColumnNameQuoteAuthor(), "Author", true, true, String.class);
    addAttribute(getColumnNameICLocaleID(),"Locale",true,true,Integer.class,"many-to-one",ICLocale.class);
  }


  public String getIDColumnName(){ return "QU_QUOTE_ID";}

  protected static String getEntityTableName(){ return "QU_QUOTE";}
  protected static String getColumnNameQuoteOrigin(){ return "QU_QUOTE_ORIGIN";}
  protected static String getColumnNameQuoteText(){ return "QU_QUOTE_TEXT";}
  protected static String getColumnNameQuoteAuthor(){ return "QU_QUOTE_AUTHOR";}
  protected static String getColumnNameICLocaleID(){ return "IC_LOCALE_ID";}



  public String getEntityName(){
    return getEntityTableName();
  }

  public String getQuoteOrigin(){
    return getStringColumnValue(getColumnNameQuoteOrigin());
  }

  public void setQuoteOrigin(String quote_origin){
    setColumn(getColumnNameQuoteOrigin(), quote_origin);
  }

  public String getQuoteText(){
    return getStringColumnValue(getColumnNameQuoteText());
  }

  public void setQuoteText(String quote_text){
    setColumn(getColumnNameQuoteText(), quote_text);
  }

  public String getQuoteAuthor(){
    return getStringColumnValue(getColumnNameQuoteAuthor());
  }

  public void setQuoteAuthor(String quote_author){
    setColumn(getColumnNameQuoteAuthor(), quote_author);
  }

  public int getICLocaleID(){
    return getIntColumnValue(getColumnNameICLocaleID());
  }

  public void setICLocaleID(int localeID){
    setColumn(getColumnNameICLocaleID(),localeID);
  }

  public Collection ejbFindAllQuotesByLocale(int localeID)throws FinderException{
    return super.idoFindAllIDsByColumnBySQL(this.getColumnNameICLocaleID(),Integer.toString(localeID));
  }

}

