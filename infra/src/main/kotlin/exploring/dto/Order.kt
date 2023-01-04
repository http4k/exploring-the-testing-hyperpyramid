package exploring.dto

data class Order(val user: Email, val items: List<ItemId>)
