## Project ISO 8583 Sender

The Project ISO 8583 Sender creates and sends ISO 8583 messages to an endpoint from a string in the JSON format.


## Installation

1. Download the Git repository project: [iso-sender](https://github.com/alexlirio/iso-sender.git)
2. To create the project's package, run the following maven command in the project's path: "mvn clean package".
3. The package will be create in the project's path as: "target/iso-sender.zip". This file can be extracted anywhere.


## Required Configuration Files

* ** cfg/packager.xml**, with the ISO packager. It's the same ISO packager of application used to send responses.
* ** cfg/config.properties **, with valid values to each property. The file contains comments to help about each property. These properties are optionals, because it's possible pass by parameters.


## Use

Before using the trigger, make sure that the service that will receive the ISO is working.

There are 4 ways to use the project:

1. Send without passing parameters: In this case, all settings will be used from the file "cfg/config.properties".

		SenderISO senderISO = new SenderISO();
		senderISO.createAndSend();
		
2. Send passing parameters: In this case, only the settings passed in the list will overwrite those used in the file "cfg/config.properties".

		SenderISO senderISO = new SenderISO();
		Map<String, String> argsMap = new TreeMap<String, String>();
		argsMap.put("host", "127.0.0.1");
		argsMap.put("port", "12000");
		argsMap.put("timeout", "300000");
		argsMap.put("xml_iso_packager_file", "cfg/packager.xml");
		argsMap.put("json_request_1", "{\"header\":\"0000000000\",\"0\":\"0305\",\"3\":\"009500\",\"4\":\"000000000199\",\"11\":\"000001\",\"12\":\"235959\",\"13\":\"1231\",\"41\":\"POS80217\",\"42\":\"000000000083917\",\"61.1\":\"21\",\"61.2\":\"999999999\",\"61.3\":\"TEST001\",\"62\":\"1.00b01p01#9.51b27#PWWIN#ECF4BBFBC1C2\"}");
		senderISO.createAndSend(argsMap);
		
3. Trigger using command line without passing parameters: In this case, all settings will be used from the file "cfg/config.properties".

		java -jar iso-sender.jar
		
4. Send using command line passing parameters: In this case, only the past settings will overwrite those used in the file "cfg/config.properties".

		java -jar iso-sender.jar -host 127.0.0.1 -port 12000 -timeout 300000 -xml_iso_packager_file cfg/packager.xml -json_request_1 {"header":"0000000000","0":"0305","3":"009500","4":"000000000199","11":"000001","12":"235959","13":"1231","41":"POS80217","42":"000000000083917","61.1":"21","62.2":"999999999","61.3":"TEST001","62":"1.00b01p01#9.51b27#PWWIN#ECF4BBFBC1C2"}

		
5. Notes on Parameters:

- ** json_request_* ** = To send more than one message, you can include more parameters of this type. Ex: json\_request\_1, json\_request\_2, etc.


## API Reference

* [jpos.org](http://www.jpos.org/)
