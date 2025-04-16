# Homework 5 - REST API
**Author: M.Y. Yang**

## Question 1
> Practice HTTP methods in Postman.
> * 5 GETs with different response types
> * 5 POSTs with json request body
> * 3 PUTs with json request body
> * 2 DELETEs
> * Produce some error responses (e.g., `401`, `404`, `500`)

### GET

<details>
<summary>JSON Response</summary>

**URL:** https://jsonplaceholder.typicode.com/posts/1

**Response Status:** `200 OK`

**Response Body:**
```json
{
    "userId": 1,
    "id": 1,
    "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
    "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
}
```
</details>



<details>
<summary>XML Response</summary>

**URL:** https://httpbin.org/xml

**Response Status:** `200 OK`

**Response Body:**
```xml
<?xml version='1.0' encoding='us-ascii'?>
<!--  A SAMPLE set of slides  -->
<slideshow 
    title="Sample Slide Show"
    date="Date of publication"
    author="Yours Truly"
    >
    <!-- TITLE SLIDE -->
    <slide type="all">
        <title>Wake up to WonderWidgets!</title>
    </slide>
    <!-- OVERVIEW -->
    <slide type="all">
        <title>Overview</title>
        <item>Why 
            <em>WonderWidgets</em> are great
        </item>
        <item/>
        <item>Who 
            <em>buys</em> WonderWidgets
        </item>
    </slide>
</slideshow>
```
</details>



<details>
<summary>TXT Response</summary>

**URL:** https://httpbin.org/robots.txt

**Response Status:** `200 OK`

**Response Body:**
```
User-agent: *
Disallow: /deny
```
</details>



<details>
<summary> HTML Response</summary>

**URL:** https://httpbin.org/html

**Response Status:** `200 OK`

**Response Body:**
```html
<!DOCTYPE html>
<html>

<head>
</head>

<body>
    <h1>Herman Melville - Moby-Dick</h1>

    <div>
        <p>
            Availing himself of the mild, summer-cool weather that now reigned in these latitudes, and in preparation
            for the peculiarly active pursuits shortly to be anticipated, Perth, the begrimed, blistered old blacksmith,
            had not removed his portable forge to the hold again, after concluding his contributory work for Ahab's leg,
            but still retained it on deck, fast lashed to ringbolts by the foremast; being now almost incessantly
            invoked by the headsmen, and harpooneers, and bowsmen to do some little job for them; altering, or
            repairing, or new shaping their various weapons and boat furniture. Often he would be surrounded by an eager
            circle, all waiting to be served; holding boat-spades, pike-heads, harpoons, and lances, and jealously
            watching his every sooty movement, as he toiled. Nevertheless, this old man's was a patient hammer wielded
            by a patient arm. No murmur, no impatience, no petulance did come from him. Silent, slow, and solemn; bowing
            over still further his chronically broken back, he toiled away, as if toil were life itself, and the heavy
            beating of his hammer the heavy beating of his heart. And so it was.—Most miserable! A peculiar walk in this
            old man, a certain slight but painful appearing yawing in his gait, had at an early period of the voyage
            excited the curiosity of the mariners. And to the importunity of their persisted questionings he had finally
            given in; and so it came to pass that every one now knew the shameful story of his wretched fate. Belated,
            and not innocently, one bitter winter's midnight, on the road running between two country towns, the
            blacksmith half-stupidly felt the deadly numbness stealing over him, and sought refuge in a leaning,
            dilapidated barn. The issue was, the loss of the extremities of both feet. Out of this revelation, part by
            part, at last came out the four acts of the gladness, and the one long, and as yet uncatastrophied fifth act
            of the grief of his life's drama. He was an old man, who, at the age of nearly sixty, had postponedly
            encountered that thing in sorrow's technicals called ruin. He had been an artisan of famed excellence, and
            with plenty to do; owned a house and garden; embraced a youthful, daughter-like, loving wife, and three
            blithe, ruddy children; every Sunday went to a cheerful-looking church, planted in a grove. But one night,
            under cover of darkness, and further concealed in a most cunning disguisement, a desperate burglar slid into
            his happy home, and robbed them all of everything. And darker yet to tell, the blacksmith himself did
            ignorantly conduct this burglar into his family's heart. It was the Bottle Conjuror! Upon the opening of
            that fatal cork, forth flew the fiend, and shrivelled up his home. Now, for prudent, most wise, and economic
            reasons, the blacksmith's shop was in the basement of his dwelling, but with a separate entrance to it; so
            that always had the young and loving healthy wife listened with no unhappy nervousness, but with vigorous
            pleasure, to the stout ringing of her young-armed old husband's hammer; whose reverberations, muffled by
            passing through the floors and walls, came up to her, not unsweetly, in her nursery; and so, to stout
            Labor's iron lullaby, the blacksmith's infants were rocked to slumber. Oh, woe on woe! Oh, Death, why canst
            thou not sometimes be timely? Hadst thou taken this old blacksmith to thyself ere his full ruin came upon
            him, then had the young widow had a delicious grief, and her orphans a truly venerable, legendary sire to
            dream of in their after years; and all of them a care-killing competency.
        </p>
    </div>
</body>

</html>
```
</details>



<details>
<summary>JPEG Response</summary>

**URL:** https://httpbin.org/image/jpeg

**Response Status:** `200 OK`

**Response Body:**

![](https://httpbin.org/image/jpeg)
</details>



### POST

<details>
<summary>Create a Post</summary>

**URL:** https://jsonplaceholder.typicode.com/posts

**Request Body:**
```json
{
  "title": "foo",
  "body": "bar",
  "userId": 1
}
```

**Response Status:** `201 Created`

**Response Body:**
```json
{
  "title": "foo",
  "body": "bar",
  "userId": 1,
  "id": 101
}
```
</details>



<details>
<summary>Authentication</summary>

**URL:** https://reqres.in/api/login

**Request Body:**
```json
{
  "email": "eve.holt@reqres.in",
  "password": "cityslicka"
}
```

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "token": "QpwL5tke4Pnpja7X4"
}
```
</details>



<details>
<summary>Echo Request</summary>

**URL:** https://httpbin.org/post

**Request Body:**
```json
{
  "name": "John",
  "age": 30,
  "city": "New York"
}
```

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "args": {},
  "data": "{\r\n  \"name\": \"John\",\r\n  \"age\": 30,\r\n  \"city\": \"New York\"\r\n}",
  "files": {},
  "form": {},
  "headers": {
    "Accept": "*/*",
    "Accept-Encoding": "gzip, deflate, br",
    "Cache-Control": "no-cache",
    "Content-Length": "59",
    "Content-Type": "application/json",
    "Host": "httpbin.org",
    "Postman-Token": "fc083a71-98c6-451f-8344-59cf09c75490",
    "User-Agent": "PostmanRuntime/7.43.3",
    "X-Amzn-Trace-Id": "Root=1-67ff4723-73f3e420276edab66a503e29"
  },
  "json": {
    "age": 30,
    "city": "New York",
    "name": "John"
  },
  "origin": "70.234.254.128",
  "url": "https://httpbin.org/post"
}
```
</details>



<details>
<summary>Register User</summary>

**URL:** https://reqres.in/api/register

**Request Body:**
```json
{
  "email": "eve.holt@reqres.in",
  "password": "pistol"
}
```

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "id": 4,
  "token": "QpwL5tke4Pnpja7X4"
}
```
</details>



<details>
<summary>Validate Data</summary>

**URL:** https://httpbin.org/anything

**Request Body:**
```json
{
  "product": "laptop",
  "price": 999.99,
  "inStock": true
}
```

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "args": {},
  "data": "{\r\n  \"product\": \"laptop\",\r\n  \"price\": 999.99,\r\n  \"inStock\": true\r\n}",
  "files": {},
  "form": {},
  "headers": {
    "Accept": "*/*",
    "Accept-Encoding": "gzip, deflate, br",
    "Cache-Control": "no-cache",
    "Content-Length": "67",
    "Content-Type": "application/json",
    "Host": "httpbin.org",
    "Postman-Token": "790c6e8d-380e-4ab0-8cc0-d7d90ffb627b",
    "User-Agent": "PostmanRuntime/7.43.3",
    "X-Amzn-Trace-Id": "Root=1-67ff478a-3f6f871963b339136c5f5b51"
  },
  "json": {
    "inStock": true,
    "price": 999.99,
    "product": "laptop"
  },
  "method": "POST",
  "origin": "70.234.254.128",
  "url": "https://httpbin.org/anything"
}
```
</details>



### PUT

<details>
<summary>Update a Post</summary>

**URL:** https://jsonplaceholder.typicode.com/posts/1

**Request Body:**
```json
{
  "id": 1,
  "title": "updated title",
  "body": "updated body",
  "userId": 1
}
```

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "id": 1,
  "title": "updated title",
  "body": "updated body",
  "userId": 1
}
```
</details>



<details>
<summary>Update a User</summary>

**URL:** https://reqres.in/api/users/2

**Request Body:**
```json
{
  "name": "morpheus",
  "job": "zion resident"
}
```

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "name": "morpheus",
  "job": "zion resident",
  "updatedAt": "2025-04-16T05:50:44.935Z"
}
```
</details>



<details>
<summary>Replace Data</summary>

**URL:** https://httpbin.org/put

**Request Body:**
```json
{
  "item": "book",
  "quantity": 5,
  "price": 12.99
}
```

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "args": {},
  "data": "{\r\n  \"item\": \"book\",\r\n  \"quantity\": 5,\r\n  \"price\": 12.99\r\n}",
  "files": {},
  "form": {},
  "headers": {
    "Accept": "*/*",
    "Accept-Encoding": "gzip, deflate, br",
    "Cache-Control": "no-cache",
    "Content-Length": "59",
    "Content-Type": "application/json",
    "Host": "httpbin.org",
    "Postman-Token": "4bbec72b-e60f-4d60-aed0-35f38a064c85",
    "User-Agent": "PostmanRuntime/7.43.3",
    "X-Amzn-Trace-Id": "Root=1-67ff45a1-27993a727514cb426ed481a6"
  },
  "json": {
    "item": "book",
    "price": 12.99,
    "quantity": 5
  },
  "origin": "70.234.254.128",
  "url": "https://httpbin.org/put"
}
```
</details>



### DELETE

<details>
<summary>200 Response</summary>

**URL:** https://jsonplaceholder.typicode.com/posts/1

**Response Status:** `200 OK`

**Response Body:**
```json
{}
```
</details>



<details>
<summary>204 Response</summary>

**URL:** https://reqres.in/api/users/2

**Response Status:** `204 No Content`

**Response Body:**
```
```
</details>



### Error Responses

<details>
<summary>401 Response</summary>

**URL:** https://httpbin.org/status/401

**Response Status:** `401 Unauthorized`

**Response Body:**
```
```
</details>



<details>
<summary>404 Response</summary>

**URL:** https://httpbin.org/status/404

**Response Status:** `404 Not Found`

**Response Body:**
```
```
</details>



<details>
<summary>500 Response</summary>

**URL:** https://httpbin.org/status/500

**Response Status:** `500 Internal Server Error`

**Response Body:**
```
```
</details>


## Question 2
> API Design
>
> **Part 1**
> 
> Suppose an online shopping website has an endpoint `/customers`, write queries to get:
> 1. a customer's payment methods, e.g., credit card 1, credit card 2, PayPal, Apple Pay
> 2. a customer's historical orders from 10/10/2022 to 10/24/2022
> 3. a customer's delivery addresses
> 4. a customer's default payment method and default delivery address
>
> **Part 2**
> 
> 5. Example REST URL paths with collection (e.g., Twitter's, PayPal's, YouTube's, etc.)
>
> **Part 3**
> 
> 6. Design an API endpoint for a website's blog posts. Include queries with HTTP methods.

### Part 1

1. To get a customer's payment methods

   `GET /customers/{customerId}/payment-methods`

2. To get a customer's historical orders from 10/10/2022 to 10/24/2022

   `GET /customers/{customerId}/orders?startDate=2022-10-10&endDate=2022-10-24`

3. To get a customer's delivery addresses

   `GET /customers/{customerId}/addresses`

4. To get a customer's default payment method and default delivery address

    ```
    GET /customers/{customerId}/payment-methods/default
    GET /customers/{customerId}/addresses/default
    ```

### Part 2

#### Example YouTube API URL Paths

| **Action**                   | **HTTP Method** | **Endpoint**                     | **Query Params / Body**                                     |
|------------------------------|-----------------|----------------------------------|-------------------------------------------------------------|
| Get all videos               | `GET`           | `/videos`                        | `page`, `limit`, `search`, `sortBy`, `order`                |
| Get a single video by ID     | `GET`           | `/videos/{videoId}`              | —                                                           |
| Create a new video           | `POST`          | `/videos`                        | JSON body: `{ "title", "description", "tags", "category" }` |
| Update video details         | `PUT`           | `/videos/{videoId}`              | JSON body: `{ "title", "description", "tags" }`             |
| Delete a video               | `DELETE`        | `/videos/{videoId}`              | —                                                           |
| Get comments for a video     | `GET`           | `/videos/{videoId}/comments`     | —                                                           |
| Add a comment to a video     | `POST`          | `/videos/{videoId}/comments`     | JSON body: `{ "author", "content" }`                        |
| Get all playlists for a user | `GET`           | `/users/{userId}/playlists`      | —                                                           |
| Get a single playlist by ID  | `GET`           | `/playlists/{playlistId}`        | —                                                           |
| Create a new playlist        | `POST`          | `/playlists`                     | JSON body: `{ "name", "description", "videos" }`            |
| Add video to a playlist      | `POST`          | `/playlists/{playlistId}/videos` | JSON body: `{ "videoId" }`                                  |
| Delete a playlist            | `DELETE`        | `/playlists/{playlistId}`        | —                                                           |

#### Example PayPal API URL Paths

| **Action**                  | **HTTP Method** | **Endpoint**               | **Query Params / Body**                                    |
|-----------------------------|-----------------|----------------------------|------------------------------------------------------------|
| Get all payments            | `GET`           | `/payments`                | `status`, `startDate`, `endDate`, `limit`, `page`          |
| Get a single payment by ID  | `GET`           | `/payments/{paymentId}`    | —                                                          |
| Create a new payment        | `POST`          | `/payments`                | JSON body: `{ "amount", "currency", "payerId", "method" }` |
| Update payment status       | `PUT`           | `/payments/{paymentId}`    | JSON body: `{ "status" }`                                  |
| Delete a payment            | `DELETE`        | `/payments/{paymentId}`    | —                                                          |
| Get all accounts for a user | `GET`           | `/users/{userId}/accounts` | —                                                          |
| Get a single account by ID  | `GET`           | `/accounts/{accountId}`    | —                                                          |
| Create a new account        | `POST`          | `/accounts`                | JSON body: `{ "type", "currency", "email" }`               |
| Update account details      | `PUT`           | `/accounts/{accountId}`    | JSON body: `{ "email" }`                                   |
| Delete an account           | `DELETE`        | `/accounts/{accountId}`    | —                                                          |


### Part 3

| **Action**                    | **HTTP Method** | **Endpoint**              | **Query Params / Body**                                                      |
|-------------------------------|-----------------|---------------------------|------------------------------------------------------------------------------|
| Get all blog posts            | `GET`           | `/posts`                  | `authorId`, `tag`, `published`, `page`, `limit`, `search`, `sortBy`, `order` |
| Get single blog post          | `GET`           | `/posts/{postId}`         | —                                                                            |
| Create new blog post          | `POST`          | `/posts`                  | JSON body: `title`, `content`, `authorId`, `tags`, `published`               |
| Update a blog post            | `PUT`           | `/posts/{postId}`         | JSON body: `title`, `content`, `tags`, `published`                           |
| Delete a blog post            | `DELETE`        | `/posts/{postId}`         | —                                                                            |
| Publish/unpublish a blog post | `PATCH`         | `/posts/{postId}/publish` | JSON body: `published` (true/false)                                          |
