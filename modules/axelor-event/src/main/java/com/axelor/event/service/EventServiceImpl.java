package com.axelor.event.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.TemplateRepository;
import com.axelor.apps.message.service.TemplateMessageService;
import com.axelor.data.Importer;
import com.axelor.data.csv.CSVImporter;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.i18n.I18n;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.google.common.io.Files;

public class EventServiceImpl implements EventService {

  protected TemplateMessageService templateMessageService;
  @Inject TemplateRepository templateRepository;

  private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  public EventServiceImpl(TemplateMessageService templateMessageService) {
    this.templateMessageService = templateMessageService;
  }

  @Override
  public Event compute(Event event) {
    List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();
    BigDecimal totalAmount = BigDecimal.ZERO;
    BigDecimal totalDiscount = BigDecimal.ZERO;
    int size = 0;

    if (eventRegistrationList != null) {

      totalAmount =
          eventRegistrationList
              .stream()
              .map(i -> i.getAmount())
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      totalDiscount =
          event
              .getEventFees()
              .multiply(new BigDecimal(eventRegistrationList.size()))
              .subtract(totalAmount);

      size = eventRegistrationList.size();
    }
    event.setTotalEntry(size);
    event.setAmountCollected(totalAmount);
    event.setTotalDiscount(totalDiscount);
    log.debug(
        "Event : Total Entry : {}, Amount Collected : {}, total Discount : {}",
        event.getTotalEntry(),
        event.getAmountCollected(),
        event.getTotalDiscount());
    
    return event;
  }

  @Override
  public Boolean importCsvFile(File file, Map<String, Object> importContext) {

    File tmpDir = null;
    File configXML = null;

    try {
      tmpDir = Files.createTempDir();
      configXML = new File(tmpDir, "input-config.xml");
      InputStream bindInputStream = this.getClass().getResourceAsStream("/demo/input-config.xml");

      if (bindInputStream == null) {
        file.delete();
        throw new Error(I18n.get(IExceptionMessage.CONFIG_FILE_MISSING));
      }
      FileOutputStream outputstream = new FileOutputStream(configXML);
      IOUtils.copy(bindInputStream, outputstream);

    } catch (Exception e) {
      file.delete();
      e.printStackTrace();
    }

    Importer importfile = new CSVImporter(tmpDir + "/input-config.xml", file.getParent());

    importfile.setContext(importContext);
    importfile.run();
    return true;
  }

  @Override
  @Transactional
  public Message sendConfirmationEmail(EventRegistration eventRegistration) {

    try {
      return templateMessageService.generateAndSendMessage(
          eventRegistration, templateRepository.findByName("Event"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
