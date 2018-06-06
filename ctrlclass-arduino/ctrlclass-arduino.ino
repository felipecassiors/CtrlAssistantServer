#include <SPI.h>
#include <MFRC522.h>
#include <Ethernet.h>
#include <EthernetUdp.h>         // UDP library from: bjoern@cs.stanford.edu 12/30/2008

#define SS_PIN 49
#define RST_PIN 48
MFRC522 mfrc522(SS_PIN, RST_PIN);  // Create MFRC522 instance.
 
// Configurações da interface de rede do Arduino
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};
IPAddress ip(192, 168, 137, 2);
unsigned int localPort = 8888;

// Configurações do servidor
IPAddress ipServer(192, 168, 137, 1);

// buffers for receiving and sending data
char packetBuffer[UDP_TX_PACKET_MAX_SIZE];  //buffer to hold incoming packet,

// An EthernetUDP instance to let us send and receive packets over UDP
EthernetUDP Udp;

void setup() {
  // config Serial
  Serial.begin(9600);

  // start the Ethernet and UDP:
  Ethernet.begin(mac, ip);
  Udp.begin(localPort);
  
  // start RFID
  SPI.begin();
  mfrc522.PCD_Init();
  Serial.println("Aproxime o seu cartao do leitor...\n");

  // config Saida
  pinMode(14, OUTPUT); 
  digitalWrite(14, LOW);
  pinMode(15, OUTPUT); 
  digitalWrite(15, LOW);
}

void sendToServer(String tag) {
  char tagToSend = tag.c_str();
  Udp.beginPacket(ipServer, localPort);
  Udp.write(tagToSend);
  Udp.endPacket();
}

bool receiveFromServer() {
  int packetSize = Udp.parsePacket();
  if (packetSize) {
    Serial.print("Received packet of size ");
    Serial.println(packetSize);
    Serial.print("From ");
    IPAddress remote = Udp.remoteIP();
    for (int i = 0; i < 4; i++) {
      Serial.print(remote[i], DEC);
      if (i < 3) {
        Serial.print(".");
      }
    }
    Serial.print(", port ");
    Serial.println(Udp.remotePort());

    // read the packet into packetBufffer
    Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);
    Serial.println("Contents:");
    Serial.println(packetBuffer);
    
    return strcmp(packetBuffer, "true") == 0;
  }
  return false;
}

bool isAllowed(String tag){ //retorna se o usuario está permitido ou nao
  sendToServer(tag);
  delay(500);
  return receiveFromServer();
}

void check(String tag){
  Serial.println("TAG: " + tag);
  if(isAllowed(tag)){ // usuario permitido
    Serial.println("Usuário autorizado");
    digitalWrite(15, HIGH);
    digitalWrite(14, HIGH); delay(50); digitalWrite(14, LOW); delay(50); 
    digitalWrite(14, HIGH);  delay(50); digitalWrite(14, LOW);
    digitalWrite(15, LOW);
  }else{
    Serial.println("Não está autorizado");
    digitalWrite(14, HIGH); delay(150); digitalWrite(14, LOW); delay(50); 
    digitalWrite(14, HIGH); delay(50); digitalWrite(14, LOW); delay(50); 
    digitalWrite(14, HIGH); delay(50); digitalWrite(14, LOW); delay(50); 
    digitalWrite(14, HIGH); delay(50); digitalWrite(14, LOW);
  }
  delay(1000);
  Serial.println("**********************\n");
}

void rfid(){
  if ( ! mfrc522.PICC_IsNewCardPresent()) return;
  if ( ! mfrc522.PICC_ReadCardSerial()) return;
  String tag= "";
  for (byte i = 0; i < mfrc522.uid.size; i++) 
  {
     tag.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
     tag.concat(String(mfrc522.uid.uidByte[i], HEX));
  }
  tag.toUpperCase();
  check(tag.substring(1));
}

void loop() {
  rfid(); delay(500); // Início do código RFID
} 
