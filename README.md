\# PTCL - Custom Offer VAS (Channel)



This is a spring boot application that runs in three phases.

\#### 1. Get Customer Profile

\#### 2. Bolt On Verification

\#### 3. Add/Remove Vas



First of all get the customer profile. Based on profile's virtual number and the given part number, 

verify the bolt on. If the verification is successful and the list of install vas is empty, then

add/remove vas via their specific channel



\## Event Names \& Phase Values in application.yml file

It sends different events for each phase

\- Get Customer Profile

&#x20; - The value of \*\*vas.phase\*\* is `profile`

&#x20; - Custom event names are

&#x20;   - `boltonProfileFail`

&#x20;   - `boltonProfileSuccess`

\- Bolt On Verification

&#x20; - The value of \*\*vas.phase\*\* is `verify`

&#x20; - Custom event names are

&#x20;   - `boltonVerificationFail`

&#x20;   - `boltonVerificationSuccess`

\- Add/Remove Vas

&#x20; - The value of \*\*vas.phase\*\* is `action`

&#x20;   - `customOfferFail`

&#x20;   - `customOfferSubmitted`



\## Sample curl requests for testing

Sample values of \*\*serviceIdentifier\*\* are `0515191588`, `0512375898`, `0512300858` and part number is `VAS\_K24`



\### Get Customer Profile

```shell

curl --request POST --location 'http://10.254.172.236:7800/GetCustomerProfile/GetCustomerProfile' \\

\--header 'X-Requested-With: XMLHttpRequest' \\

\--header 'Access-Control-Allow-Credentials: true' \\

\--header 'Content-Type: application/xml' \\

\--data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:get="http://GetCustomerProfile">

&#x20;  <soapenv:Header/>

&#x20;  <soapenv:Body>

&#x20;     <get:GetCustomerProfile>

&#x20;        <SecurityParam>

&#x20;           <UserName>CRM</UserName>

&#x20;           <Password>123</Password>

&#x20;           <IP>?</IP>

&#x20;        </SecurityParam>

&#x20;        <RequestParam>

&#x20;           <ServiceIdentifier>0512300858</ServiceIdentifier>

&#x20;           <ServiceIdentifierType></ServiceIdentifierType>

&#x20;        </RequestParam>

&#x20;     </get:GetCustomerProfile>

&#x20;  </soapenv:Body>

</soapenv:Envelope>'

```



\### Bolt On Verification

//sample values of \*\*serviceId\*\* which are primarily virtual numbers are 051999186138, 0512375898, 051999230962

```shell

curl --request POST --location 'http://esb-prd1.ptclgroup.com:7855/BoltonVerification?wsdl' \\

\--header 'X-Requested-With: XMLHttpRequest' \\

\--header 'Access-Control-Allow-Credentials: true' \\

\--header 'Content-Type: application/xml' \\

\--data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:bol="http://www.ptcl.net.pk/BoltonVerification/">

&#x20;  <soapenv:Header/>

&#x20;  <soapenv:Body>

&#x20;     <bol:BoltonVerificationRequest>

&#x20;        <bol:securityParam>

&#x20;           <bol:username>IIB</bol:username>

&#x20;           <bol:password>IIB</bol:password>

&#x20;        </bol:securityParam>

&#x20;        <bol:boltonPartNumber>VAS\_K24</bol:boltonPartNumber>

&#x20;        <bol:serviceId>051999186138</bol:serviceId>

&#x20;        <bol:action>Add</bol:action>

&#x20;     </bol:BoltonVerificationRequest>

&#x20;  </soapenv:Body>

</soapenv:Envelope>'

```

\### Add Vas

```shell

curl --request POST --location 'http://10.254.172.236:7800/AddVas/AddVas' \\

\--header 'X-Requested-With: XMLHttpRequest' \\

\--header 'Access-Control-Allow-Credentials: true' \\

\--header 'Content-Type: application/xml' \\

\--data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:add="http://AddVas">

&#x20;  <soapenv:Header/>

&#x20;  <soapenv:Body>

&#x20;     <add:AddVas>

&#x20;        <SecurityParam>

&#x20;           <UserName>CRM</UserName>

&#x20;           <Password>123</Password>

&#x20;           <IP>?</IP>

&#x20;        </SecurityParam>

&#x20;        <PSTN>0515191588</PSTN>

&#x20;        <IntegrationId>100829365214</IntegrationId>

&#x20;        <ServiceID>VAS\_K24</ServiceID>

&#x20;     </add:AddVas>

&#x20;  </soapenv:Body>

</soapenv:Envelope>'

```

\### Remove Vas

Client does not need this service

```shell

curl --request POST --location 'http://10.254.172.236:7800/RemoveVas/RemoveVas' \\

\--header 'X-Requested-With: XMLHttpRequest' \\

\--header 'Access-Control-Allow-Credentials: true' \\

\--header 'Content-Type: application/xml' \\

\--data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rem="http://RemoveVas">

&#x20; <soapenv:Header/>

&#x20; <soapenv:Body>

&#x20;   <rem:RemoveVas>

&#x20;     <SecurityParam>

&#x20;       <UserName>CRM</UserName>

&#x20;       <Password>123</Password>

&#x20;       <IP>?</IP>

&#x20;     </SecurityParam>

&#x20;     <PSTN>04235463542</PSTN>

&#x20;     <IntegrationId>100775510213</IntegrationId>

&#x20;     <ServiceID>VAS\_K13</ServiceID>

&#x20;   </rem:RemoveVas>

&#x20; </soapenv:Body>

</soapenv:Envelope>'

```

\## Database Schema

\#### Bolt On Customer Profile

```sql

CREATE TABLE IF NOT EXISTS evam.ptcl\_bolton\_customer\_profile

(

&#x20;   id serial,

&#x20;   insert\_time timestamp(0) without time zone NOT NULL DEFAULT CURRENT\_TIMESTAMP,

&#x20;   debug\_mode character varying(255) NOT NULL,

&#x20;   landline character varying(255),

&#x20;   response\_code character varying(255),

&#x20;   response\_description character varying(255),

&#x20;   response\_product\_type character varying(255),

&#x20;   response\_data\_rate character varying(255),

&#x20;   response\_virtual\_number character varying(255),

&#x20;   response\_integration\_id character varying(255),

&#x20;   response\_mobile\_number character varying(255),

&#x20;   response\_account\_id character varying(255),

&#x20;   response\_xml text

);

```



\#### Bolt On Verification

```sql

CREATE TABLE IF NOT EXISTS evam.ptcl\_bolton\_verification

(

&#x20;   id serial,

&#x20;   insert\_time timestamp(0) without time zone NOT NULL DEFAULT CURRENT\_TIMESTAMP,

&#x20;   debug\_mode character varying(255) NOT NULL,

&#x20;   part\_number character varying(255),

&#x20;   virtual\_number character varying(255),

&#x20;   action character varying(255),

&#x20;   response\_code character varying(255),

&#x20;   response\_description character varying(255),

&#x20;   response\_integrationId character varying(255),

&#x20;   response\_installed\_vas character varying(255),

&#x20;   response\_xml text

);

```



\#### Bolt On Add/Remove Vas

```sql

CREATE TABLE IF NOT EXISTS evam.ptcl\_bolton\_vas

(

&#x20;   id serial,

&#x20;   offer\_code character varying(255) NOT NULL,

&#x20;   offer\_uuid character varying(255) NOT NULL,

&#x20;   scenario\_name character varying(255) NOT NULL,

&#x20;   segment\_code character varying(255) NOT NULL,

&#x20;   insert\_time timestamp(0) without time zone NOT NULL DEFAULT CURRENT\_TIMESTAMP,

&#x20;   debug\_mode character varying(255) NOT NULL,

&#x20;   actor\_id character varying(255) NOT NULL,

&#x20;   action character varying(255) NOT NULL,

&#x20;   pstn character varying(255) NOT NULL,

&#x20;   integration\_id character varying(255) NOT NULL,

&#x20;   service\_id character varying(255) NOT NULL,

&#x20;   response\_error\_code character varying(255) NOT NULL,

&#x20;   response\_error\_message character varying(255) NOT NULL,

&#x20;   response\_order\_id character varying(255) NOT NULL,

&#x20;   response\_status character varying(255) NOT NULL,

&#x20;   response\_xml text,

&#x20;   channel\_type text

)

```



\## Offer-Processor module's application.yml

\*\*kafka.producer.offer-record-topic property must be enabled.\*\*

```

kafka:

&#x20;…

&#x20;producer:

&#x20;  ...

&#x20;  offer-record-topic: success-offer-record # Remove comment this line

```



The custom offer template module is a spring boot project (consume topic: success-offer-record).



If a custom offer is earned successfully in the offer-processor module, the custom offer model is sent to the kafka success-offer-record topic. 

This sent action is turned off by default.  

When such an action is requested, the offer-processor application.yml kafka.producer.offer-record-topic property must be enabled.



Business logic is developed by inheriting the AbstractOfferService abstract class.



For example, there is the MockOfferServiceImpl class.



The event is sent to the engine with the sendEvent methods in the abstract (AbstractOfferService) class. This option is available as optional.



\*\*Default Port:\*\* 9998  

\*\*Default Context Path:\*\* /offer-integration-template



\*\*Health Endpoint:\*\* /offer-integration-template/health

\## REQUIREMENTS

```

OpenJDK 8

Lombok IntelliJ plugin

IntelliJ IDEA (Compiler > Annotation Processors > Enable annotation processing active for lombok)

```

\## BUILD

```

./mvnw clean package

```

Output: marketing-offer-integration-template-\*\*version\*\*.zip in target folder

\## HOW TO RUN

Update application.yml in config folder (change kafka.bootstrap-address)

\### Manual

```

java -jar marketing-offer-integration-template-\_\_VERSION\_\_.jar

```

\### Wrapper (Recommended)

```

chmod +x bin/\*

```

\#### Start

```

./marketing-offer-integration-template start

```

\#### Stop

```

./marketing-offer-integration-template stop

```

\#### Status

```

./marketing-offer-integration-template status

```





