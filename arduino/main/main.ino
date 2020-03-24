void setup() {
  Serial.begin(9600);
  pinMode(13, OUTPUT);
  pinMode(12, OUTPUT);
}

bool led1 = false, led2 = false;
int meter = 0;
int event = -1;

void loop() {
  Serial.println(String(meter));
  if(Serial.available() > 0) {
    if (event < 0) {
      char c = Serial.read();
      if (c == '\n') {
      } else if (c == 'n') {
        event = 1;
      } else if (c == 'f') {
        event = 2;
      }
    } else {
      int v = Serial.read();
      if (event == 1 && v == 1) {
        led1 = true;
      } else if (event == 1 && v == 2) {
        led2 = true;
      } else if (event == 2 && v == 1) {
        led1 = false;
      } else if (event == 2 && v == 2) {
        led2 = false;
      }
      event = -1;
    }
  }
  digitalWrite(13, led1);
  digitalWrite(12, led2);
  meter = analogRead(A5);
  delay(10);
}
