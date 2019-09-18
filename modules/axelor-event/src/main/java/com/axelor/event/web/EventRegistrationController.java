package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventRegistrationController {

  @Inject EventRegistrationService eventRegistrationService;

  public void validateRegistration(ActionRequest request, ActionResponse response) {

    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    Event event;
    try {
      if (eventRegistration.getEvent() == null) {
        event = request.getContext().getParent().asType(Event.class);
        eventRegistration.setEvent(event);
      } else {
        event = eventRegistration.getEvent();
      }

      if (eventRegistrationService.checkRegistrationDate(event, eventRegistration)) {
        response.setError(I18n.get(IExceptionMessage.REGISTRATION_DATE));
      }
      eventRegistration = eventRegistrationService.compute(event, eventRegistration);
      response.setValues(eventRegistration);

      if (eventRegistration.getEvent() != null) {

        if (eventRegistration.getId() == null) {
          if (eventRegistrationService.chechExceedCondition(event, eventRegistration)) {
            response.setError(I18n.get(IExceptionMessage.REGISTRATION_EXCEEDS_CAPACITY));
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addIntoEvent(ActionRequest request, ActionResponse response) {

    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    System.err.println(eventRegistration.getId());

    Event event = eventRegistration.getEvent();
      eventRegistration = eventRegistrationService.addEvent(event, eventRegistration);
      response.setValues(eventRegistration);
  }
}
