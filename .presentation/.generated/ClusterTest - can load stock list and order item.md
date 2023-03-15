#ClusterTest - can load stock list and order item
```mermaid
sequenceDiagram
    title ClusterTest - can load stock list and order item - Sequence
    participant Website User
	participant api-gateway
	participant website
	participant warehouse
	participant cognito
	participant db
	participant dept-store
	participant email.eu-west-1.amazonaws.com
	participant event-stream

    Website User->>api-gateway: GET 
    activate api-gateway
    
    api-gateway->>Website User: 307 Temporary Redirect
    deactivate api-gateway
    
	
    Website User->>cognito: GET oauth2/authorize
    activate cognito
    
    cognito->>Website User: 302 Found
    deactivate cognito
    
	
    Website User->>cognito: GET oauth2/login
    activate cognito
    
    cognito->>Website User: 200 OK
    deactivate cognito
    
	
    Website User->>cognito: POST oauth2/login
    activate cognito
    
    cognito->>Website User: 303 See Other
    deactivate cognito
    
	
    Website User->>api-gateway: GET oauth/callback
    activate api-gateway
    
    api-gateway->>cognito: POST oauth2/token
    activate cognito
    
    cognito->>api-gateway: 200 OK
    deactivate cognito
    
    api-gateway->>Website User: 307 Temporary Redirect
    deactivate api-gateway
    
	
    Website User->>api-gateway: GET 
    activate api-gateway
    
    api-gateway->>website: GET 
    activate website
    
    website->>warehouse: GET v1/items
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
    warehouse->>website: 200 OK
    deactivate warehouse
    
    website->>api-gateway: 200 OK
    deactivate website
    
    api-gateway->>Website User: 200 OK
    deactivate api-gateway
    
	
    Website User->>api-gateway: GET 
    activate api-gateway
    
    api-gateway->>website: GET 
    activate website
    
    website->>warehouse: GET v1/items
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
    warehouse->>website: 200 OK
    deactivate warehouse
    
    website->>api-gateway: 200 OK
    deactivate website
    
    api-gateway->>Website User: 200 OK
    deactivate api-gateway
    
	
    Website User->>api-gateway: POST order/{id}
    activate api-gateway
    
    api-gateway->>website: POST order/{id}
    activate website
    
    website->>warehouse: POST v1/dispatch
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
	
    warehouse->>db: adjust
    
    db->>warehouse: 
    
	
    warehouse->>dept-store: POST v1/order
    activate dept-store
    
    dept-store->>warehouse: 200 OK
    deactivate dept-store
    
    warehouse->>website: 200 OK
    deactivate warehouse
    
	
    website->>email.eu-west-1.amazonaws.com: POST 
    activate email.eu-west-1.amazonaws.com
    
    email.eu-west-1.amazonaws.com->>website: 200 OK
    deactivate email.eu-west-1.amazonaws.com
    
	
    website-)event-stream: CustomerOrder(item=1)
    
    
    website->>api-gateway: 200 OK
    deactivate website
    
    api-gateway->>Website User: 200 OK
    deactivate api-gateway
    
```

```mermaid
C4Context
title ClusterTest - can load stock list and order item

System(WebsiteUser, "Website User")
System(apigateway, "api-gateway")
System(website, "website")
System(warehouse, "warehouse")
System(cognito, "cognito")
ContainerDb(db, "db")
System(deptstore, "dept-store")
System(emaileuwest1amazonawscom, "email.eu-west-1.amazonaws.com")
System(eventstream, "event-stream")    
Rel_D(WebsiteUser, apigateway, " ") 
Rel_D(WebsiteUser, cognito, " ") 
Rel_D(apigateway, cognito, " ") 
Rel_D(apigateway, website, " ") 
Rel_D(website, warehouse, " ") 
Rel_D(warehouse, db, " ") 
Rel_D(warehouse, deptstore, " ") 
Rel_D(website, emaileuwest1amazonawscom, " ") 
Rel_D(website, eventstream, " ")     
```


## ClusterTest - can load stock list and order item - Maximum Trace Depth

| Origin | Target | Request |  Max Depth  |
|:------:|:------:|:-------:|:-----------:|
|	Website User	|	api-gateway	|	GET 	|	4	|
|	Website User	|	api-gateway	|	GET 	|	4	|
|	Website User	|	api-gateway	|	POST order/{id}	|	4	|
|	Website User	|	api-gateway	|	GET oauth/callback	|	2	|
|	Website User	|	api-gateway	|	GET 	|	1	|
|	Website User	|	cognito	|	GET oauth2/authorize	|	1	|
|	Website User	|	cognito	|	GET oauth2/login	|	1	|
|	Website User	|	cognito	|	POST oauth2/login	|	1	|


## ClusterTest - can load stock list and order item - Trace Step Counts

| Origin | Target | Request |  Steps  |
|:------:|:------:|:-------:|:-------:|
|	Website User	|	api-gateway	|	POST order/{id}	|	8	|
|	Website User	|	api-gateway	|	GET 	|	4	|
|	Website User	|	api-gateway	|	GET 	|	4	|
|	Website User	|	api-gateway	|	GET oauth/callback	|	2	|
|	Website User	|	api-gateway	|	GET 	|	1	|
|	Website User	|	cognito	|	GET oauth2/authorize	|	1	|
|	Website User	|	cognito	|	GET oauth2/login	|	1	|
|	Website User	|	cognito	|	POST oauth2/login	|	1	|
