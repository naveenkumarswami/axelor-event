package com.axelor.event.service;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public interface EventRegistrationService {
  
  public EventRegistration compute(Event event, EventRegistration eventRegistration);
  
}
