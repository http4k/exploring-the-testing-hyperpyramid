package hyperpyramid.adapter

import hyperpyramid.port.DepartmentStoreContract
import org.http4k.client.JavaHttpClient
import org.http4k.core.Credentials
import org.http4k.core.Uri
import org.junit.jupiter.api.Disabled
import java.lang.System.getProperty

@Disabled
class RealDepartmentStoreTest : DepartmentStoreContract {
    override val departmentStore = HttpDepartmentStore(
        Credentials(getProperty("USER"), getProperty("PASSWORD")),
        Uri.of("http://floormart.com"), JavaHttpClient()
    )
}
