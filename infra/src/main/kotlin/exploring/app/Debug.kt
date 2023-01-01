package exploring.app

import org.http4k.core.Filter
import org.http4k.core.NoOp
import org.http4k.filter.DebuggingFilters

fun Debug(debug: Boolean) = if (debug) DebuggingFilters.PrintRequestAndResponse() else Filter.NoOp
