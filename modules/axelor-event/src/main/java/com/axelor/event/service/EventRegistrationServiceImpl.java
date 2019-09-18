package com.axelor.event.service;

import java.time.Period;
import java.util.Comparator;
import java.util.List;
import com.axelor.db.Query;
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

    //    int diffCloseAndRegist = event.getRegistrationCloseDate().getDayOfMonth() -
    // eventRegistration.getRegistrationDateT().getDayOfMonth();

    //    System.out.println("diff :"+diff );

    Integer diffCloseAndRegist =
        Period.between(
                eventRegistration.getRegistrationDateT().toLocalDate(),
                event.getRegistrationCloseDate())
            .getDays();
    //    System.out.println("diff :" + diff);
    //    Integer diffCloseAndRegist =
    //        event
    //            .getRegistrationCloseDate()
    //            .compareTo(eventRegistration.getRegistrationDateT().toLocalDate());
    System.err.println("differ :" + diffCloseAndRegist);
    List<Discount> discountList = event.getDiscountList();
    Discount discount = null;
    if (discountList != null) {
      discount =
          discountList
              .stream()
              .filter(a -> a.getBeforeDays() <= diffCloseAndRegist)
              .max(Comparator.comparing(Discount::getDiscountAmount))
              .orElse(null);
    }
    if (discount != null) {
      eventRegistration.setAmount(event.getEventFees().subtract(discount.getDiscountAmount()));
      System.err.println(discount);
    } else {
      eventRegistration.setAmount(event.getEventFees());
    }

    return eventRegistration;
  }

  @Override
  @Transactional
  public EventRegistration addEvent(Event event, EventRegistration eventRegistration) {

    List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();
    eventRegistration = eventRegistrationRepository.find(eventRegistration.getId());
    System.err.println(eventRegistration);
    eventRegistrationList.add(eventRegistration);
    event.setEventRegistrationList(eventRegistrationList);
    eventRegistration.setEvent(event);
    return eventRegistration;
  }

  @Override
  public boolean checkRegistrationDate(Event event, EventRegistration eventRegistration) {

    return event.getRegistrationOpenDate() != null
            && event
                .getRegistrationOpenDate()
                .isAfter(eventRegistration.getRegistrationDateT().toLocalDate())
        || event
            .getRegistrationCloseDate()
            .isBefore(eventRegistration.getRegistrationDateT().toLocalDate());
  }

  @Override
  public boolean chechExceedCondition(Event event, EventRegistration eventRegistration) {

    return (event.getEventRegistrationList() != null
            && event.getCapacity() <= event.getEventRegistrationList().size())
        || (event.getEventRegistrationList() == null && event.getCapacity() == null
            || event.getCapacity() <= 0);
  }

  @Override
  @Transactional
  public void removeEventRegistration() {

    Query<EventRegistration> eventRegistrationList = eventRegistrationRepository.all();
    eventRegistrationList.fetchSteam().filter(a -> a.getEvent()==null).forEach(a -> eventRegistrationRepository.remove(a));

    return ;
  }
}
