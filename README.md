# PTCL - Custom Offer VAS Channel

This is a spring boot application that runs in three phases.
#### 1. Get Customer Profile
#### 2. Bolt On Verification
#### 3. Add/Remove Vas (Channel)

First of all get the customer profile. Based on profile's virtual number and the given part number, 
verify the bolt on. If the verification is successful and the list of install vas is empty, then
add/remove vas via their specific channel

## Event Names & Phase Values in application.yml file
It sends different events for each phase
- Get Customer Profile
  - The value of **vas.phase** is `profile`
  - Custom event names are
    - `boltonProfileFail`
    - `boltonProfileSuccess`
- Bolt On Verification
  - The value of **vas.phase** is `verify`
  - Custom event names are
    - `boltonVerificationFail`
    - `boltonVerificationSuccess`
- Add/Remove Vas
  - The value of **vas.phase** is `action`
    - `customOfferFail`
    - `customOfferSubmitted`

## Sample curl requests for testing
Sample values of **serviceIdentifier** are `0515191588`, `0512375898`, `0512300858` and part number is `VAS_K24`

### Get Customer Profile
```shell
curl --request POST --location 'http://10.254.172.236:7800/GetCustomerProfile/GetCustomerProfile' \
--header 'X-Requested-With: XMLHttpRequest' \
--header 'Access-Control-Allow-Credentials: true' \
--header 'Content-Type: application/xml' \
--data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:get="http://GetCustomerProfile">
   <soapenv:Header/>
   <soapenv:Body>
      <get:GetCustomerProfile>
         <SecurityParam>
            <UserName>CRM</UserName>
            <Password>123</Password>
            <IP>?</IP>
         </SecurityParam>
         <RequestParam>
            <ServiceIdentifier>0512300858</ServiceIdentifier>
            <ServiceIdentifierType></ServiceIdentifierType>
         </RequestParam>
      </get:GetCustomerProfile>
   </soapenv:Body>
</soapenv:Envelope>'
```

### Bolt On Verification
//sample values of **serviceId** which are primarily virtual numbers are 051999186138, 0512375898, 051999230962
```shell
curl --request POST --location 'http://esb-prd1.ptclgroup.com:7855/BoltonVerification?wsdl' \
--header 'X-Requested-With: XMLHttpRequest' \
--header 'Access-Control-Allow-Credentials: true' \
--header 'Content-Type: application/xml' \
--data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:bol="http://www.ptcl.net.pk/BoltonVerification/">
   <soapenv:Header/>
   <soapenv:Body>
      <bol:BoltonVerificationRequest>
         <bol:securityParam>
            <bol:username>IIB</bol:username>
            <bol:password>IIB</bol:password>
         </bol:securityParam>
         <bol:boltonPartNumber>VAS_K24</bol:boltonPartNumber>
         <bol:serviceId>051999186138</bol:serviceId>
         <bol:action>Add</bol:action>
      </bol:BoltonVerificationRequest>
   </soapenv:Body>
</soapenv:Envelope>'
```
### Add Vas
```shell
curl --request POST --location 'http://10.254.172.236:7800/AddVas/AddVas' \
--header 'X-Requested-With: XMLHttpRequest' \
--header 'Access-Control-Allow-Credentials: true' \
--header 'Content-Type: application/xml' \
--data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:add="http://AddVas">
   <soapenv:Header/>
   <soapenv:Body>
      <add:AddVas>
         <SecurityParam>
            <UserName>CRM</UserName>
            <Password>123</Password>
            <IP>?</IP>
         </SecurityParam>
         <PSTN>0515191588</PSTN>
         <IntegrationId>100829365214</IntegrationId>
         <ServiceID>VAS_K24</ServiceID>
      </add:AddVas>
   </soapenv:Body>
</soapenv:Envelope>'
```
### Remove Vas
Client does not need this service
```shell
curl --request POST --location 'http://10.254.172.236:7800/RemoveVas/RemoveVas' \
--header 'X-Requested-With: XMLHttpRequest' \
--header 'Access-Control-Allow-Credentials: true' \
--header 'Content-Type: application/xml' \
--data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rem="http://RemoveVas">
  <soapenv:Header/>
  <soapenv:Body>
    <rem:RemoveVas>
      <SecurityParam>
        <UserName>CRM</UserName>
        <Password>123</Password>
        <IP>?</IP>
      </SecurityParam>
      <PSTN>04235463542</PSTN>
      <IntegrationId>100775510213</IntegrationId>
      <ServiceID>VAS_K13</ServiceID>
    </rem:RemoveVas>
  </soapenv:Body>
</soapenv:Envelope>'
```
## Database Schema
#### Bolt On Customer Profile
```sql
CREATE TABLE IF NOT EXISTS evam.ptcl_bolton_customer_profile
(
    id serial,
    insert_time timestamp(0) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    debug_mode character varying(255) NOT NULL,
    landline character varying(255),
    response_code character varying(255),
    response_description character varying(255),
    response_product_type character varying(255),
    response_data_rate character varying(255),
    response_virtual_number character varying(255),
    response_integration_id character varying(255),
    response_mobile_number character varying(255),
    response_account_id character varying(255),
    response_xml text    
);
```

#### Bolt On Verification
```sql
CREATE TABLE IF NOT EXISTS evam.ptcl_bolton_verification
(
    id serial,
    insert_time timestamp(0) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    debug_mode character varying(255) NOT NULL,
    part_number character varying(255),
    virtual_number character varying(255),
    action character varying(255),
    response_code character varying(255),
    response_description character varying(255),
    response_integrationId character varying(255),
    response_installed_vas character varying(255),
    response_xml text
);
```

#### Bolt On Add/Remove Vas
```sql
CREATE TABLE IF NOT EXISTS evam.ptcl_bolton_vas
(
    id serial,
    offer_code character varying(255) NOT NULL,
    offer_uuid character varying(255) NOT NULL,
    scenario_name character varying(255) NOT NULL,
    segment_code character varying(255) NOT NULL,
    insert_time timestamp(0) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    debug_mode character varying(255) NOT NULL,
    actor_id character varying(255) NOT NULL,
    action character varying(255) NOT NULL,
    pstn character varying(255) NOT NULL,
    integration_id character varying(255) NOT NULL,
    service_id character varying(255) NOT NULL,
    response_error_code character varying(255) NOT NULL,
    response_error_message character varying(255) NOT NULL,
    response_order_id character varying(255) NOT NULL,
    response_status character varying(255) NOT NULL,
    response_xml text,
    channel_type text    
)
```

## Offer-Processor module's application.yml
**kafka.producer.offer-record-topic property must be enabled.**
```
kafka:
 …
 producer:
   ...
   offer-record-topic: success-offer-record # Remove comment this line
```

The custom offer template module is a spring boot project (consume topic: success-offer-record).

If a custom offer is earned successfully in the offer-processor module, the custom offer model is sent to the kafka success-offer-record topic. 
This sent action is turned off by default.  
When such an action is requested, the offer-processor application.yml kafka.producer.offer-record-topic property must be enabled.

Business logic is developed by inheriting the AbstractOfferService abstract class.

For example, there is the MockOfferServiceImpl class.

The event is sent to the engine with the sendEvent methods in the abstract (AbstractOfferService) class. This option is available as optional.

**Default Port:** 9998  
**Default Context Path:** /offer-integration-template

**Health Endpoint:** /offer-integration-template/health
## REQUIREMENTS
```
OpenJDK 8
Lombok IntelliJ plugin
IntelliJ IDEA (Compiler > Annotation Processors > Enable annotation processing active for lombok)
```
## BUILD
```
./mvnw clean package
```
Output: marketing-offer-integration-template-**version**.zip in target folder
## HOW TO RUN
Update application.yml in config folder (change kafka.bootstrap-address)
### Manual
```
java -jar marketing-offer-integration-template-__VERSION__.jar
```
### Wrapper (Recommended)
```
chmod +x bin/*
```
#### Start
```
./marketing-offer-integration-template start
```
#### Stop
```
./marketing-offer-integration-template stop
```
#### Status
```
./marketing-offer-integration-template status
```

