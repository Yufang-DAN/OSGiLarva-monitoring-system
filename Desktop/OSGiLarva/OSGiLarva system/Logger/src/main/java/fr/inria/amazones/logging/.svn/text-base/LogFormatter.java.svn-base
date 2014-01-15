package fr.inria.amazones.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter{
  public String format(LogRecord record){
    String cn=record.getSourceClassName();
    return "["+record.getLevel()+"]: "+cn.substring(cn.lastIndexOf('.')+1)+"->"+formatMessage(record)+"\n";
  }
}
