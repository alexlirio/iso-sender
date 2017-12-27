## Project ISO 8583 Sender

The Project ISO 8583 Sender creates and sends ISO 8583 messages to an endpoint from a string in the JSON format.


## Installation

1. Download the Git repository project: [iso-sender](https://github.com/alexlirio/iso-sender.git)
2. To create the project's zip package, run the following maven command in the project's root folder: "mvn clean package".
3. The package is created in the project folder as: "target/iso-sender.zip". This file can be extracted anywhere.


## Required Configuration

For operation it is necessary to configure the following files within "cfg /":

1. ** packager.xml**, with the ISO packager used in the application that will receive the ISO message.
2. ** config.properties **, with the values needed to send the ISO message. The configuration of these properties are optional, because in execution we can pass these values as a list of parameters.


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
		argsMap.put("port", "11000");
		argsMap.put("timeout", "300000");
		argsMap.put("xml_iso_packager_file", "cfg/packager.xml");
		argsMap.put("json_request_1", "{\"header\":\"9999\",\"0\":\"0305\",\"3\":\"009500\",\"11\":\"000206\",\"12\":\"171531\",\"41\":\"POS80217\",\"56.1\":\"21\",\"56.2\":\"999999999\"}");
		senderISO.createAndSend(argsMap);
		
3. Trigger using command line without passing parameters: In this case, all settings will be used from the file "cfg/config.properties".

		java -jar iso-sender-0.0.1.jar
		
4. Send using command line passing parameters: In this case, only the past settings will overwrite those used in the file "cfg/config.properties".

		java -jar iso-sender.jar -host 927.0.0.1 -port 91000 -timeout 900000 -xml_iso_packager_file cfg/packager9.xml -json_request_1 {"header":"9999","0":"0305","3":"009500","11":"000206","12":"171531","41":"POS80217","56.1":"21","56.2":"999999999"}

		
5. Notes on Parameters:

- ** json_request_* ** = To send more than one message, you can include more parameters of this type. Ex: json\_request\_1, json\_request\_2, etc.


## API Reference

www.jpos.org

