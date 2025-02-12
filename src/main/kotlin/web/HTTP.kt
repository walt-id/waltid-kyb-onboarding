package id.walt.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.cors.CORSConfig
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing


fun CORSConfig.anyHeader(includeUnsafe: Boolean = false) {
    HttpHeaders.safeHeader.forEach { allowHeader(it) }
    if (includeUnsafe)
        HttpHeaders.unsafeHeader.forEach { allowHeader(it) }
}

val HttpHeaders.safeHeader
    get() = allHeaders.filter { !isUnsafe(it) }

val HttpHeaders.unsafeHeader
    get() = allHeaders.filter { isUnsafe(it) }

val HttpHeaders.allHeaders
    get() = listOf(
        Accept, AcceptCharset, AcceptEncoding, AcceptLanguage, AcceptRanges,
        Age, Allow, ALPN, AuthenticationInfo, Authorization, CacheControl, Connection,
        ContentDisposition, ContentEncoding, ContentLanguage, ContentLength, ContentLocation,
        ContentRange, ContentType, Cookie, DASL, Date, DAV, Depth, Destination, ETag, Expect,
        Expires, From, Forwarded, Host, HTTP2Settings, If, IfMatch, IfModifiedSince,
        IfNoneMatch, IfRange, IfScheduleTagMatch, IfUnmodifiedSince, LastModified,
        Location, LockToken, Link, MaxForwards, MIMEVersion, OrderingType, Origin,
        Overwrite, Position, Pragma, Prefer, PreferenceApplied, ProxyAuthenticate,
        ProxyAuthenticationInfo, ProxyAuthorization, PublicKeyPins, PublicKeyPinsReportOnly,
        Range, Referrer, RetryAfter, ScheduleReply, ScheduleTag, SecWebSocketAccept,
        SecWebSocketExtensions, SecWebSocketKey, SecWebSocketProtocol, SecWebSocketVersion,
        Server, SetCookie, SLUG, StrictTransportSecurity, TE, Timeout, Trailer,
        TransferEncoding, Upgrade, UserAgent, Vary, Via, Warning, WWWAuthenticate,
        AccessControlAllowOrigin, AccessControlAllowMethods, AccessControlAllowCredentials,
        AccessControlAllowHeaders, AccessControlRequestMethod, AccessControlRequestHeaders,
        AccessControlExposeHeaders, AccessControlMaxAge, XHttpMethodOverride, XForwardedHost,
        XForwardedServer, XForwardedProto, XForwardedFor, XRequestId, XCorrelationId
    )

fun CORSConfig.anyMethod() = HttpMethod.DefaultMethods.forEach { allowMethod(it) }

fun Application.configureHTTP() {
    install(AutoHeadResponse)

//    routing {
//        swaggerUI(path = "openapi")
//    }
//    routing {
//        openAPI(path = "openapi")
//    }


    /*
    install(CORS) {
        allowCredentials = true
        anyHost()
        anyMethod()
        anyHeader()
    }*/
    /*install(CORS) {
        allowCredentials = true
        maxAgeInSeconds = 1


        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.Cookie)

        //allowHost("localhost:3000", schemes = listOf("http", "https")) // For dev
        //allowHost("localhost:8080", schemes = listOf("http", "https")) // For dev
        anyHost() // Don't do this in production if possible. Try to limit it.
    }*/
}