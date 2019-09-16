package com.axelor.event.web;

import java.io.File;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
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
import java.util.Optional;
import java.util.stream.Collectors;

public class EventController {

  @Inject MetaFileRepository metaFileRepo;

  @Inject MetaFiles metaFiles;
  @Inject EventService eventService;

  public void validateRegistration(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);

    if (event.getEventRegistrationList() != null
        && event.getCapacity() < event.getEventRegistrationList().size()) {
      response.setError(I18n.get(IExceptionMessage.REGISTRATION_EXVEEDS_CAPACITY));
    }

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

    if(event.getEventRegistrationList().size()!=eventReigstrationList.size()) {
    event.setEventRegistrationList(eventReigstrationList);
    response.setValues(event);
//    response.setError(I18n.get(IExceptionMessage.REGISTRATION_DATE));
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

      response.setFlash(I18n.get(IExceptionMessage.IMPORT_COMPLETED_MESSAGE));
    } else {
      response.setFlash(I18n.get(IExceptionMessage.INVALID_DATA_FORMAT_ERROR));
    }
  }
}
