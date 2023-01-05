package hyperpyramid.app

import org.http4k.core.Filter
import org.http4k.core.NoOp
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse

fun Debug(debug: Boolean) = if (debug) PrintRequestAndResponse() else Filter.NoOp
