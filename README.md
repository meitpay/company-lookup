# company-lookup

##### To run the app you need to set some values in src/commonMain/resources/application.conf
See Auth0 and Sentry below on how to obtain these values.

${?AUTHENTICATION_ENDPOINT}
${?API_ENDPOINT}
${?SENTRY_URL}


#### mac/linux
./gradlew run

#### windows
gradlew.bat run


### Auth0
Currently, only the backend can be secured (disabled in dev)
If enabled, you'll get a 401 response when you're trying to access the API.

Head over to https://auth0.com/ and create a free account.
- Follow the quicksteps to create an API.
- replace ${?AUTHENTICATION_ENDPOINT} with your auth0 authentication endpoint (issuer) you'll find this value in the auth0 interface
- replace ${?API_ENDPOINT} with the name of your API (the name you selected when you created the API).

To verify that our API is secured:
1. set the environment to prod
2. Go to auth0 and select your API -> Test -> Select your preferred way to generate a token
3. Test the API e.g.
curl --request GET \
  --url localhost:8080/api/organization \
  --header 'authorization: Bearer YOUR_ACCESS_TOKEN'
  

### Sentry
Create a free account at https://sentry.io/welcome/

Follow the tutorial from sentry and create a new project. Go settings -> your project -> client keys (DSN) and copy the url and replace ${?SENTRY_URL} with the url from sentry.
