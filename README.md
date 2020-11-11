# company-lookup

###To run the app

####mac/linux
./gradlew clean build
./gradlew run

####windows
gradlew.bat clean build
gradlew.bat run


###Auth0
Currently the backend can be secured (disabled in dev)
If enabled, you'll get a 401 response when trying to access the API.

To use this you need an auth0 account, you can create a free account at https://auth0.com/
Follow the quicksteps to create an API.

In our application.conf file we these values we need to get from auth0

auth0 {
    issuer = ${?AUTHENTICATION_ENDPOINT}
    audience = ${?API_ENDPOINT}
}

issuer
- You'll find in the auth0 interface

audience
- the identifier/name of your api

Copy the values to the environment variables or replace 
${?AUTHENTICATION_ENDPOINT} ${?API_ENDPOINT} with the values from auth0

To verify that our API is secured:
1. set the environment to prod
2. Go to auth0 and select your API -> Test -> Select your preferred way to generate a token
3. Test the API e.g.
curl --request GET \
  --url localhost:8080/api/organization \
  --header 'authorization: Bearer YOUR_ACCESS_TOKEN'
  

###Sentry
Create a free account at https://sentry.io/welcome/

in application.conf we have
sentry {
    url = ${SENTRY_URL}
}

Follow the tutorial from sentry and create a new project.
Go settings -> your project -> client keys (DSN) and copy the url to your
environment variables/replace ${SENTRY_URL} with the url
