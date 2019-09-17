package com.axelor.event.service;

import java.io.File;
import com.axelor.event.db.Event;
import com.axelor.meta.db.MetaFile;

public interface EventService {
  
  public Event compute(Event event);
  public boolean importCsvFile(File csvFile);
  public MetaFile sendEmail(Event event);
  
}
