int ledPin = 13;
int flag = 0;
int receivedData;
void setup() {
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW);
  Serial.begin(9600);
}
void loop() {
  if (Serial.available() > 0) {
    receivedData = Serial.read();
    flag = 1;
  }
  if (flag == 1) {
    switch (receivedData) {
      case '0':
        digitalWrite(ledPin, LOW);
        Serial.println("LED Turned OFF");
        break;
      case '1':
        digitalWrite(ledPin, HIGH);
        Serial.println("LED Turned ON");
        break;
      default:
        Serial.println("Nothing here");
        break;
    }
    flag = 0;
    receivedData = 12;
  }
  Serial.flush();
}
