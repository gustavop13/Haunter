#include "Adafruit_FONA.h"
#include <SoftwareSerial.h>

#define FONA_RX 2
#define FONA_TX 3
#define FONA_RST 4

char replybuffer[255];
SoftwareSerial fonaSS = SoftwareSerial(FONA_TX, FONA_RX);
SoftwareSerial *fonaSerial = &fonaSS;
String val;

Adafruit_FONA fona = Adafruit_FONA(FONA_RST);

uint8_t readline(char *buff, uint8_t maxbuff, uint16_t timeout = 0);
uint8_t type;

void setup() {
  while(!Serial);
  Serial.begin(115200);

  fonaSerial->begin(4800);
  if(!fona.begin(*fonaSerial)) {
    Serial.println(F("Couldn't find FONA"));
    while(1);
  }
  type = fona.type();
  Serial.println(F("FONA is OK"));
}

void loop() {
  while(Serial.available() == 0);
  val  = Serial.readString();
  Serial.println(val);
  if(val.charAt(0) == 'c') {
    call(val.substring(1,11));
    Serial.println("Called!");
  } else if(val.charAt(0) == 's') {
    sms(val.substring(1,11), val.substring(11));
    Serial.println("Sent!");
  } else if(val.charAt(0) == 'r') {
    read_sms();
  } else if(val.charAt(0) == 'd') {
    uint8_t smsn = 1;
    fona.deleteSMS(smsn);
  }
}

void flushSerial() {
  while (Serial.available())
    Serial.read();
}

uint8_t readline(char *buff, uint8_t maxbuff, uint16_t timeout) {
  uint16_t buffidx = 0;
  boolean timeoutvalid = true;
  if (timeout == 0) timeoutvalid = false;

  while (true) {
    if (buffidx > maxbuff) {
      //Serial.println(F("SPACE"));
      break;
    }

    while (Serial.available()) {
      char c =  Serial.read();

      //Serial.print(c, HEX); Serial.print("#"); Serial.println(c);

      if (c == '\r') continue;
      if (c == 0xA) {
        if (buffidx == 0)   // the first 0x0A is ignored
          continue;

        timeout = 0;         // the second 0x0A is the end of the line
        timeoutvalid = true;
        break;
      }
      buff[buffidx] = c;
      buffidx++;
    }

    if (timeoutvalid && timeout == 0) {
      //Serial.println(F("TIMEOUT"));
      break;
    }
    delay(1);
  }
  buff[buffidx] = 0;  // null term
  return buffidx;
}

void call(String num) {
  char phone[21];
  num.toCharArray(phone, 11);
  flushSerial();
  fona.callPhone(phone);
}

void sms(String num, String mes) {
  char sendto[21], message[141];
  num.toCharArray(sendto, 11);
  mes.toCharArray(message, 141);
  flushSerial();
  fona.sendSMS(sendto, message);
}

void read_sms() {
  flushSerial();
  uint8_t smsn = 1;
  uint16_t smslen;
  fona.readSMS(smsn, replybuffer, 250, &smslen);
}

void delete_sms() {
  flushSerial();
  uint8_t smsn = 1;
  fona.deleteSMS(smsn);
}
