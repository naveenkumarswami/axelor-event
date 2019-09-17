package com.axelor.event.service;

import java.io.File;
import com.axelor.apps.message.db.Message;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public interface EventService {
  
  public Event compute(Event event);
  public boolean importCsvFile(File csvFile);
  public Message sendConfirmationEmail(EventRegistration eventRegistration);
  
}
