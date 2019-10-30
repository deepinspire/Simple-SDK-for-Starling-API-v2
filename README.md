# StarlingAPIWrapper
*StarlingAPIWrapper* is a Java library that provides simple access to the Starling Bank API.
It have minimal dependencies and is compatible with Java 1.8+.

## Links
* Documentation here: [https://developer.starlingbank.com/payments/docs](https://developer.starlingbank.com/payments/docs)
* How to getting started: [https://developer.starlingbank.com/get-started](https://developer.starlingbank.com/get-started)
* Swagger 2.0 definition [https://payment-api-sandbox.starlingbank.com/api/swagger.json](https://payment-api-sandbox.starlingbank.com/api/swagger.json)

## Short plan to start
1. Create an Account (Set up a developer account)
2. Register Application in the sandbox environment
 2.1 Generating a key pair and upload to the portal
 2.2 Add Sandbox Customer
  2.2.1 Business / Personal / Joint
 2.3 Add funds to your Business Customer Account
4. Clone and setup "StarlingAPIWrapper" locally
 4.1 Copy key pair to folder "keys"
 4.2 Create new configuration file or change existing "cfg/sandbox.properties" 
    with your current sandbox application data
5. Experiment in the sandbox environment
 5.1 Run simple test (/src/tests/com/deepinspire/sterlingbank/SterlingApiV2WrapperTests.java) for checking that all configured correctly
6. Build and integrate lib to your project
7. Get your application approved

## License
The gem is available as open source under the terms of
the [MIT License](http://opensource.org/licenses/MIT).

## Contributing
All contributions are welcome - just make a pull request, making sure you include tests
and documentation for any public methods, and write a good, informative commit
message/pull request body.
