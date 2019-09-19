package com.axelor.event.service;

import java.io.File;
import java.util.Map;
import com.axelor.apps.message.db.Message;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public interface EventService {

  public Event compute(Event event);

  public Boolean importCsvFile(File csvFile, Map<String, Object> importContext);

  public Message sendConfirmationEmail(EventRegistration eventRegistration);
}
