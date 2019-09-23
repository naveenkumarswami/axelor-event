package com.axelor.event.web;

import java.io.File;
import com.axelor.apps.message.db.Message;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.event.service.EventService;
import com.axelor.i18n.I18n;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventController {

  @Inject MetaFileRepository metaFileRepo;

  @Inject MetaFiles metaFiles;
  @Inject EventService eventService;
  @Inject EventRegistrationRepository eventRegistrationRepository;
  @Inject EventRegistrationService eventRegistrationService;

  public void validateRegistration(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);
    try {

      List<EventRegistration> eventReigstrationList =
          event
              .getEventRegistrationList()
              .stream()
              .filter(eventRegistration -> !eventRegistrationService.checkRegistrationDate(event, eventRegistration))
              .collect(Collectors.toList());

      if (event.getEventRegistrationList().size() != eventReigstrationList.size()) {
        event.setEventRegistrationList(eventReigstrationList);
        response.setValues(event);
        response.setFlash(I18n.get(IExceptionMessage.REGISTRATION_DATE));
      }

      if (event.getEventRegistrationList() != null
          && event.getCapacity() < event.getEventRegistrationList().size()) {
        event.setEventRegistrationList(eventReigstrationList.subList(0, event.getCapacity()));
        response.setValues(event);
        response.setFlash(I18n.get(IExceptionMessage.REGISTRATION_EXCEEDS_CAPACITY));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Transactional
  public void updateAmount(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);
    event = eventService.compute(event);
    response.setValues(event);
  }

  public void setRegistration(ActionRequest request, ActionResponse response) {

    Integer id = (Integer) request.getContext().get("showRecord");
    Map<String, Object> importContext = new HashMap<String, Object>();
    importContext.put("_eventId", id.longValue());

    MetaFile metaFile =
        metaFileRepo.find(
            Long.valueOf(((Map) request.getContext().get("importFile")).get("id").toString()));
    File csvFile = MetaFiles.getPath(metaFile).toFile();

    if (Files.getFileExtension(csvFile.getName()).equals("csv")) {
      eventService.importCsvFile(csvFile, importContext);
      csvFile.delete();
      response.setFlash(I18n.get(IExceptionMessage.IMPORT_COMPLETED_MESSAGE));
    } else {
      csvFile.delete();
      response.setFlash(I18n.get(IExceptionMessage.INVALID_DATA_FORMAT_ERROR));
    }
  }

  @Transactional
  public void sendEmail(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);
    List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();

    try {

      for (EventRegistration eventRegistration : eventRegistrationList) {
        eventRegistration = eventRegistrationRepository.find(eventRegistration.getId());

        if (eventRegistration.getEmail() != null && eventRegistration.getEmailSend() != true) {

          Message message = eventService.sendConfirmationEmail(eventRegistration);
          if (message != null) {
            response.setFlash(I18n.get(IExceptionMessage.EMAIL_SUCCESS));
            eventRegistration.setEmailSend(true);
            eventRegistrationRepository.save(eventRegistration);
          } else {
            response.setFlash(I18n.get(IExceptionMessage.EMAIL_ERROR1));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      response.setFlash(I18n.get(IExceptionMessage.EMAIL_ERROR2));
    }
  }
}
