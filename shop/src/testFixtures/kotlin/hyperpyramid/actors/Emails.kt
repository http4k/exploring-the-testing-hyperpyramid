package hyperpyramid.actors

import hyperpyramid.dto.Email

fun interface Emails : (Email) -> List<Pair<Email, String>>
