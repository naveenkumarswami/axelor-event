package com.axelor.event.web;

import java.io.File;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.event.service.EventService;
import com.axelor.i18n.I18n;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.io.Files;
import com.google.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventController {

  @Inject MetaFileRepository metaFileRepo;

  @Inject MetaFiles metaFiles;
  @Inject EventService eventService;
  @Inject EventRepository eventRepository;

  public void validateRegistration(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);
    try {

      List<EventRegistration> eventReigstrationList =
          event
              .getEventRegistrationList()
              .stream()
              .filter(
                  i ->
                      event.getRegistrationOpenDate() != null
                          && !event
                              .getRegistrationOpenDate()
                              .isAfter(i.getRegistrationDateT().toLocalDate())
                          && !event
                              .getRegistrationCloseDate()
                              .isBefore(i.getRegistrationDateT().toLocalDate()))
              .collect(Collectors.toList());

      if (event.getEventRegistrationList().size() != eventReigstrationList.size()) {
        event.setEventRegistrationList(eventReigstrationList);
        response.setValues(event);
        response.setFlash(I18n.get(IExceptionMessage.REGISTRATION_DATE));
      }

      if (event.getEventRegistrationList() != null
          && event.getCapacity() < event.getEventRegistrationList().size()) {
        response.setError(I18n.get(IExceptionMessage.REGISTRATION_EXCEEDS_CAPACITY));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateAmount(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);
    event = eventService.compute(event);
    response.setValues(event);
  }

  public void setRegistration(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);

    Integer id = (Integer) request.getContext().get("showRecords");

    MetaFile metaFile =
        metaFileRepo.find(
            Long.valueOf(((Map) request.getContext().get("importFile")).get("id").toString()));
    File csvFile = MetaFiles.getPath(metaFile).toFile();

    if (Files.getFileExtension(csvFile.getName()).equals("csv")) {
      File dataDir = Files.createTempDir();
      System.err.println(dataDir.getAbsolutePath());
      //      System.err.println(csvFile.getParent());
      eventService.importCsvFile(csvFile);
      csvFile.delete();
      response.setFlash(I18n.get(IExceptionMessage.IMPORT_COMPLETED_MESSAGE));
    } else {
      response.setFlash(I18n.get(IExceptionMessage.INVALID_DATA_FORMAT_ERROR));
    }
  }

  public void sendEmail(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);

    try {
      event = eventRepository.find(event.getId());

      MetaFile logFile = eventService.sendEmail(event);

      if (logFile == null) {
        response.setFlash(I18n.get(IExceptionMessage.EMAIL_SUCCESS));
      } else {
        response.setFlash(I18n.get(IExceptionMessage.EMAIL_ERROR2));
      }
    //  response.setValue("emailLog", logFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
