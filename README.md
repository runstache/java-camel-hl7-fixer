# HL7 Fixer Application

Apache Camel Application to fix HL7 Messages and replay them to a host and port.

## Setup Options

The Options for setting up this item are defined in the __application.properties__ file of the project.

* __app.host__: This is the HL7 Host to replay the Messages to.
* __app.port__: This is the port to send the message to of the HL7 Host.
* __app.sourceDirectory__: This is the local source directory to process the HL7 Messages from.

## Usage

The application works by ingesting files from the defined source directory that contain the HL7 messages to process.  The application expects each file to only contain a single message.  Once the content is ingested, it will remove any extra line breaks and ASCII control Characters.

_This application assumes that the Carriage Return character is used to denote the end of an HL7 Segment.  The application will remove any Line Feeds in the HL7 message string._
