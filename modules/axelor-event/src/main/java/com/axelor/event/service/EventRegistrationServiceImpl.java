package com.axelor.event.service;

import java.time.Period;
import java.util.Comparator;
import java.util.List;
import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EventRegistrationServiceImpl implements EventRegistrationService {

  @Inject EventRegistrationRepository eventRegistrationRepository;

  @Override
  public EventRegistration compute(Event event, EventRegistration eventRegistration) {
    if (event.getRegistrationCloseDate() != null
        && eventRegistration.getRegistrationDateT() != null) {

      Integer diffCloseAndRegist =
          Period.between(
                  eventRegistration.getRegistrationDateT().toLocalDate(),
                  event.getRegistrationCloseDate())
              .getDays();

      List<Discount> discountList = event.getDiscountList();
      Discount discount = null;
      if (discountList != null) {
        discount =
            discountList
                .stream()
                .filter(discountObj -> discountObj.getBeforeDays() <= diffCloseAndRegist)
                .max(Comparator.comparing(Discount::getDiscountAmount))
                .orElse(null);
      }
      if (discount != null) {
        eventRegistration.setAmount(event.getEventFees().subtract(discount.getDiscountAmount()));
      } else {
        eventRegistration.setAmount(event.getEventFees());
      }
    }

    return eventRegistration;
  }

  @Override
  @Transactional
  public EventRegistration addEvent(Event event, EventRegistration eventRegistration) {

    List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();
    eventRegistration = eventRegistrationRepository.find(eventRegistration.getId());
    eventRegistrationList.add(eventRegistration);
    event.setEventRegistrationList(eventRegistrationList);
    eventRegistration.setEvent(event);
    return eventRegistration;
  }

  @Override
  public boolean checkRegistrationDate(Event event, EventRegistration eventRegistration) {

    return event.getRegistrationOpenDate() != null
            && eventRegistration.getRegistrationDateT() != null
            && (event
                    .getRegistrationOpenDate()
                    .isAfter(eventRegistration.getRegistrationDateT().toLocalDate())
                || event
                    .getRegistrationCloseDate()
                    .isBefore(eventRegistration.getRegistrationDateT().toLocalDate()))
        || event.getRegistrationOpenDate() == null
        || eventRegistration.getRegistrationDateT() == null;
  }

  @Override
  public boolean chechExceedCondition(Event event, EventRegistration eventRegistration) {

    return (event.getEventRegistrationList() != null
            && event.getCapacity() <= event.getEventRegistrationList().size())
        || (event.getEventRegistrationList() == null && event.getCapacity() == null
            || event.getCapacity() <= 0);
  }
}
