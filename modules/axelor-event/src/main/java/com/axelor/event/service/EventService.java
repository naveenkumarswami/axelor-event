package com.axelor.event.service;

import java.io.File;
import com.axelor.event.db.Event;

public interface EventService {
  
  public Event compute(Event event);
  public boolean importCsvFile(File csvFile);
  
}
