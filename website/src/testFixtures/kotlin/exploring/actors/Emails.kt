package exploring.actors

import exploring.dto.Email

fun interface Emails : (Email) -> List<Pair<Email, String>>
