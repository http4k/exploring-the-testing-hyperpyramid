# InMemoryClusterTest - can load stock list and order item
```mermaid
sequenceDiagram
    title InMemoryClusterTest - can load stock list and order item - Sequence
    participant Shop User
	participant api_gateway
	participant shop
	participant warehouse
	participant images
	participant cognito
	participant db
	participant image_cache_s3_eu_west_1_amazonaws_com
	participant dept_store
	participant email_eu_west_1_amazonaws_com
	participant event_stream

    Shop User->>api_gateway: GET 
    activate api_gateway
    
    api_gateway->>Shop User: 307 Temporary Redirect
    deactivate api_gateway
    
	
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
    
	
    Shop User->>api_gateway: GET oauth/callback
    activate api_gateway
    
    api_gateway->>cognito: POST oauth2/token
    activate cognito
    
    cognito->>api_gateway: 200 OK
    deactivate cognito
    
    api_gateway->>Shop User: 307 Temporary Redirect
    deactivate api_gateway
    
	
    Shop User->>api_gateway: GET 
    activate api_gateway
    
    api_gateway->>shop: GET 
    activate shop
    
    shop->>warehouse: GET v1/items
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
    warehouse->>shop: 200 OK
    deactivate warehouse
    
    shop->>api_gateway: 200 OK
    deactivate shop
    
    api_gateway->>Shop User: 200 OK
    deactivate api_gateway
    
	
    Shop User->>api_gateway: GET img/{.+}
    activate api_gateway
    
    api_gateway->>images: GET img/{id}
    activate images
    
    images->>image_cache_s3_eu_west_1_amazonaws_com: GET {bucketKey:.+}
    activate image_cache_s3_eu_west_1_amazonaws_com
    
    image_cache_s3_eu_west_1_amazonaws_com->>images: 200 OK
    deactivate image_cache_s3_eu_west_1_amazonaws_com
    
    images->>api_gateway: 200 OK
    deactivate images
    
    api_gateway->>Shop User: 200 OK
    deactivate api_gateway
    
	
    Shop User->>api_gateway: POST order/{id}
    activate api_gateway
    
    api_gateway->>shop: POST order/{id}
    activate shop
    
    shop->>warehouse: POST v1/dispatch
    activate warehouse
    
    warehouse->>db: items
    
    db->>warehouse: 
    
	
    warehouse->>db: adjust
    
    db->>warehouse: 
    
	
    warehouse->>dept_store: POST v1/order
    activate dept_store
    
    dept_store->>warehouse: 200 OK
    deactivate dept_store
    
    warehouse->>shop: 202 Accepted
    deactivate warehouse
    
	
    shop->>email_eu_west_1_amazonaws_com: POST 
    activate email_eu_west_1_amazonaws_com
    
    email_eu_west_1_amazonaws_com->>shop: 200 OK
    deactivate email_eu_west_1_amazonaws_com
    
	
    shop-)event_stream: CustomerOrder(item=1)
    
    
    shop->>api_gateway: 200 OK
    deactivate shop
    
    api_gateway->>Shop User: 200 OK
    deactivate api_gateway
    
```

```mermaid
C4Context
title InMemoryClusterTest - can load stock list and order item

System(customer, "Customer")
System(apigateway, "api-gateway")
System(shop, "shop")
System(warehouse, "warehouse")
System(images, "images")
System(cognito, "cognito")
ContainerDb(db, "db")
System(imagecaches3euwest1amazonawscom, "image-cache.s3.eu-west-1.amazonaws.com")
System(deptstore, "dept-store")
System(emaileuwest1amazonawscom, "email.eu-west-1.amazonaws.com")
System(eventstream, "event-stream")    
Rel_D(customer, apigateway, " ") 
Rel_D(customer, cognito, " ") 
Rel_D(apigateway, cognito, " ") 
Rel_D(apigateway, shop, " ") 
Rel_D(shop, warehouse, " ") 
Rel_D(warehouse, db, " ") 
Rel_D(apigateway, images, " ") 
Rel_D(images, imagecaches3euwest1amazonawscom, " ") 
Rel_D(warehouse, deptstore, " ") 
Rel_D(shop, emaileuwest1amazonawscom, " ") 
Rel_D(shop, eventstream, " ")     
```


## InMemoryClusterTest - can load stock list and order item - Maximum Trace Depth

| Origin | Target | Request |  Max Depth  |
|:------:|:------:|:-------:|:-----------:|
|	Shop User	|	api-gateway	|	GET 	|	4	|
|	Shop User	|	api-gateway	|	POST order/{id}	|	4	|
|	Shop User	|	api-gateway	|	GET img/{.+}	|	3	|
|	Shop User	|	api-gateway	|	GET oauth/callback	|	2	|
|	Shop User	|	api-gateway	|	GET 	|	1	|
|	Shop User	|	cognito	|	GET oauth2/authorize	|	1	|
|	Shop User	|	cognito	|	GET oauth2/login	|	1	|
|	Shop User	|	cognito	|	POST oauth2/login	|	1	|


## InMemoryClusterTest - can load stock list and order item - Trace Step Counts

| Origin | Target | Request |  Steps  |
|:------:|:------:|:-------:|:-------:|
|	Shop User	|	api-gateway	|	POST order/{id}	|	8	|
|	Shop User	|	api-gateway	|	GET 	|	4	|
|	Shop User	|	api-gateway	|	GET img/{.+}	|	3	|
|	Shop User	|	api-gateway	|	GET oauth/callback	|	2	|
|	Shop User	|	api-gateway	|	GET 	|	1	|
|	Shop User	|	cognito	|	GET oauth2/authorize	|	1	|
|	Shop User	|	cognito	|	GET oauth2/login	|	1	|
|	Shop User	|	cognito	|	POST oauth2/login	|	1	|
