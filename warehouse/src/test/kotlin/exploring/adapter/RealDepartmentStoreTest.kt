package exploring.adapter

import exploring.port.DepartmentStore
import exploring.port.DepartmentStoreContract
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.junit.jupiter.api.Disabled

@Disabled
class RealDepartmentStoreTest : DepartmentStoreContract {
    override val departmentStore = DepartmentStore.Http(Uri.of("http://floormart.com"), JavaHttpClient())
}
