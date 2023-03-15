# InMemoryClusterTest - can load stock list and order item
```mermaid
sequenceDiagram
    title InMemoryClusterTest - can load stock list and order item - Sequence
    participant Website User
	participant api_gateway
	participant website
	participant warehouse
	participant images
	participant cognito
	participant db
	participant image_cache_s3_eu_west_1_amazonaws_com
	participant dept_store
	participant email_eu_west_1_amazonaws_com
	participant event_stream

    Website User->>api_gateway: GET 
    activate api_gateway
    
    api_gateway->>Website User: 307 Temporary Redirect
    deactivate api_gateway
    
	
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
    
	
    Website User->>api_gateway: GET oauth/callback
    activate api_gateway
    
    api_gateway->>cognito: POST oauth2/token
    activate cognito
    
    cognito->>api_gateway: 200 OK
    deactivate cognito
    
    api_gateway->>Website User: 307 Temporary Redirect
    deactivate api_gateway
    
	
    Website User->>api_gateway: GET 
    activate api_gateway
    
    api_gateway->>website: GET 
    activate website
    
    website->>warehouse: GET v1/items
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
    warehouse->>website: 200 OK
    deactivate warehouse
    
    website->>api_gateway: 200 OK
    deactivate website
    
    api_gateway->>Website User: 200 OK
    deactivate api_gateway
    
	
    Website User->>api_gateway: GET img/{.+}
    activate api_gateway
    
    api_gateway->>images: GET img/{id}
    activate images
    
    images->>image_cache_s3_eu_west_1_amazonaws_com: GET {bucketKey:.+}
    activate image_cache_s3_eu_west_1_amazonaws_com
    
    image_cache_s3_eu_west_1_amazonaws_com->>images: 200 OK
    deactivate image_cache_s3_eu_west_1_amazonaws_com
    
    images->>api_gateway: 200 OK
    deactivate images
    
    api_gateway->>Website User: 200 OK
    deactivate api_gateway
    
	
    Website User->>api_gateway: POST order/{id}
    activate api_gateway
    
    api_gateway->>website: POST order/{id}
    activate website
    
    website->>warehouse: POST v1/dispatch
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
	
    warehouse->>db: adjust
    
    db->>warehouse: 
    
	
    warehouse->>dept_store: POST v1/order
    activate dept_store
    
    dept_store->>warehouse: 200 OK
    deactivate dept_store
    
    warehouse->>website: 202 Accepted
    deactivate warehouse
    
	
    website->>email_eu_west_1_amazonaws_com: POST 
    activate email_eu_west_1_amazonaws_com
    
    email_eu_west_1_amazonaws_com->>website: 200 OK
    deactivate email_eu_west_1_amazonaws_com
    
	
    website-)event_stream: CustomerOrder(item=1)
    
    
    website->>api_gateway: 200 OK
    deactivate website
    
    api_gateway->>Website User: 200 OK
    deactivate api_gateway
    
```

```mermaid
C4Context
title InMemoryClusterTest - can load stock list and order item

System(WebsiteUser, "Website User")
System(apigateway, "api-gateway")
System(website, "website")
System(warehouse, "warehouse")
System(images, "images")
System(cognito, "cognito")
ContainerDb(db, "db")
System(imagecaches3euwest1amazonawscom, "image-cache.s3.eu-west-1.amazonaws.com")
System(deptstore, "dept-store")
System(emaileuwest1amazonawscom, "email.eu-west-1.amazonaws.com")
System(eventstream, "event-stream")    
Rel_D(WebsiteUser, apigateway, " ") 
Rel_D(WebsiteUser, cognito, " ") 
Rel_D(apigateway, cognito, " ") 
Rel_D(apigateway, website, " ") 
Rel_D(website, warehouse, " ") 
Rel_D(warehouse, db, " ") 
Rel_D(apigateway, images, " ") 
Rel_D(images, imagecaches3euwest1amazonawscom, " ") 
Rel_D(warehouse, deptstore, " ") 
Rel_D(website, emaileuwest1amazonawscom, " ") 
Rel_D(website, eventstream, " ")     
```


## InMemoryClusterTest - can load stock list and order item - Maximum Trace Depth

| Origin | Target | Request |  Max Depth  |
|:------:|:------:|:-------:|:-----------:|
|	Website User	|	api-gateway	|	GET 	|	4	|
|	Website User	|	api-gateway	|	POST order/{id}	|	4	|
|	Website User	|	api-gateway	|	GET img/{.+}	|	3	|
|	Website User	|	api-gateway	|	GET oauth/callback	|	2	|
|	Website User	|	api-gateway	|	GET 	|	1	|
|	Website User	|	cognito	|	GET oauth2/authorize	|	1	|
|	Website User	|	cognito	|	GET oauth2/login	|	1	|
|	Website User	|	cognito	|	POST oauth2/login	|	1	|


## InMemoryClusterTest - can load stock list and order item - Trace Step Counts

| Origin | Target | Request |  Steps  |
|:------:|:------:|:-------:|:-------:|
|	Website User	|	api-gateway	|	POST order/{id}	|	8	|
|	Website User	|	api-gateway	|	GET 	|	4	|
|	Website User	|	api-gateway	|	GET img/{.+}	|	3	|
|	Website User	|	api-gateway	|	GET oauth/callback	|	2	|
|	Website User	|	api-gateway	|	GET 	|	1	|
|	Website User	|	cognito	|	GET oauth2/authorize	|	1	|
|	Website User	|	cognito	|	GET oauth2/login	|	1	|
|	Website User	|	cognito	|	POST oauth2/login	|	1	|
