package hyperpyramid.endpoint

import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.ItemPickup
import hyperpyramid.util.Json

fun main() {
    println(Json.asFormatString(ItemPickup(Email.of("asd"), ItemId.of("123"), 1)))
}
