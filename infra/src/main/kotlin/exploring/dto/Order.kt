package exploring.dto

data class Order(val customer: Email, val items: List<ItemId>)
