FORMAT: 1A
HOST: api.getfreshspired.com

# Freshspire API

**TODO: /chains**

**TODO: David - look over /stores**

The Freshspire API exposes real-time discount information in grocery stores. More 
specifically, it exposes discount information on expiring food categories such as
fresh produce, meats, and dairy products.

All ```POST``` request parameters should be encoded in json in the request body.

API key should be included as a *request parameter* (```?apiKey=...```) in all endpoints not
under ```/users```. This doesn't necessarily apply to endpoints under ```/users``` since
there are certain endpoints that don't require an API key, require it in a request body, etc.


**Backend repository (Github):** [link](https://github.com/reesjones/freshspire)

**Freshspire app repository (Bitbucket):** [link](https://bitbucket.org/bigpixelnc/app)

# Group User Resources

All use account stuffs. Here, you can create users, deal with forgotten passwords, etc.

Each phone number may have only one account associated with it.

## Users [/users]

This endpoint contains all resources and methods regarding the user account system. 

You can create/delete/update users and login/authenticate.

## Create a user [/users/create{?phoneNumber}]
This endpoint allows a client to create a user. This is done in two requests:

 1. The client sends a ```GET``` request to ```/users/create``` with ```phoneNumber``` as
 a URL parameter. This will send a confirmation code to the number provided.
 2. Once the client has gotten their code, they ```POST``` to ```/users/create``` with
 ```firstName```, ```phoneNumber```, ```password```, and ```confirmationCode``` as json
 parameters in the request body. If the phone number and code are correct, the user is created
 and added to the database and the user object in JSON format is returned, which includes
 the API key needed for future requests.

Only one account can be created per phone number. API calls to create an account with a
phone number already associated with an account will return unsuccessfully.

### GET Send verification code to phone number [GET]
**Unit tested**
This is the first request to make in the create user process. This accepts the 
user's phone number and sends a verification code to the phone number provided.

+ Parameters
    + phoneNumber: 1234567890 (string, required) - The user's phone number

+ Response 200 (application/json)

        {
            "status": "ok",
            "message": "Verification code sent to <phone number>."
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Could not send verification code. Is phone number formatted correctly?"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "User already exists for phone number <phoneNumber>"
        }

+ Response 503 (application/json)

        {
            "status": "error",
            "message": "Internal server error"
        }

### POST Use confirmation code to create user [POST]
**Unit tested**
Once the client has received their verification code, they ```POST``` to ```/users/create```
with their first name, phone number, new password, and code as json parameters in the body.
If the phone number and code are correct, the user is created and the user object is returned.

+ Request (application/json)
    + Attributes
        + firstName: test         (string, required) - The user's first name
        + phoneNumber: 1234567890 (string, required) - The user's phone number
        + verificationCode: 12345 (string, required) - The verification code sent to the user's phone number (from the POST request)
        + newPassword: newPass123 (string, required) - The new password of the user


+ Response 201 (application/json)

        {
            "apiKey": "YOUR_API_KEY",
            "enabledLocation": true,
            "firstName": "test",
            "phoneNumber": "1234567890",
            "userId": "1111111111"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Password parameter cannot be empty"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "First name parameter cannot be empty"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Invalid request parameters"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Account already exists with phone number"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Invalid phone number/authentication code pair"
        }

+ Response 500 (application/json)

        {
            "status": "error",
            "message": "Internal server error"
        }

## Resources for a specific user [/users/{userId}]
Methods specific to a user registered with Freshspire.

+ Parameters
    + userId: 1111111111 (string, required) - The unique ID of the user. Specified in the URL.

### DELETE Delete user [DELETE]
**Unit tested**

This deletes a user. The request body must include ```apiKey```. Response 
code ```200 OK``` for successful deletion, otherwise a response code ```4xx``` or ```5xx``` 
will be returned with an error message.

+ Request (application/json)
    + Attributes
        + apiKey: YOUR\_API\_KEY (string, required) - The user's API key.

+ Response 200 (application/json)

        {
            "message": "Successfully deleted user"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "User ID/API key pair incorrect"
        }

+ Response 500 (application/json)

## User's enabledLocation [/users/{userId}/enabledLocation{?apiKey}]
This is a boolean value which indicates if the user has enabled location services on their mobile device.

Returns ```404 Not Found``` if the ```userId``` is not valid.

+ Parameters
    + userId: 1111111111 (string, required) - the ID of the user
    + apiKey: YOUR\_API\_KEY (string, required) - The user's API key.

### GET Get enabledLocation [GET]
**Unit tested**

Gets the enabledLocation boolean property of the user.

Returns ```404 Not Found``` if the ```userId``` is not valid.

+ Response 200 (application/json)

        {
            "enabledLocation": false
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "API key cannot be empty"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "User ID/API key pair incorrect"
        }

+ Response 404 (application/json)

        {
            "status": "error",
            "messsage": "User not found"
        }

### PUT Update enabledLocation [PUT]
**Unit tested**

Updates the enabledLocation property of the user. Include ```apiKey``` and ```enabledLocation``` parameters as 
json in the request body.

Returns ```404 Not Found``` if the ```userId``` is not valid.

+ Request (application/json)
    + Attributes
        + enabledLocation: false (boolean, required) - True if the user has enabled location 
        services for Freshspire on their mobile device.

+ Response 200 (application/json)

        {
            "status": "ok",
            "message": "Successfully updated enabledLocation"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "User ID/API key pair incorrect"
        }

+ Response 404 (application/json)

        {
            "status": "error",
            "messsage": "User not found"
        }

## Forgot Password [/users/forgot-password{?phoneNumber}]

This endpoint allows a client to both login and update their password if they've forgotten it. This is done in two requests:

 1. The client sends a ```GET``` request to ```/users/forgot-password``` with ```phoneNumber``` as a URL parameter. This
 will send a confirmation code to the phone number provided.
 2. Once the client has gotten their code, they ```POST``` to the same URL with ```phoneNumber```, ```verificationCode```,
 and ```newPassword``` as json parameters in the body. If the phone number and code are correct, the user's password is updated
 in the database and the user object is returned, which includes the API key needed for future API requests.

### GET Start verification of phone number [GET]
**Unit tested**

This is the first request to make in the forgotten password process. This accepts the user phone number and 
sends a verification code to the phone number provided.

+ Parameters
    + phoneNumber: 1234567890 (string, required) - Needed only for the GET request, send the phone number to get 
        SMS with verification code.

+ Response 200 (application/json)

        {
            "status": "ok",
            "message": "SMS has been sent to the number"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Bad request"
        }
        
+ Response 500 (application/json)

        {
            "status": "error",
            "message": "verification code not sent due to error"
        }

### POST Login and set new password [POST]
**Unit tested**

Once the client has received a verification code, they ```POST``` to this endpoint with their phone 
number, code, and new password. If the phone number and code are correct, the user's password is updated in
the database and the user object is returned, which includes the API key needed for future API requests.

+ Request (application/json)
    + Attributes
        + phoneNumber: 1234567890 (string, required) - The user's phone number
        + verificationCode: 12345 (string, required) - The verification code sent to the user's phone number (from the POST request)
        + newPassword: newPass123 (string, required) - The new password of the user
        
+ Response 200 (application/json)

        {
            "apiKey": "YOUR_API_KEY",
            "enabledLocation": true,
            "firstName": "test",
            "phoneNumber": "1234567890",
            "userId": "1111111111"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Password cannot be empty"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Phone number is empty"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Verification code is invalid"
        }

## Reset Password [/users/reset-password]

Resets a user's password. It is assumed that if you're calling this endpoint, you have the credentials of the user
and just want to change the password. If not, use ```/users/forgot-password```.

### PUT Reset account password [PUT]
**Unit tested**

To update a user's password, ```PUT``` to this endpoint with an API key, old password, and new password as json
parameters in the body. If the API key and old password are correct, a ```200 OK``` is returned with a status and a message.
Otherwise, a ```400 Bad Request``` is sent.

+ Request (application/json)
    + Attributes
        + apiKey: YOUR\_API\_KEY (string, required) - The user's API key.
        + newPassword: myNewPassword (string, required) - new password to replace existing password
        + oldPassword: abc123 (string, required) - If you don't have the old password, you will 
        have to click on forgot password before sending in a request without the old password.

+ Response 200 (application/json)
    
        {
            "status": "success",
            "message": "Successfully updated password"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Bad request - oldPassword is probably incorrect"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Invalid authentication credentials"
        }

## User Login with Phone & Password [/users/login]

This endpoint logs in a user.

### POST Login user with phone & password [POST]
**Unit tested**

This logs in a user. The client sends a ```phoneNumber``` and ```password``` as a parameter in 
the body, and if they are valid, a ```200 OK``` response is returned with the user object
in the body, which contains the ```apiKey``` necessary for future requests.

If the supplied phone number or password is invalid, a ```401 Unauthorized``` response code is
returned. An otherwise unsuccessful request will return a response code ```4xx``` or ```5xx``` 
will be returned with an error message.

+ Request (application/json)
    + Attributes
        + phoneNumber:  1234567890  (string, required) - The phone number of the user.
        + password:     abc123      (string, required) - The user's password.

+ Response 200 (application/json)

        {
            "apiKey": "YOUR_API_KEY",
            "enabledLocation": true,
            "firstName": "test",
            "phoneNumber": "1234567890",
            "userId": "1111111111"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Phone number cannot be empty"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Password cannot be empty"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Phone number/password pair is invalid"
        }

## User Login with API Key [/users/key-login]

This endpoint logs in a user given an ```apiKey```, i.e. returns a user object.

 * ```400 Bad Request``` is returned for a bad API key.
 * ```500 Internal Server Error``` is returned when something is broken in the backend.

### POST Login user with API key [POST]
**Unit tested**

+ Request (application/json)
    + Attributes
        + apiKey:  YOUR\_API\_KEY  (string, required) - The API key of the user.

+ Response 200 (application/json)

        {
            "apiKey": "YOUR_API_KEY",
            "enabledLocation": true,
            "firstName": "test",
            "phoneNumber": "1234567890",
            "userId": "1111111111"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "API key cannot be empty"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Unauthenticated"
        }

+ Response 500 (application/json)

        {
            "status": "error",
            "message": "Internal server error"
        }

# Group Discount Data Resources
A discount object has the following properties:

 * **discountId** (string): The unique ID of the discount. **Example:** *"d01"*
 * **storeId** (string): ID of the store the discount is associated with. **Example:** *"s01"*
 * **chainId** (string): ID of the chain the discount is associated with. **Example:** *"harristeeter"*
 * **productId** (string): ID of the product this is a discount of. **Example:** *"p01"*
 * **thumbnail** (string): URL of the thumbnail image for the product. **Example:** *"api.getfreshspired.com/static/images/products/p01.jpg"*
 * **displayName** (string): String display name. **Example:** *"Pork chops"*
 * **quantity**: How many there are of the discount. **Example:** *3*
 * **foodType** (string): the food category the discounted product is classified as. TODO: I'm working on the
 list of acceptable values for this parameter. **Example:** *"meat"*
 * **discountedPrice** (decimal): The discounted price. **Example:** *7.5*
 * **originalPrice** (decimal): The original price. **Example:** *15*
 * **priceUnit** (string): The unit of the price. This could be per pound, per package, per unit, etc. **Example:** *"lb"*
 * **expirationDate** (integer): Expiration date (format described below) of the discount. **Example:** *1457191800*
 * **posted** (string): Timestamp (format described below) when the discount was posted. **Example:** *1456932600*

All timestamps are in the [UNIX timestamp](https://en.wikipedia.org/wiki/Unix_time) format, which is defined as the number
of seconds elapsed since 00:00:00 January 1st 1970.

**Example:** March 2nd, 2016 at 10:30AM EST converts to a UNIX timestamp of ```1456932600```.

Here's a helpful [timestamp converter](http://www.epochconverter.com/).

## Discounts by latitude/longitude [/discounts/{latitude}/{longitude}{?apiKey,q,within,foodType,chain}]
This endpoint represents the discounts geographically close to the latitude and longitude specified. 
Use the query parameters to filter the results or search by product name.

Each discount object in the response includes a URL to its corresponding thumbnail image.

+ Parameters
    + latitude: 35.7915074 (required, float) - the latitude of the location you want nearby discounts for
    + longitude: ```-78.661654``` (required, float) - the longitude of the location you want nearby discounts for
    + apiKey: YOUR\_API\_KEY (string, required) - The user's API key.

### GET discounts [GET]
Get discounts geographically close to the latitude and longitude specified. Filter with parameters ```within```,
```foodType```, and ```chain```. Search by product name with the ```q``` parameter.

The discount objects in the response will include the URL for the thumbnail of the product.

+ Parameters
    + q:        bananas         (string, optional)  - Optional search query which will include only 
    discounts with the search query in the product name.
    + within:   10              (number, optional)  - Include results within this many miles. Default 10.
    + foodType: meat            (string, optional) - A food type to include. Default all. Declare this 
    parameter multiple times for multiple food types (i.e. ```&foodType=meat&foodType=dairy```
    for both meat and dairy)
    + chain:    c01             (string, optional)   - ID of a grocery store chain to include. Default all. 
    Declare this parameter multiple times for multiple chains (i.e. ```&chain=c01&chain=c02``` to 
    include discounts from chains with IDs ```c01``` and ```c01```)

+ Response 200 (application/json)

        {
            "count": 6,
            "discounts:": [
                {
                    "discountId": "d01",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p01",
                    "thumbnail": "/static/images/products/p01.jpg",
                    "displayName": "Pork tenderloin",
                    "quantity": 3,
                    "foodType": "meat",
                    "discountedPrice": 7.5,
                    "originalPrice": 15,
                    "priceUnit": "lb",
                    "expirationDate": 1457191800,
                    "posted": 1456932600
                },
                {
                    "discountId": "d02",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p02",
                    "thumbnail": "/static/images/products/p02.jpg",
                    "displayName": "Lemons",
                    "quantity": 11,
                    "foodType": "produce",
                    "discountedPrice": 0.6,
                    "originalPrice": 1,
                    "priceUnit": "lb",
                    "expirationDate": 1457191800,
                    "posted": 1456954200
                },
                {
                    "discountId": "d03",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p03",
                    "thumbnail": "/static/images/products/p03.jpg",
                    "displayName": "1doz Chocolate Doughnuts",
                    "quantity": 4,
                    "foodType": "bakery",
                    "discountedPrice": 3.5,
                    "originalPrice": 5.5,
                    "priceUnit": "box",
                    "expirationDate": 1457352000,
                    "posted": 1457028000
                },
                {
                    "discountId": "d04",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p04",
                    "thumbnail": "/static/images/products/p04.jpg",
                    "displayName": "Organic pork sausages",
                    "quantity": 1,
                    "foodType": "meat",
                    "discountedPrice": 3.14,
                    "originalPrice": 5.25,
                    "priceUnit": "package",
                    "expirationDate": 1457193600,
                    "posted": 1457100000
                },
                {
                    "discountId": "d05",
                    "storeId": "s02",
                    "chainId": "harristeeter",
                    "productId": "p05",
                    "thumbnail": "/static/images/products/p05.jpg",
                    "displayName": "Dinner rolls (6ct)",
                    "quantity": 5,
                    "foodType": "bakery",
                    "discountedPrice": 4,
                    "originalPrice": 6,
                    "priceUnit": "bag",
                    "expirationDate": 1457355600,
                    "posted": 1457193600
                },
                {
                    "discountId": "d06",
                    "storeId": "s02",
                    "chainId": "harristeeter",
                    "productId": "p06",
                    "thumbnail": "/static/images/products/p06.jpg",
                    "displayName": "Grape tomatoes (8ox box)",
                    "quantity": 8,
                    "foodType": "produce",
                    "discountedPrice": 1.9,
                    "originalPrice": 3,
                    "priceUnit": "8oz box",
                    "expirationDate": 1457528400,
                    "posted": 1457355600
                }
            ]
        }

+ Response 400

        {
            "status": "error",
            "message": "Bad request - are the parameters formatted correctly?"
        }

+ Response 401

        {
            "status": "error",
            "message": "Unauthenticated"
        }

## Discounts by street address/zip code [/discounts/place/{zipCode}{?apiKey,q,within,foodType,chain}]

+ Parameters
    + zipCode: 27607 (required, string) - The zip code of the location to search from
    + apiKey: YOUR\_API\_KEY (string, required) - The user's API key.

### GET Discounts [GET]
Get discounts geographically close to the zip code specified. Filter with parameters ```within```,
```foodType```, and ```chain```. Search by product name with the ```q``` parameter.

The discount objects in the response will include the URL for the thumbnail of the product.

+ Parameters
    + q:        bananas         (string, optional) - Optional search query which will include only 
    discounts with the search query in the product name.
    + within:   10              (number, optional) - Include results within this many miles. Default 10.
    + foodType: meat            (optional, string) - A food type to include. Default all. Declare this 
    parameter multiple times for multiple food types (i.e. *```&foodType=meat&foodType=dairy```*
    for both meat and dairy)
    + chain:    c01             (array, optional)   - ID of a grocery store chain to include. Default all. 
    Declare this parameter multiple times for multiple chains (i.e. *```&chain=c01&chain=c02```* to 
    include discounts from chains with IDs *```c01```* and *```c01```*)

+ Response 200 (application/json)

        {
            "count": 6,
            "discounts:": [
                {
                    "discountId": "d01",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p01",
                    "thumbnail": "/static/images/products/p01.jpg",
                    "displayName": "Pork tenderloin",
                    "quantity": 3,
                    "foodType": "meat",
                    "discountedPrice": 7.5,
                    "originalPrice": 15,
                    "priceUnit": "lb",
                    "expirationDate": 1457191800,
                    "posted": 1456932600
                },
                {
                    "discountId": "d02",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p02",
                    "thumbnail": "/static/images/products/p02.jpg",
                    "displayName": "Lemons",
                    "quantity": 11,
                    "foodType": "produce",
                    "discountedPrice": 0.6,
                    "originalPrice": 1,
                    "priceUnit": "lb",
                    "expirationDate": 1457191800,
                    "posted": 1456954200
                },
                {
                    "discountId": "d03",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p03",
                    "thumbnail": "/static/images/products/p03.jpg",
                    "displayName": "1doz Chocolate Doughnuts",
                    "quantity": 4,
                    "foodType": "bakery",
                    "discountedPrice": 3.5,
                    "originalPrice": 5.5,
                    "priceUnit": "box",
                    "expirationDate": 1457352000,
                    "posted": 1457028000
                },
                {
                    "discountId": "d04",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p04",
                    "thumbnail": "/static/images/products/p04.jpg",
                    "displayName": "Organic pork sausages",
                    "quantity": 1,
                    "foodType": "meat",
                    "discountedPrice": 3.14,
                    "originalPrice": 5.25,
                    "priceUnit": "package",
                    "expirationDate": 1457193600,
                    "posted": 1457100000
                },
                {
                    "discountId": "d05",
                    "storeId": "s02",
                    "chainId": "harristeeter",
                    "productId": "p05",
                    "thumbnail": "/static/images/products/p05.jpg",
                    "displayName": "Dinner rolls (6ct)",
                    "quantity": 5,
                    "foodType": "bakery",
                    "discountedPrice": 4,
                    "originalPrice": 6,
                    "priceUnit": "bag",
                    "expirationDate": 1457355600,
                    "posted": 1457193600
                },
                {
                    "discountId": "d06",
                    "storeId": "s02",
                    "chainId": "harristeeter",
                    "productId": "p06",
                    "thumbnail": "/static/images/products/p06.jpg",
                    "displayName": "Grape tomatoes (8ox box)",
                    "quantity": 8,
                    "foodType": "produce",
                    "discountedPrice": 1.9,
                    "originalPrice": 3,
                    "priceUnit": "8oz box",
                    "expirationDate": 1457528400,
                    "posted": 1457355600
                }
            ]
        }

+ Response 400

        {
            "status": "error",
            "message": "Bad request - are the parameters formatted correctly?"
        }

+ Response 401

        {
            "status": "error",
            "message": "Unauthenticated"
        }

+ Response 404

        {
            "status": "error",
            "message": "Zip code not found or incorrectly formatted."
        }

# Group Store Data Resources
Use this endpoint to get and post discounts from/to a specific store, and get store information.

A store object contains the following properties:

 * **storeId** (string): The unique ID of the store. **Example:** *"s01"*
 * **name** (string): The display name of the store. **Example:** *"Cameron Village Harris Teeter"*
 * **address** (string): The street address of the store. **Example:** *"500 Oberlin Road, Raleigh, NC 27605"*
 * **lat** (decimal): Latitude of the store. **Example:** *35.7889461*
 * **long** (decimal): Longitude of the store. **Example:** *-78.6623816*
 * **discounts** (integer): Number of discounts in the store. **Example:** *24*

## All Stores [/stores{?apiKey}]

+ Parameters
    + apiKey: YOUR\_API\_KEY (string, required) - The user's API key.

### GET List of stores with Freshspire discounts [GET]
Returns an array of store objects. Response contains ```count``` (number of stores returned) 
and ```stores``` (array of store objects).

TODO filter and search functionality?

+ Response 200 (application/json)

        {
            "count": 5,
            "stores": [
                {
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "name": "Cameron Village Harris Teeter",
                    "address": "500 Oberlin Road, Raleigh, NC 27605",
                    "lat": 35.7889461,
                    "long": -78.6623816,
                    "discounts": 24
                },
                {
                    "storeId": "s02",
                    "chainId": "harristeeter",
                    "name": "Hillsborough Street Harris Teeter",
                    "address": "5563 Western Blvd, Raleigh, NC 27606",
                    "lat": 35.784214,
                    "long": -78.7206401
                    "discounts": 29
                },
                {
                    "storeId": "s03",
                    "chainId": "patelbros",
                    "name": "Cary Patel Brothers",
                    "address": "802 E Chatham St, Cary, NC 27511",
                    "lat": 35.7909357,
                    "long": -78.7633049
                    "discounts": 6
                },
                {
                    "storeId": "s04",
                    "chainId": "wholefoods",
                    "name": "Raleigh Whole Foods",
                    "address": "3540 Wade Avenue, Raleigh, NC 27607",
                    "lat": 35.8024691,
                    "long": -78.6864058
                    "discounts": 18
                },
                {
                    "storeId": "s05",
                    "chainId": "foodlion",
                    "name": "Raleigh Boulevard Food Lion",
                    "address": "1100 Raleigh Blvd #122, Raleigh, NC 27610",
                    "lat": 35.7943748,
                    "long": -78.6114524
                    "discounts": 34
                }
            ]
        }

+ Response 400

        {
            "status": "error",
            "message": "Bad request - are the parameters formatted correctly?"
        }

+ Response 401

        {
            "status": "error",
            "message": "Unauthenticated"
        }

## Specific Store [/stores/{storeId}{?apiKey}]

### GET Specific store information [GET]
Returns the store object associated with given store ID.

+ Parameters
    + storeId: s01 (string, required)           - The unique ID of the store
    + apiKey: YOUR\_API\_KEY (string, required) - The user's API key.

+ Response 200 (application/json)

        {
            "storeId": "s01",
            "name": "Cameron Village Harris Teeter",
            "address": "500 Oberlin Road, Raleigh, NC 27605",
            "lat": 35.7889461,
            "long": -78.6623816,
            "discounts": 24
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Bad request - are the parameters formatted correctly?"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Unauthenticated"
        }

+ Response 404 (application/json)

        {
            "status": "error",
            "message": "Store with ID <storeId> not found"
        }

## Discounts in Specific Store [/stores/{storeId}/discounts{?apiKey,q,foodType}]
Contains all discount objects posted in the store with ID ```storeId```. Get and search for discounts, filter the
results, post new discounts to, and delete existing discounts from the store.

+ Parameters
    + apiKey:   YOUR\_API\_KEY  (string, required) - The user's API key.

### GET Discounts at a specific store [GET]
Returns array of discount objects. Filter the results with ```foodType``` and search by product name with query 
parameter ```q```.

+ Parameters
    + q:        Pork            (string, optional) - Optional search query which will include only 
        discounts with the search query in the product name.
    + foodType: meat            (optional, string) - A food type to include. Default all. Declare this 
        parameter multiple times for multiple food types (i.e. ```&foodType=meat&foodType=dairy``` 
        for both meat and dairy)

+ Response 200 (application/json)

        {
            "count": 2,
            "discounts": [
                {
                    "discountId": "d01",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p01",
                    "thumbnail": "/static/images/products/p01.jpg",
                    "displayName": "Pork tenderloin",
                    "quantity": 3,
                    "foodType": "meat",
                    "discountedPrice": 7.5,
                    "originalPrice": 15,
                    "priceUnit": "lb",
                    "currency": "$",
                    "expirationDate": 1457191800,
                    "posted": 1456932600
                },
                {
                    "discountId": "d02",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p02",
                    "thumbnail": "/static/images/products/p02.jpg",
                    "displayName": "Lemons",
                    "quantity": 11,
                    "foodType": "produce",
                    "discountedPrice": 0.6,
                    "originalPrice": 1,
                    "priceUnit": "lb",
                    "currency": "$",
                    "expirationDate": 1457191800,
                    "posted": 1456954200
                },
                {
                    "discountId": "d03",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p03",
                    "thumbnail": "/static/images/products/p03.jpg",
                    "displayName": "1doz Chocolate Doughnuts",
                    "quantity": 4,
                    "foodType": "bakery",
                    "discountedPrice": 3.5,
                    "originalPrice": 5.5,
                    "priceUnit": "box",
                    "currency": "$",
                    "expirationDate": 1457352000,
                    "posted": 1457028000
                },
                {
                    "discountId": "d04",
                    "storeId": "s01",
                    "chainId": "harristeeter",
                    "productId": "p04",
                    "thumbnail": "/static/images/products/p04.jpg",
                    "displayName": "Organic pork sausages",
                    "quantity": 1,
                    "foodType": "meat",
                    "discountedPrice": 3.14,
                    "originalPrice": 5.25,
                    "priceUnit": "package",
                    "currency": "$",
                    "expirationDate": 1457193600,
                    "posted": 1457100000
                }
            ]
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Bad request - are the parameters formatted correctly?"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Unauthenticated"
        }

+ Response 404 (application/json)

        {
            "status": "error",
            "message": "Store with ID <storeId> not found"
        }

### POST Add single discount to specific store [POST]
Posts a new discount to the store with ID ```storeId```. ```productId```, ```originalPrice```,
```discountedPrice```, and ```expirationDate``` must be included as JSON parameters in the request body.

+ Request
    + Attributes
        + productId:        p01         (string, required) - The ID of the product being discounted.
        + originalPrice:    15          (number, required) - The original price of the product
        + discountedPrice:  7.5         (number, required) - The discounted price of the product
        + expirationDate:   1457191800  (number, required) - The UNIX timestamp of the expiration date.

+ Response 200 (application/json)

        {
            "status": "ok",
            "message": "New discount posted"
        }

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Bad request - are the parameters formatted correctly?"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Unauthenticated"
        }

+ Response 404 (application/json)

        {
            "status": "error",
            "message": "Store with ID <storeId> not found"
        }

# Group Static Content

## Discount thumbnails [/static/images/products/{productId}{?apiKey}]

+ Parameters
    + productId:    p01             (string, required) - The unique ID of the product
    + apiKey:       YOUR\_API\_KEY  (string, required) - The user's API key.

### GET Thumbnail for a particular product [GET]
Returns the JPG thumbnail for a particular product, specified in the URL by the product ID.

+ Response 200 (image/jpg)

+ Response 400 (application/json)

        {
            "status": "error",
            "message": "Bad request - are the parameters formatted correctly?"
        }

+ Response 401 (application/json)

        {
            "status": "error",
            "message": "Unauthenticated"
        }

+ Response 404 (application/json)

        {
            "status": "error",
            "message": "Product with ID <productId> not found"
        }

# Group System Status

## System status [/status]

### Is System Up [GET]
This returns a ```status: ok``` only if all status endpoints under ```/status``` are ```ok```.

Returns ```200 OK```  with status ```ok``` if system is running properly.

Returns ```503 Service Unavailable``` with status ```unavailable``` if it's *not* running properly.

+ Response 200 (application/json)

        {
            "status": "ok"
        }

+ Response 503 (application/json)

        {
            "status": "unavailable"
        }

## Database status [/status/database]

### Is Database Up [GET]
Returns ```200 OK```  with status ```ok``` if database is running properly.

Returns ```503 Service Unavailable``` with status ```unavailable``` if it's *not* running properly.

+ Response 200 (application/json)

        {
            "status": "ok"
        }

+ Response 503 (application/json)

        {
            "status": "unavailable"
        }







