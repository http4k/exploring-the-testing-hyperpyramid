package hyperpyramid

import hyperpyramid.ApiGatewaySettings.API_GATEWAY_URL
import hyperpyramid.ApiGatewaySettings.OAUTH_CLIENT_ID
import hyperpyramid.ApiGatewaySettings.OAUTH_CLIENT_SECRET
import hyperpyramid.ApiGatewaySettings.OAUTH_URL
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.extend
import org.http4k.security.InsecureCookieBasedOAuthPersistence
import org.http4k.security.OAuthProvider
import org.http4k.security.OAuthProviderConfig

fun ApiGatewayOAuthProvider(
    env: Environment,
    outgoingHttp: HttpHandler
) = OAuthProvider(
    OAuthProviderConfig(
        OAUTH_URL(env),
        "/oauth2/authorize",
        "/oauth2/token",
        Credentials(OAUTH_CLIENT_ID(env), OAUTH_CLIENT_SECRET(env))
    ),
    outgoingHttp,
    API_GATEWAY_URL(env).extend(Uri.of("/oauth/callback")),
    emptyList(),
    InsecureCookieBasedOAuthPersistence("")
)
