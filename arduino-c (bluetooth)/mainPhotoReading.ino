#define pin12 A12
#define pin13 A13
#define pin14 A14
#define pin15 A15

double lastVals12[10] = { 0 };
double lastVals13[10] = { 0 };
double lastVals14[10] = { 0 };
double lastVals15[10] = { 0 };

int averagingRange = 10;

void setup() {
  Serial.begin(9600);
  pinMode(pin12, INPUT);  // set pull-up on analog pin 0
  pinMode(pin13, INPUT);  // set pull-up on analog pin 0
  pinMode(pin14, INPUT);  // set pull-up on analog pin 0
  pinMode(pin15, INPUT);  // set pull-up on analog pin 0
}

void loop() {
  delay(100);   // delay a bit

  // read each brightness current value
  double read12 = analogRead(pin12);
  double read13 = analogRead(pin13);
  double read14 = analogRead(pin14);
  double read15 = analogRead(pin15);

  // shift all values down except the last one (id = 9)
  for (int id = 0; id < averagingRange - 1; id += 1) {
    lastVals12[id] = lastVals12[id + 1];
    lastVals13[id] = lastVals13[id + 1];
    lastVals14[id] = lastVals14[id + 1];
    lastVals15[id] = lastVals15[id + 1];
  }

  // put the current value as the last id slot
  lastVals12[9] = read12;
  lastVals13[9] = read13;
  lastVals14[9] = read14;
  lastVals15[9] = read15;

  // set averages
  double avg12 = 0;
  double avg13 = 0;
  double avg14 = 0;
  double avg15 = 0;
  for (int id = 0; id < 10; id += 1) {
    avg12 += lastVals12[id];
    avg13 += lastVals13[id];
    avg14 += lastVals14[id];
    avg15 += lastVals15[id];
  }
  avg12 = avg12 / averagingRange;
  avg13 = avg13 / averagingRange;
  avg14 = avg14 / averagingRange;
  avg15 = avg15 / averagingRange;

  int spacesForReadingVals = 8;
  // print 12
  Serial.print(read12);
  Serial.print(" avg12: ");
  Serial.print("hihihi");

  // print 13
  Serial.print(read13);
  Serial.print(" avg13: ");
  Serial.print(avg13);
  Serial.print(" | ");

  // print 14
  Serial.print(read14);
  Serial.print(" avg14: ");
  Serial.print(avg14);
  Serial.print(" | ");

  // print 15
  Serial.print(read15);
  Serial.print(" avg15: ");
  Serial.println(avg15);

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
