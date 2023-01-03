package exploring.adapter

import exploring.FakeDepartmentStore
import exploring.port.DepartmentStore
import exploring.port.DepartmentStoreContract
import org.http4k.core.Uri

class FakeDepartmentStoreTest : DepartmentStoreContract {
    override val departmentStore = DepartmentStore.Http(Uri.of("http://store"), FakeDepartmentStore())
}
