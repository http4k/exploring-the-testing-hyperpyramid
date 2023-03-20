#ClusterTest - can load stock list and order item
```mermaid
sequenceDiagram
    title ClusterTest - can load stock list and order item - Sequence
    participant Shop User
	participant api-gateway
	participant shop
	participant warehouse
	participant cognito
	participant db
	participant dept-store
	participant email.eu-west-1.amazonaws.com
	participant event-stream

    Shop User->>api-gateway: GET 
    activate api-gateway
    
    api-gateway->>Shop User: 307 Temporary Redirect
    deactivate api-gateway
    
	
    Shop User->>cognito: GET oauth2/authorize
    activate cognito
    
    cognito->>Shop User: 302 Found
    deactivate cognito
    
	
    Shop User->>cognito: GET oauth2/login
    activate cognito
    
    cognito->>Shop User: 200 OK
    deactivate cognito
    
	
    Shop User->>cognito: POST oauth2/login
    activate cognito
    
    cognito->>Shop User: 303 See Other
    deactivate cognito
    
	
    Shop User->>api-gateway: GET oauth/callback
    activate api-gateway
    
    api-gateway->>cognito: POST oauth2/token
    activate cognito
    
    cognito->>api-gateway: 200 OK
    deactivate cognito
    
    api-gateway->>Shop User: 307 Temporary Redirect
    deactivate api-gateway
    
	
    Shop User->>api-gateway: GET 
    activate api-gateway
    
    api-gateway->>shop: GET 
    activate shop
    
    shop->>warehouse: GET v1/items
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
    warehouse->>shop: 200 OK
    deactivate warehouse
    
    shop->>api-gateway: 200 OK
    deactivate shop
    
    api-gateway->>Shop User: 200 OK
    deactivate api-gateway
    
	
    Shop User->>api-gateway: GET 
    activate api-gateway
    
    api-gateway->>shop: GET 
    activate shop
    
    shop->>warehouse: GET v1/items
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
    warehouse->>shop: 200 OK
    deactivate warehouse
    
    shop->>api-gateway: 200 OK
    deactivate shop
    
    api-gateway->>Shop User: 200 OK
    deactivate api-gateway
    
	
    Shop User->>api-gateway: POST order/{id}
    activate api-gateway
    
    api-gateway->>shop: POST order/{id}
    activate shop
    
    shop->>warehouse: POST v1/dispatch
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
	
    warehouse->>db: adjust
    
    db->>warehouse: 
    
	
    warehouse->>dept-store: POST v1/order
    activate dept-store
    
    dept-store->>warehouse: 200 OK
    deactivate dept-store
    
    warehouse->>shop: 200 OK
    deactivate warehouse
    
	
    shop->>email.eu-west-1.amazonaws.com: POST 
    activate email.eu-west-1.amazonaws.com
    
    email.eu-west-1.amazonaws.com->>shop: 200 OK
    deactivate email.eu-west-1.amazonaws.com
    
	
    shop-)event-stream: CustomerOrder(item=1)
    
    
    shop->>api-gateway: 200 OK
    deactivate shop
    
    api-gateway->>Shop User: 200 OK
    deactivate api-gateway
    
```

```mermaid
C4Context
title ClusterTest - can load stock list and order item

System(customer, "Customer")
System(apigateway, "api-gateway")
System(shop, "shop")
System(warehouse, "warehouse")
System(cognito, "cognito")
ContainerDb(db, "db")
System(deptstore, "dept-store")
System(emaileuwest1amazonawscom, "email.eu-west-1.amazonaws.com")
System(eventstream, "event-stream")    
Rel_D(customer, apigateway, " ") 
Rel_D(customer, cognito, " ") 
Rel_D(apigateway, cognito, " ") 
Rel_D(apigateway, shop, " ") 
Rel_D(shop, warehouse, " ") 
Rel_D(warehouse, db, " ") 
Rel_D(warehouse, deptstore, " ") 
Rel_D(shop, emaileuwest1amazonawscom, " ") 
Rel_D(shop, eventstream, " ")     
```


## ClusterTest - can load stock list and order item - Maximum Trace Depth

| Origin | Target | Request |  Max Depth  |
|:------:|:------:|:-------:|:-----------:|
|	Shop User	|	api-gateway	|	GET 	|	4	|
|	Shop User	|	api-gateway	|	GET 	|	4	|
|	Shop User	|	api-gateway	|	POST order/{id}	|	4	|
|	Shop User	|	api-gateway	|	GET oauth/callback	|	2	|
|	Shop User	|	api-gateway	|	GET 	|	1	|
|	Shop User	|	cognito	|	GET oauth2/authorize	|	1	|
|	Shop User	|	cognito	|	GET oauth2/login	|	1	|
|	Shop User	|	cognito	|	POST oauth2/login	|	1	|


## ClusterTest - can load stock list and order item - Trace Step Counts

| Origin | Target | Request |  Steps  |
|:------:|:------:|:-------:|:-------:|
|	Shop User	|	api-gateway	|	POST order/{id}	|	8	|
|	Shop User	|	api-gateway	|	GET 	|	4	|
|	Shop User	|	api-gateway	|	GET 	|	4	|
|	Shop User	|	api-gateway	|	GET oauth/callback	|	2	|
|	Shop User	|	api-gateway	|	GET 	|	1	|
|	Shop User	|	cognito	|	GET oauth2/authorize	|	1	|
|	Shop User	|	cognito	|	GET oauth2/login	|	1	|
|	Shop User	|	cognito	|	POST oauth2/login	|	1	|
