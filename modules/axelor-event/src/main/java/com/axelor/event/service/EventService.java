package com.axelor.event.service;

import com.axelor.event.db.Event;

public interface EventService {
  
  public Event compute(Event event);
  public boolean importCsvFile();
  
}
