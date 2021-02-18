#define pin15 A15

double lastVals15[10] = { 0 };

int averagingRange = 10;

void setup() {
  Serial.begin(9600);
  pinMode(pin15, INPUT);  // set pull-up on analog pin 0
}

void loop() {
  delay(100);   // delay a bit

  // read each brightness current value
  double read15 = analogRead(pin15);

  // shift all values down except the last one (id = 9)
  for (int id = 0; id < averagingRange - 1; id += 1) {
    lastVals15[id] = lastVals15[id + 1];
  }

  // put the current value as the last id slot
  lastVals15[9] = read15;

  // set averages
  double avg15 = 0;
  for (int id = 0; id < 10; id += 1) {
    avg15 += lastVals15[id];
  }
  avg15 = avg15 / averagingRange;

  int spacesForReadingVals = 8;

  // print 15
  printf(read15, spacesForReadingVals);
  Serial.print(" avg15: ");
  printf(avg15, spacesForReadingVals);
  Serial.println("");


}


// prints number with spaces; LEN IS THE TOTAL STRING LENGTH WANTED
void printf(double number, int len) {     // for printing floats (will have .00 at the end)
  int spacesNeeded = 0;

  Serial.print(number);
  if (number < 10)         // x.00 --> 4
    spacesNeeded = len - 4;
  else if (number < 100)   // xx.00 --> 5
    spacesNeeded = len - 5;
  else if (number < 1000)  // xxx.00 --> 6
    spacesNeeded = len - 6;
  else if (number < 10000) // xxxx.00 --> 7
    spacesNeeded = len - 7;

  if (spacesNeeded <= 0){
    Serial.print("hello");
    return;
  }

  for (int x = 0; x < spacesNeeded; x += 1)
    Serial.print(" ");
}
