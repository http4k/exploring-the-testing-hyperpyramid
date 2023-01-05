package hyperpyramid.adapter

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.port.DepartmentStore
import hyperpyramid.port.DepartmentStoreContract
import org.http4k.core.Credentials
import org.http4k.core.Uri

class FakeDepartmentStoreTest : DepartmentStoreContract {
    override val departmentStore = DepartmentStore.Http(
        Credentials("user", "password"),
        Uri.of("http://store"), FakeDepartmentStore())
}
