package com.axelor.event.csv.script;

import java.util.List;
import java.util.Map;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.event.service.EventService;
import com.google.inject.Inject;

public class EventRegistrationImporter {

  @Inject EventRepository eventRepository;
  @Inject EventRegistrationRepository eventRegistrationRepository;
  @Inject EventRegistrationService eventRegistrationService;
  @Inject EventService eventService;

  public Object checkValues(Object bean, Map<String, Object> values) {

    assert bean instanceof EventRegistration;
    EventRegistration eventRegistration = (EventRegistration) bean;

    Long id = (Long) values.get("_eventId");
    Event event = eventRepository.find(id);
    
    try {

    if (eventRegistrationService.chechExceedCondition(event, eventRegistration)
        || eventRegistrationService.checkRegistrationDate(event, eventRegistration)) {
      return null;
    }

    if (bean != null && eventRegistration != null) {

      List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();
      eventRegistrationList.add(eventRegistration);
      eventRegistration.setEvent(event);
      eventRegistration = eventRegistrationService.compute(event, eventRegistration);
      eventRegistration = eventRegistrationRepository.save(eventRegistration);
      event.setEventRegistrationList(eventRegistrationList);
      event = eventRepository.save(event);
      event = eventService.compute(event);
      eventRepository.save(event);
      return eventRegistration;
    }
    }catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
