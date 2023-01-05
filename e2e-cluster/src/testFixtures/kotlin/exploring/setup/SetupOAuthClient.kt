package exploring.setup

import dev.forkhandles.result4k.valueOrNull
import exploring.ApiGatewaySettings
import exploring.TheInternet
import org.http4k.connect.amazon.cognito.createUserPool
import org.http4k.connect.amazon.cognito.createUserPoolClient
import org.http4k.connect.amazon.cognito.model.ClientName
import org.http4k.connect.amazon.cognito.model.PoolName
import org.http4k.core.Credentials
import org.http4k.core.with

internal fun SetupOAuthClient(theInternet: TheInternet): CloudSetup = {
    val creds = with(theInternet.cognito.client()) {
        val poolId =
            createUserPool(PoolName.of("pool")).valueOrNull()!!.UserPool.Id!!
        val userPoolClient = createUserPoolClient(
            UserPoolId = poolId,
            ClientName = ClientName.of("Hyperpyramid"),
            GenerateSecret = true
        )
            .valueOrNull()!!.UserPoolClient

        Credentials(userPoolClient.ClientId.value, userPoolClient.ClientSecret!!.value)
    }

    it.with(
        ApiGatewaySettings.OAUTH_CLIENT_ID of creds.user,
        ApiGatewaySettings.OAUTH_CLIENT_SECRET of creds.password
    )
}