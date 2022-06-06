# supermarket-users
Users microservice for supermarket

### Purpose
Basically the porpuse is to manage market users, it can be cashier or admin.
Here lies the whole application's entry point, because to access any route of the others microservice, you have to be autheticated by the /login route.

### What is the difference between a cashier and an admin?
The difference is that admins have no limitations through the system usage, but cashiers do.
Cashiers cannot register or update products or update inventory either.

### How to get token for login
If its isn't your first time, you can make a ```post``` request to /login, passing name and password in the request body.
By doing this you'll reveive a token that you can put in the Authorization request header with the Bearer type.

If it is your first time using the application, you will need ask an admin to create a acount for you. By standard, the system already have
an admin user created, but only specific persons have access to it.

### Data structure
When you see User as return param of the route, notice that we are talking about this structure:

##### USER
```
{
  name: string;
  email: string;
  password: string;
  accessLevel: ENUM(CASHIER, ADMIN);
}
```

### Fnally, what are the microservices routes?
The microservice routes are the following:

##### LOGIN
```
method: POST
url: /login
body: {
  "name": "your_username",
  "password": "your_password"
}
returns: access_token
```


##### CREATE
###### *NOTE*: If you set a real email to your user, and you are an admin, you will receive an email message when some product in the inventory is with low stock.
```
method: POST
url: /users/create
body: {
  "name": "your_username",
  "email": "your@email.com",
  "password": "your_password.123",
  "accessLevel": "ADMIN or CASHIER"
}
returns: User
```

##### UPDATE
```
method: PATCH
url: /users/update/{id}
body: {
  "name": "new_name",
  "email": "new@email.com",
  "accessLevel": "CASHIER"
}
returns: User
```

##### LIST ALL
```
method: GET
url: /users/listAll
returns: User[]
```

##### FIND BY ID
```
method: GET
url: /users/{id}
returns: User
```
