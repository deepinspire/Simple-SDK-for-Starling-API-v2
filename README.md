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
   - Generating a key pair and upload to the portal
   - Add Sandbox Customer
     - Business / Personal / Joint
   - Add funds to your Business Customer Account
4. Clone and setup "StarlingAPIWrapper" locally
   - Copy key pair to folder "keys"
   - Create new configuration file or change existing "cfg/sandbox.properties" with your current sandbox application data
5. Experiment in the sandbox environment
   - Run simple test (/src/tests/com/deepinspire/sterlingbank/SterlingApiV2WrapperTests.java) for checking that all configured correctly
6. Build and integrate lib to your project
7. Get your application approved

## License
The gem is available as open source under the terms of
the [MIT License](http://opensource.org/licenses/MIT).

## Contributing
All contributions are welcome - just make a pull request, making sure you include tests
and documentation for any public methods, and write a good, informative commit
message/pull request body.
