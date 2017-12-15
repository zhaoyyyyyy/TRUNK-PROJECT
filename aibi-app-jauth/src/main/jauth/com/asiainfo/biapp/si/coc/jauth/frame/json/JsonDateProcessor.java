package com.asiainfo.biapp.si.coc.jauth.frame.json;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;


public class JsonDateProcessor
  implements JsonValueProcessor
{
  private FastDateFormat format;

  public JsonDateProcessor(String format)
  {
    this.format =FastDateFormat.getInstance(format);
  }

  public Object processArrayValue(Object value, JsonConfig jsonConfig) {
    return "";
  }

  public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
    if (value == null) {
      return null;
    }
    return this.format.format((Date)value);
  }
}
