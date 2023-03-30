package hyperpyramid.adapter

import dev.forkhandles.result4k.failureOrNull
import hyperpyramid.FakeDepartmentStore
import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import hyperpyramid.port.DepartmentStoreContract
import org.http4k.chaos.ChaosBehaviours.ReturnStatus
import org.http4k.core.Credentials
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Uri
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class FakeDepartmentStoreTest : DepartmentStoreContract {
    private val fake = FakeDepartmentStore()
    override val departmentStore = HttpDepartmentStore(
        Credentials("user", "password"),
        Uri.of("http://store"), fake
    )

    @Test
    fun `when a collection blows up`() {
        val email = Email.of("01234567890")
        val id = ItemId.of("123")
        fake.misbehave(ReturnStatus(INTERNAL_SERVER_ERROR))

        expectThat(
            departmentStore.collection(email, id).failureOrNull()?.message
        ).isEqualTo("Internal Server Error")
    }

}
