# OAuth2 and OpenID connect authorization server using Spring Boot Starter OAuth2 Authorization Server

## Steps for testing

- Run the app

- Open a new tab on the browser and type this url

```plaintext
http://localhost:8080/oauth2/authorize?response_type=code&client_id=oidc-client&scope=openid&redirect_uri=https://springone.io/authorized&code_challenge=CRq1PsYf6Q-pwEHavwO3AJ0BDhWDzu04cSilvd7ke7s&code_challenge_method=S256
```

- Login using the user `user` and password `password`

- If the login works you will be redirected to the `springone.io/authorized?code={code}` page

- Copy the `{code}` for later usage

- Create a new POST request to the `http:localhost:8080/oauth2/token` endpoint using curl, Postman or similar. It will use Basic authentication for client authentication using the base64 string of oidc-client@secret as configured in the registeredClientRepository Bean. Replace {code}  with the code returned in the previous step.

```bash
curl --location 'http://localhost:8080/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic b2lkYy1jbGllbnQ6c2VjcmV0' \
--data-urlencode 'redirect_uri=https://springone.io/authorized' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'code={code} \
--data-urlencode 'code_verifier=15Ysb3kNvbyLtSQrCOuXIxZ9z91LRkLukfDdJDMeCEo'
```

- This should return the `access_token` in the response
