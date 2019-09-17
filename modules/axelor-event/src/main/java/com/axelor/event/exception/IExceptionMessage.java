package com.axelor.event.exception;

public interface IExceptionMessage {

  static final String REGISTRATION_EXCEEDS_CAPACITY = /*$$(*/ "Registration Exceeds Capacity" /*)*/;
  /** Event */
  static final String REGISTRATION_DATE = /*$$(*/ "Registration date should be between open and close registration dates " /*)*/;
  
  public static final String INVALID_DATA_FORMAT_ERROR = /*$$(*/
      "Invalid data format. Please select csv file only." /*)*/;
  
  public static final String IMPORT_COMPLETED_MESSAGE = /*$$(*/
      "Import completed successfully" /*)*/;
  

  static final String EMAIL_ERROR1 = /*$$(*/
      "Error in sending an email to the following targets" /*)*/;

  static final String EMAIL_ERROR2 = /*$$(*/
      "Error in sending emails" /*)*/;

  static final String EMAIL_SUCCESS = /*$$(*/ "Emails sent successfully" /*)*/;


}
