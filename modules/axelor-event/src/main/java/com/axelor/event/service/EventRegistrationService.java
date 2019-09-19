package com.axelor.event.service;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public interface EventRegistrationService {

  public EventRegistration compute(Event event, EventRegistration eventRegistration);

  public EventRegistration addEvent(Event event, EventRegistration eventRegistration);

  public boolean checkRegistrationDate(Event event, EventRegistration eventRegistration);

  public boolean chechExceedCondition(Event event, EventRegistration eventRegistration);
}
