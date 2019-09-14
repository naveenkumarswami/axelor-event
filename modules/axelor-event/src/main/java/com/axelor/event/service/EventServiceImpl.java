package com.axelor.event.service;

import com.axelor.data.Importer;
import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.event.db.Event;

public class EventServiceImpl implements EventService{

  @Override
  public Event compute(Event event) {
    return null;
  }

  @Override
  public boolean importCsvFile() {
    
    Importer importfile = new CSVImporter(
        "/home/axelor/Projects/Gst-Project/axelor-gst-app/modules/axelor-gst/src/main/resources/data-demo/input-config.xml",
        "/home/axelor/Projects/Gst-Project/axelor-gst-app/modules/axelor-gst/src/main/resources/data-demo/input");
    Listener listener =
        new Listener(){
        
          @Override
          public void imported(Integer total, Integer success) {
            // TODO Auto-generated method stub
            
          }
        
          @Override
          public void imported(Model bean) {
            // TODO Auto-generated method stub
            
          }
        
          @Override
          public void handle(Model bean, Exception e) {
            // TODO Auto-generated method stub
            
          }
        };
        
        importfile.addListener(listener);
        importfile.run();

    
    return true;
  }

 }
